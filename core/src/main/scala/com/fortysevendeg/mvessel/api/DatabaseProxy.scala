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
