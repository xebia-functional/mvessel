package com.fortysevendeg.mvessel

import java.io.Closeable

import android.database.sqlite.SQLiteDatabase
import android.database.{Cursor, SQLException}
import Database._
import com.fortysevendeg.mvessel.util.ReflectionOps._

import scala.util.{Failure, Success, Try}

class Database(
  val name: String,
  timeout: Long,
  retry: Long,
  flags: Int) extends Closeable {

  val database = executeWithRetry(retry, timeout)(openDatabase(name, flags))

  def openDatabase(name: String, flags: Int) = SQLiteDatabase.openDatabase(name, javaNull, flags)

  def rawQuery(sql: String): Cursor = rawQuery(sql, javaNull)

  def rawQuery(sql: String, selectionArgs: Array[String]): Cursor =
    executeWithRetry(retry, timeout)(database.rawQuery(sql, selectionArgs))

  def execSQL(sql: String): Unit = execSQL(sql, javaNull)

  def execSQL(sql: String, bindArgs: Array[AnyRef]): Unit =
    executeWithRetry(retry, timeout) {
      Option(bindArgs) match {
        case Some(array) if !array.isEmpty => database.execSQL(sql, array)
        case _ => database.execSQL(sql)
      }
    }

  def changedRowCount(): Int = callToChangedRowCount(database) getOrElse 0

  def setTransactionSuccessful(): Unit = transactionState(TransactionSuccessful)

  def beginTransaction(): Unit = transactionState(StartsTransaction)

  def endTransaction(): Unit = transactionState(FinishTransaction)

  def close(): Unit = transactionState(Close)

  private[this] def transactionState(transactionState: TransactionState): Unit =
    executeWithRetry(retry, timeout) {
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

  def isLockedException(exception: SQLException): Boolean =
    lockedExceptionClass exists (_.isAssignableFrom(exception.getClass))

  def executeWithRetry[T](retry: Long, timeout: Long)(f: => T): T =
    executeWithRetry(0, retry, timeout)(f)

  def callToChangedRowCount(database: SQLiteDatabase): Option[Int] = lastChangeCount(database)

  private[this] def executeWithRetry[T](elapsed: Long, retry: Long, timeout: Long)(f: => T): T =
    Try(f) match {
      case Success(d) => d
      case Failure(e: SQLException) if isLockedException(e) && (elapsed + retry < timeout) =>
        Thread.sleep(retry)
        executeWithRetry(elapsed + retry, retry, timeout)(f)
      case Failure(e) => throw e
    }

}
