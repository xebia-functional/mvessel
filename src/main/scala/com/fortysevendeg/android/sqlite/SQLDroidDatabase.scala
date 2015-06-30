package com.fortysevendeg.android.sqlite

import java.lang.reflect.Method

import android.database.sqlite.SQLiteDatabase
import android.database.{Cursor, SQLException}
import com.fortysevendeg.android.sqlite.SQLDroidDatabase._

import scala.util.{Failure, Success, Try}

class SQLDroidDatabase(val name: String, timeout: Long, retry: Long, flags: Int) {

  val database = executeWithRetry(retry, timeout)(openDatabase(name, flags))

  def openDatabase(name: String, flags: Int) = SQLiteDatabase.openDatabase(name, javaNull, flags)

  def rawQuery(sql: String): Cursor = rawQuery(sql, Array.empty)

  def rawQuery(sql: String, selectionArgs: Array[String]): Cursor =
    executeWithRetry(retry, timeout)(database.rawQuery(sql, selectionArgs))

  def execSQL(sql: String): Unit = execSQL(sql, Array.empty)

  def execSQL(sql: String, bindArgs: Array[AnyRef]): Unit =
    executeWithRetry(retry, timeout) {
      if (bindArgs.isEmpty) database.execSQL(sql)
      else database.execSQL(sql, bindArgs)
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

object SQLDroidDatabase {

  sealed trait TransactionState

  object TransactionSuccessful  extends TransactionState

  object StartsTransaction      extends TransactionState

  object FinishTransaction      extends TransactionState

  object Close                  extends TransactionState

  def isLockedException(exception: SQLException): Boolean =
    lockedExceptionClass exists (_.isAssignableFrom(exception.getClass))

  def executeWithRetry[T](retry: Long, timeout: Long)(f: => T): T =
    executeWithRetry(0, retry, timeout)(f)

  def callToChangedRowCount(database: SQLiteDatabase): Option[Int] =
    changedRowCountMethod map (_.invoke(database).asInstanceOf[Int])

  /**
   * Added in API level 11
   */
  private[this] val lockedExceptionClass: Option[Class[_]] =
    Try(Class.forName(
      "android.database.sqlite.SQLiteDatabaseLockedException",
      false,
      getClass.getClassLoader)).toOption

  private[this] def executeWithRetry[T](elapsed: Long, retry: Long, timeout: Long)(f: => T): T =
    Try(f) match {
      case Success(d) => d
      case Failure(e: SQLException) if isLockedException(e) && (elapsed + retry < timeout) =>
        Thread.sleep(retry)
        executeWithRetry(elapsed + retry, retry, timeout)(f)
      case Failure(e) => throw e
    }

  /**
   * The count of changed rows on Android it's a call to a package-private method
   */
  private[this] val changedRowCountMethod: Option[Method] =
    Try(classOf[SQLiteDatabase].getDeclaredMethod("lastChangeCount")) match {
      case Success(m) =>
        m.setAccessible(true)
        Some(m)
      case _ => None
    }

}
