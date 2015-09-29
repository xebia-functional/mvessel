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

trait CursorProxy {

  def isClosed: Boolean

  def isBeforeFirst: Boolean

  def isAfterLast: Boolean

  def isFirst: Boolean

  def isLast: Boolean

  def move(offset: Int): Boolean

  def moveToPosition(position: Int): Boolean

  def moveToLast(): Boolean

  def moveToFirst(): Boolean

  def moveToNext(): Boolean

  def moveToPrevious(): Boolean

  def getPosition: Int

  def getCount: Int

  def getColumnCount: Int

  def getColumnName(columnIndex: Int): String

  def getColumnIndexOrThrow(columnName: String): Int

  def isNull(columnIndex: Int): Boolean

  def getCursorType(columnIndex: Int): CursorType.Value

  def getString(columnIndex: Int): String

  def getShort(columnIndex: Int): Short

  def getInt(columnIndex: Int): Int

  def getLong(columnIndex: Int): Long

  def getDouble(columnIndex: Int): Double

  def getFloat(columnIndex: Int): Float

  def getBlob(columnIndex: Int): Array[Byte]

  def close(): Unit

}

object CursorType {

  sealed trait Value

  case object String extends Value

  case object Integer extends Value

  case object Float extends Value

  case object Blob extends Value

  case object Null extends Value
}
