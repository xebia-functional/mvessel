/*
 * The MIT License (MIT)
 *
 * Copyright (C) 2012 47 Degrees, LLC http://47deg.com hello@47deg.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 */

package com.fortysevendeg.mvessel.api.impl

import android.database.sqlite.SQLiteDatabase
import com.fortysevendeg.mvessel.{AndroidDriver, _}
import com.fortysevendeg.mvessel.api.{DatabaseProxy, DatabaseProxyFactory}
import com.fortysevendeg.mvessel.util.ReflectionOps._

class AndroidDatabaseFactory extends DatabaseProxyFactory[AndroidCursor] {

  override def openDatabase(name: String, flags: Int): DatabaseProxy[AndroidCursor] =
    new AndroidDatabase(SQLiteDatabase.openDatabase(name, javaNull, flags))

  override def isLockedException(exception: RuntimeException): Boolean =
    lockedExceptionClass exists (_.isAssignableFrom(exception.getClass))
}

class AndroidDatabase(sqliteDatabase: SQLiteDatabase) extends DatabaseProxy[AndroidCursor] {

  override def rawQuery(sql: String, selectionArgs: Array[String]): AndroidCursor =
    new AndroidCursor(sqliteDatabase.rawQuery(sql, selectionArgs))

  override def execSQL(sql: String, bindArgs: Option[Array[AnyRef]]): Unit =
    bindArgs match {
      case Some(args) => sqliteDatabase.execSQL(sql, args)
      case _ => sqliteDatabase.execSQL(sql)
    }

  override def setTransactionSuccessful(): Unit = sqliteDatabase.setTransactionSuccessful()

  override def endTransaction(): Unit = sqliteDatabase.endTransaction()

  override def changedRowCount: Option[Int] = lastChangeCount(sqliteDatabase)

  override def inTransaction: Boolean = sqliteDatabase.inTransaction()

  override def isOpen: Boolean = sqliteDatabase.isOpen

  override def close(): Unit = sqliteDatabase.close()

  override def beginTransaction(): Unit = sqliteDatabase.beginTransaction()

  override def getVersion: Int = sqliteDatabase.getVersion

  override def getDriverName: String = AndroidDriver.driverName

  override def getDriverVersion: String = AndroidDriver.driverVersion
}