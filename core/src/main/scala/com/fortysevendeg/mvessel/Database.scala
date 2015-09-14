package com.fortysevendeg.mvessel

import java.io.Closeable

import com.fortysevendeg.mvessel.Database._
import com.fortysevendeg.mvessel.api.{DatabaseProxy, CursorProxy, DatabaseProxyFactory}

import scala.util.{Failure, Success, Try}

class Database[T <: CursorProxy](
  databaseFactory: DatabaseProxyFactory[T],
  val name: String,
  timeout: Long,
  retry: Long,
  flags: Int) extends Closeable {

  val database = executeWithRetry(databaseFactory, retry, timeout)(openDatabase(name, flags))

  def openDatabase(name: String, flags: Int): DatabaseProxy[T] = databaseFactory.openDatabase(name, flags)

  def rawQuery(sql: String): T = rawQuery(sql, javaNull)

  def rawQuery(sql: String, selectionArgs: Array[String]): T =
    executeWithRetry(databaseFactory, retry, timeout)(database.rawQuery(sql, selectionArgs))

  def execSQL(sql: String): Unit = execSQL(sql, javaNull)

  def execSQL(sql: String, bindArgs: Array[AnyRef]): Unit =
    executeWithRetry(databaseFactory, retry, timeout) {
      Option(bindArgs) match {
        case Some(array) if !array.isEmpty => database.execSQL(sql, Option(array))
        case _ => database.execSQL(sql)
      }
    }

  def changedRowCount(): Int = database.changedRowCount getOrElse 0

  def setTransactionSuccessful(): Unit = transactionState(TransactionSuccessful)

  def beginTransaction(): Unit = transactionState(StartsTransaction)

  def endTransaction(): Unit = transactionState(FinishTransaction)

  def close(): Unit = transactionState(Close)

  private[this] def transactionState(transactionState: TransactionState): Unit =
    executeWithRetry(databaseFactory, retry, timeout) {
      transactionState match {
        case TransactionSuccessful => database.setTransactionSuccessful()
        case StartsTransaction => database.beginTransaction()
        case FinishTransaction => database.endTransaction()
        case Close => database.close()
      }
    }
}

object Database {

  sealed trait TransactionState

  case object TransactionSuccessful extends TransactionState

  case object StartsTransaction extends TransactionState

  case object FinishTransaction extends TransactionState

  case object Close extends TransactionState

  def executeWithRetry[R, T <: CursorProxy](
    databaseWrapperFactory: DatabaseProxyFactory[T],
    retry: Long, 
    timeout: Long)(f: => R): R =
    withRetry(databaseWrapperFactory, 0, retry, timeout)(f)

  private[this] def withRetry[R, T <: CursorProxy](
    databaseWrapperFactory: DatabaseProxyFactory[T],
    elapsed: Long, 
    retry: Long, 
    timeout: Long)(f: => R): R =
    Try(f) match {
      case Success(d) => d
      case Failure(e: RuntimeException) if databaseWrapperFactory.isLockedException(e) && (elapsed + retry < timeout) =>
        Thread.sleep(retry)
        withRetry(databaseWrapperFactory, elapsed + retry, retry, timeout)(f)
      case Failure(e) => throw e
    }

}
