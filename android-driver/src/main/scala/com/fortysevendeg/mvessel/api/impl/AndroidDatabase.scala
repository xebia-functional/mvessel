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

  override def rawQuery(sql: String, selectionArgs: Array[String]): CursorProxy = ???

  override def execSQL(sql: String, bindArgs: Option[Array[AnyRef]]): Unit = ???

  override def setTransactionSuccessful(): Unit = ???

  override def endTransaction(): Unit = ???

  override def changedRowCount: Option[Int] = lastChangeCount(sqliteDatabase)

  override def isOpen: Boolean = ???

  override def close(): Unit = ???

  override def beginTransaction(): Unit = ???

  override def getVersion: Int = sqliteDatabase.getVersion

  override def getDriverName: String = AndroidDriver.driverName

  override def getDriverVersion: String = AndroidDriver.driverName
}