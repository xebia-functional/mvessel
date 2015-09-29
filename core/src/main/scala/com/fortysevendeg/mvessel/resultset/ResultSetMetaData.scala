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

package com.fortysevendeg.mvessel.resultset

import java.sql.{ResultSetMetaData => SQLResultSetMetaData, Types}

import com.fortysevendeg.mvessel.{WrapperNotSupported, _}
import com.fortysevendeg.mvessel.api.{CursorType, CursorProxy}
import com.fortysevendeg.mvessel.logging.LogWrapper

class ResultSetMetaData(
  cursor: CursorProxy,
  log: LogWrapper)
  extends SQLResultSetMetaData
  with WrapperNotSupported {

  override def getCatalogName(column: Int): String = log.notImplemented("")

  override def getSchemaName(column: Int): String = log.notImplemented("")

  override def getTableName(column: Int): String = log.notImplemented("")

  override def getColumnCount: Int = cursor.getColumnCount

  override def getColumnName(column: Int): String =
    cursor.getColumnName(column - 1)

  override def getColumnLabel(column: Int): String =
    getColumnName(column)

  override def getColumnType(column: Int): Int =
    prepareCursor match {
      case (None, _) =>
        Types.NULL
      case (Some(c), maybeInt) =>
        val nativeType = cursor.getCursorType(column - 1)
        maybeInt map cursor.moveToPosition
        fromWrapperType(nativeType)
    }

  override def getColumnTypeName(column: Int): String =
    typeName(getColumnType(column))

  override def getColumnDisplaySize(column: Int): Int =
    throw new UnsupportedOperationException

  override def getColumnClassName(column: Int): String =
    throw new UnsupportedOperationException

  override def getPrecision(column: Int): Int = log.notImplemented(0)

  override def getScale(column: Int): Int = log.notImplemented(0)

  override def isDefinitelyWritable(column: Int): Boolean = log.notImplemented(false)

  override def isCurrency(column: Int): Boolean = log.notImplemented(false)

  override def isCaseSensitive(column: Int): Boolean = log.notImplemented(false)

  override def isSearchable(column: Int): Boolean = log.notImplemented(false)

  override def isReadOnly(column: Int): Boolean = log.notImplemented(false)

  override def isWritable(column: Int): Boolean = log.notImplemented(false)

  override def isNullable(column: Int): Int = log.notImplemented(SQLResultSetMetaData.columnNullableUnknown)

  override def isSigned(column: Int): Boolean =
    getColumnType(column) match {
      case (Types.INTEGER | Types.FLOAT) => true
      case t => false
    }

  override def isAutoIncrement(column: Int): Boolean =
    throw new UnsupportedOperationException

  private[this] def prepareCursor: (Option[CursorProxy], Option[Int]) =
    (cursor.getCount, cursor.isBeforeFirst || cursor.isAfterLast) match {
      case (0, _) =>
        (None, None)
      case (_, true) =>
        val position = cursor.getPosition
        cursor.moveToFirst()
        (Some(cursor), Some(position))
      case _ =>
        (Some(cursor), None)
    }

  private[this] def fromWrapperType(cursorType: CursorType.Value): Int =
    cursorType match {
      case CursorType.Null => Types.NULL
      case CursorType.Integer => Types.INTEGER
      case CursorType.Float => Types.FLOAT
      case CursorType.String => Types.VARCHAR
      case CursorType.Blob => Types.BLOB
      case _ => Types.NULL
    }

  private[this] def typeName(t: Int): String =
    t match {
      case Types.NULL => nullString
      case Types.INTEGER => "INTEGER"
      case Types.FLOAT => "FLOAT"
      case Types.VARCHAR => "TEXT"
      case Types.BLOB => "BLOB"
      case _ => ""
    }
}
