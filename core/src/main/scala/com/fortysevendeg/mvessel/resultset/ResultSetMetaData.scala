package com.fortysevendeg.mvessel.resultset

import java.sql.{ResultSetMetaData => SQLResultSetMetaData, Types}

import android.database.Cursor
import com.fortysevendeg.mvessel.logging.{AndroidLogWrapper, LogWrapper}
import com.fortysevendeg.mvessel.util.CursorUtils
import CursorUtils._
import com.fortysevendeg.mvessel.WrapperNotSupported

class ResultSetMetaData(
  cursor: Cursor,
  log: LogWrapper = new AndroidLogWrapper)
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
        val nativeType = cursor.getTypeSafe(column - 1)
        maybeInt map cursor.moveToPosition
        fromNativeType(nativeType)
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

  private[this] def prepareCursor: (Option[Cursor], Option[Int]) =
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

  private[this] def fromNativeType(nativeType: Int): Int =
    nativeType match {
      case Cursor.FIELD_TYPE_NULL => Types.NULL
      case Cursor.FIELD_TYPE_INTEGER => Types.INTEGER
      case Cursor.FIELD_TYPE_FLOAT => Types.FLOAT
      case Cursor.FIELD_TYPE_STRING => Types.VARCHAR
      case Cursor.FIELD_TYPE_BLOB => Types.BLOB
      case _ => Types.NULL
    }

  private[this] def  typeName(t: Int): String =
    t match {
      case Types.NULL => "NULL"
      case Types.INTEGER => "INTEGER"
      case Types.FLOAT => "FLOAT"
      case Types.VARCHAR => "TEXT"
      case Types.BLOB => "BLOB"
      case _ => ""
    }
}
