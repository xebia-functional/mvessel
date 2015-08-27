package com.fortysevendeg.mvessel.api.impl

import android.database.sqlite.SQLiteDatabase
import com.fortysevendeg.mvessel.AndroidDriver
import com.fortysevendeg.mvessel.api.{CursorProxy, DatabaseProxy, DatabaseProxyFactory}
import com.fortysevendeg.mvessel.util.ReflectionOps._

class AndroidDatabaseFactory extends DatabaseProxyFactory {

  override def openDatabase(name: String, flags: Int): DatabaseProxy =
    new AndroidDatabase(SQLiteDatabase.openDatabase(name, null, flags))

  override def isLockedException(exception: RuntimeException): Boolean =
    lockedExceptionClass exists (_.isAssignableFrom(exception.getClass))

}

class AndroidDatabase(sqliteDatabase: SQLiteDatabase) extends DatabaseProxy {

  override def rawQuery(sql: String, selectionArgs: Array[String]): CursorProxy =
    new AndroidCursor(sqliteDatabase.rawQuery(sql, selectionArgs))

  override def execSQL(sql: String, bindArgs: Option[Array[AnyRef]]): Unit =
    bindArgs match {
      case Some(args) => sqliteDatabase.execSQL(sql, args)
      case _ => sqliteDatabase.execSQL(sql)
    }

  override def setTransactionSuccessful(): Unit = sqliteDatabase.setTransactionSuccessful()

  override def endTransaction(): Unit = sqliteDatabase.endTransaction()

  override def changedRowCount: Option[Int] = lastChangeCount(sqliteDatabase)

  override def isOpen: Boolean = sqliteDatabase.isOpen

  override def close(): Unit = sqliteDatabase.clone()

  override def beginTransaction(): Unit = sqliteDatabase.beginTransaction()

  override def getVersion: Int = sqliteDatabase.getVersion

  override def getDriverName: String = AndroidDriver.driverName

  override def getDriverVersion: String = AndroidDriver.driverName
}