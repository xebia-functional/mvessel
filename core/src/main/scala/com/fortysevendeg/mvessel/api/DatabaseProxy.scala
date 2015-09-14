package com.fortysevendeg.mvessel.api

trait DatabaseProxyFactory[T <: CursorProxy] {

  def openDatabase(name: String, flags: Int): DatabaseProxy[T]

  def isLockedException(exception: RuntimeException): Boolean

}

trait DatabaseProxy[T <: CursorProxy] {

  def rawQuery(sql: String, selectionArgs: Array[String]): T

  def execSQL(sql: String, bindArgs: Option[Array[AnyRef]] = None): Unit

  def changedRowCount: Option[Int]

  def setTransactionSuccessful(): Unit

  def beginTransaction(): Unit

  def endTransaction(): Unit

  def isOpen: Boolean

  def getVersion: Int

  def getDriverName: String

  def getDriverVersion: String

  def close(): Unit

}
