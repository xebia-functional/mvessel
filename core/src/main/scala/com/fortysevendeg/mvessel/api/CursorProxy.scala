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
