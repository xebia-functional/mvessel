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

import android.content.ContentResolver
import android.database.{CharArrayBuffer, ContentObserver, Cursor, DataSetObserver}
import android.net.Uri
import android.os.Bundle
import com.fortysevendeg.mvessel.api.CursorType.Value
import com.fortysevendeg.mvessel.api.{CursorProxy, CursorType}

class AndroidCursor(val cursor: Cursor)
  extends CursorProxy
  with Cursor
  with AndroidCursorData {

  override def getCursorType(columnIndex: Int): Value =
    cursor.getType(columnIndex) match {
      case Cursor.FIELD_TYPE_NULL => CursorType.Null
      case Cursor.FIELD_TYPE_INTEGER => CursorType.Integer
      case Cursor.FIELD_TYPE_FLOAT => CursorType.Float
      case Cursor.FIELD_TYPE_BLOB => CursorType.Blob
      case _ => CursorType.String
    }

  override def isClosed: Boolean =
    cursor.isClosed

  override def isBeforeFirst: Boolean =
    cursor.isBeforeFirst

  override def isAfterLast: Boolean =
    cursor.isAfterLast

  override def isLast: Boolean =
    cursor.isLast

  override def isFirst: Boolean =
    cursor.isFirst

  override def getCount: Int =
    cursor.getCount

  override def getPosition: Int =
    cursor.getPosition

  override def getColumnCount: Int =
    cursor.getColumnCount

  override def getColumnNames: Array[String] =
    cursor.getColumnNames

  override def getColumnIndex(columnName: String): Int =
    cursor.getColumnIndex(columnName)

  override def getColumnIndexOrThrow(columnName: String): Int =
    cursor.getColumnIndexOrThrow(columnName)

  override def getColumnName(columnIndex: Int): String =
    cursor.getColumnName(columnIndex)

  override def moveToFirst(): Boolean =
    cursor.moveToFirst()

  override def move(offset: Int): Boolean =
    cursor.move(offset)

  override def moveToNext(): Boolean =
    cursor.moveToNext()

  override def moveToPrevious(): Boolean =
    cursor.moveToPrevious()

  override def moveToLast(): Boolean =
    cursor.moveToLast()

  override def moveToPosition(position: Int): Boolean =
    cursor.moveToPosition(position)

  override def close(): Unit =
    cursor.close()

  override def getWantsAllOnMoveCalls: Boolean =
    cursor.getWantsAllOnMoveCalls

  override def registerContentObserver(observer: ContentObserver): Unit =
    cursor.registerContentObserver(observer)

  override def unregisterContentObserver(observer: ContentObserver): Unit =
    cursor.unregisterContentObserver(observer)

  override def registerDataSetObserver(observer: DataSetObserver): Unit =
    cursor.registerDataSetObserver(observer)

  override def unregisterDataSetObserver(observer: DataSetObserver): Unit =
    cursor.unregisterDataSetObserver(observer)

  override def deactivate(): Unit =
    cursor.deactivate()

  override def copyStringToBuffer(columnIndex: Int, buffer: CharArrayBuffer): Unit =
    cursor.copyStringToBuffer(columnIndex, buffer)

  override def requery(): Boolean = cursor.requery()

  override def setNotificationUri(cr: ContentResolver, uri: Uri): Unit =
    cursor.setNotificationUri(cr, uri)

  override def getExtras: Bundle =
    cursor.getExtras

  override def respond(extras: Bundle): Bundle =
    cursor.respond(extras)
}

trait AndroidCursorData {
  self: AndroidCursor =>

  override def isNull(columnIndex: Int): Boolean = cursor.isNull(columnIndex)

  override def getDouble(columnIndex: Int): Double = cursor.getDouble(columnIndex)

  override def getFloat(columnIndex: Int): Float = cursor.getFloat(columnIndex)

  override def getLong(columnIndex: Int): Long = cursor.getLong(columnIndex)

  override def getShort(columnIndex: Int): Short = cursor.getShort(columnIndex)

  override def getInt(columnIndex: Int): Int = cursor.getInt(columnIndex)

  override def getBlob(columnIndex: Int): Array[Byte] = cursor.getBlob(columnIndex)

  override def getString(columnIndex: Int): String = cursor.getString(columnIndex)

  override def getType(columnIndex: Int): Int = cursor.getType(columnIndex)
}