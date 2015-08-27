package com.fortysevendeg.mvessel.api.impl

import android.database.Cursor
import com.fortysevendeg.mvessel.api.CursorType.Value
import com.fortysevendeg.mvessel.api.CursorProxy

class AndroidCursor(cursor: Cursor) extends CursorProxy {

  override def isClosed: Boolean = ???

  override def getType(columnIndex: Int): Value = ???

  override def moveToFirst(): Boolean = ???

  override def move(offset: Int): Boolean = ???

  override def isBeforeFirst: Boolean = ???

  override def getPosition: Int = ???

  override def moveToNext(): Boolean = ???

  override def isAfterLast: Boolean = ???

  override def moveToPrevious(): Boolean = ???

  override def isLast: Boolean = ???

  override def getDouble(columnIndex: Int): Double = ???

  override def getColumnCount: Int = ???

  override def moveToLast(): Boolean = ???

  override def isFirst: Boolean = ???

  override def getCount: Int = ???

  override def getColumnIndexOrThrow(columnName: String): Int = ???

  override def getColumnName(columnIndex: Int): String = ???

  override def getFloat(columnIndex: Int): Float = ???

  override def getLong(columnIndex: Int): Long = ???

  override def moveToPosition(position: Int): Boolean = ???

  override def getShort(columnIndex: Int): Short = ???

  override def isNull(columnIndex: Int): Boolean = ???

  override def close(): Unit = ???

  override def getInt(columnIndex: Int): Int = ???

  override def getBlob(columnIndex: Int): Array[Byte] = ???

  override def getString(columnIndex: Int): String = ???
}
