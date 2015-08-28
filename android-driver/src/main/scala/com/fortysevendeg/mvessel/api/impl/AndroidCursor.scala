package com.fortysevendeg.mvessel.api.impl

import android.database.Cursor
import com.fortysevendeg.mvessel.api.CursorType.Value
import com.fortysevendeg.mvessel.api.{CursorType, CursorProxy}

class AndroidCursor(cursor: Cursor) extends CursorProxy {

  override def getType(columnIndex: Int): Value = cursor.getType(columnIndex) match {
    case Cursor.FIELD_TYPE_NULL => CursorType.Null
        case Cursor.FIELD_TYPE_INTEGER => CursorType.Integer
        case Cursor.FIELD_TYPE_FLOAT => CursorType.Float
        case Cursor.FIELD_TYPE_STRING => CursorType.String
        case Cursor.FIELD_TYPE_BLOB => CursorType.Blob
  }

  override def isClosed: Boolean = cursor.isClosed

  override def isBeforeFirst: Boolean = cursor.isBeforeFirst

  override def isAfterLast: Boolean = cursor.isAfterLast

  override def isLast: Boolean = cursor.isLast

  override def isFirst: Boolean = cursor.isFirst

  override def getCount: Int = cursor.getCount

  override def getPosition: Int = cursor.getPosition

  override def getColumnCount: Int = cursor.getColumnCount

  override def getColumnIndexOrThrow(columnName: String): Int = cursor.getColumnIndexOrThrow(columnName)

  override def getColumnName(columnIndex: Int): String = cursor.getColumnName(columnIndex)

  override def moveToFirst(): Boolean = cursor.moveToFirst()

  override def move(offset: Int): Boolean = cursor.move(offset)

  override def moveToNext(): Boolean = cursor.moveToNext()

  override def moveToPrevious(): Boolean = cursor.moveToPrevious()

  override def moveToLast(): Boolean = cursor.moveToLast()

  override def moveToPosition(position: Int): Boolean = cursor.moveToPosition(position)

  override def isNull(columnIndex: Int): Boolean = cursor.isNull(columnIndex)

  override def getDouble(columnIndex: Int): Double = cursor.getDouble(columnIndex)

  override def getFloat(columnIndex: Int): Float = cursor.getFloat(columnIndex)

  override def getLong(columnIndex: Int): Long = cursor.getLong(columnIndex)

  override def getShort(columnIndex: Int): Short = cursor.getShort(columnIndex)

  override def getInt(columnIndex: Int): Int = cursor.getInt(columnIndex)

  override def getBlob(columnIndex: Int): Array[Byte] = cursor.getBlob(columnIndex)

  override def getString(columnIndex: Int): String = cursor.getString(columnIndex)

  override def close(): Unit = cursor.close()
}
