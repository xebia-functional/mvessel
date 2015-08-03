package com.fortysevendeg.mvessel.resultset

import java.io._
import java.math.BigDecimal
import java.net.URL
import java.sql.{ResultSet => SQLResultSet, _}
import java.util
import java.util.Calendar

import android.database.Cursor
import com.fortysevendeg.mvessel.data.{Blob, Clob}
import com.fortysevendeg.mvessel.logging.{AndroidLogWrapper, LogWrapper}
import com.fortysevendeg.mvessel.util.CursorUtils._
import com.fortysevendeg.mvessel.util.DateUtils
import com.fortysevendeg.mvessel.{WrapperNotSupported, javaNull, _}

import scala.util.{Failure, Success, Try}
import scala.{Array => SArray}

class ResultSet(
  val cursor: Cursor,
  val log: LogWrapper = new AndroidLogWrapper())
  extends SQLResultSet
  with NonEditableResultSet
  with WrapperNotSupported
  with DateUtils {

  private[this] var lastColumnRead: Option[Int] = None

  private[this] def moveOp(f: => Boolean): Boolean =
    f match {
      case true =>
        lastColumnRead = None
        true
      case false =>
        false
    }

  private[this] def getOp[T](index: Int)(f: Int => T): T = {
    lastColumnRead = Some(index)
    f(index)
  }

  private[this] def notClosed[T](f: => T) =
    if (cursor.isClosed) throw new SQLException("Cursor is closed")
    else Try(f) match {
      case Success(r) => r
      case Failure(e) => throw new SQLException(e)
    }

  private[this] def notNull[T](columnIndex: Int)(f: => T): Option[T] =
    getOp(columnIndex.index)(cursor.isNull) match {
      case true => None
      case false => Option(f)
    }

  private[this] def readBytes(columnIndex: Int): Option[SArray[Byte]] =
    notNull(columnIndex) {
      cursor.getTypeSafe(columnIndex.index) match {
        case t if t == Cursor.FIELD_TYPE_STRING => getOp(columnIndex.index)(cursor.getString).getBytes
        case _ => getOp(columnIndex.index)(cursor.getBlob)
      }
    }

  private[this] def readString(columnIndex: Int): Option[String] =
    notNull(columnIndex)(getOp(columnIndex.index)(cursor.getString))

  private[this] def readTime(columnIndex: Int): Option[Long] =
    notNull(columnIndex) {
      cursor.getTypeSafe(columnIndex.index) match {
        case (Cursor.FIELD_TYPE_INTEGER) => Some(getOp(columnIndex.index)(cursor.getLong))
        case _ => parseDate(getOp(columnIndex.index)(cursor.getString)) map (_.getTime)
      }
    } getOrElse None

  override def isBeforeFirst: Boolean = notClosed(cursor.isBeforeFirst)

  override def isAfterLast: Boolean = notClosed(cursor.isAfterLast)

  override def isFirst: Boolean = notClosed(cursor.isFirst)

  override def isLast: Boolean = notClosed(cursor.isLast)

  override def absolute(row: Int): Boolean = notClosed(moveOp {
    (cursor.getCount, row) match {
      case (0, _) => false
      case (_, 0) => cursor.moveToPosition(-1)
      case (c, r) if r > 0 =>
        if (c < r) false
        else cursor.moveToPosition(r.index)
      case (c, r) =>
        val abs = Math.abs(r)
        if (c < abs) false
        else cursor.moveToPosition(c - abs)
    }
  })

  override def relative(rows: Int): Boolean = notClosed(moveOp {
    (cursor.getCount, cursor.getPosition, rows) match {
      case (0, _, _) => false
      case (_, _, 0) => false
      case (c, p, r) if p + r > c => false
      case (c, p, r) if p + r < -1 => false
      case _ => cursor.move(rows)
    }
  })

  override def beforeFirst(): Unit = notClosed(moveOp(cursor.moveToPosition(-1)))

  override def afterLast(): Unit = notClosed(moveOp {
    if (cursor.moveToLast()) cursor.moveToNext()
    else false
  })

  override def next(): Boolean = notClosed(moveOp(cursor.moveToNext()))

  override def previous(): Boolean = notClosed(moveOp(cursor.moveToPrevious()))

  override def last(): Boolean = notClosed(moveOp(cursor.moveToLast()))

  override def first(): Boolean = notClosed(moveOp(cursor.moveToFirst()))

  override def getRow: Int = notClosed(cursor.getPosition + 1)

  override def findColumn(columnLabel: String): Int = notClosed {
    cursor.getColumnIndexOrThrow(columnLabel) + 1
  }

  override def close(): Unit = cursor.close()

  override def isClosed: Boolean = cursor.isClosed

  override def wasNull(): Boolean = lastColumnRead exists cursor.isNull

  override def getType: Int = SQLResultSet.TYPE_SCROLL_SENSITIVE

  override def getMetaData: ResultSetMetaData = new ResultSetMetaData(cursor, log)

  override def getTimestamp(columnIndex: Int): Timestamp = notClosed {
    readTime(columnIndex) map (new Timestamp(_)) getOrElse javaNull
  }

  override def getTimestamp(columnLabel: String): Timestamp =
    getTimestamp(findColumn(columnLabel))

  override def getTimestamp(columnIndex: Int, cal: Calendar): Timestamp = {
    log.w("getTimestamp(columnIndex: Int, cal: Calendar) not implemented correctly, Calendar is not used")
    getTimestamp(columnIndex)
  }

  override def getTimestamp(columnLabel: String, cal: Calendar): Timestamp = {
    log.w("getTimestamp(columnIndex: Int, cal: Calendar) not implemented correctly, Calendar is not used")
    getTimestamp(columnLabel)
  }

  override def getBinaryStream(columnIndex: Int): InputStream = notClosed {
    readBytes(columnIndex) map (new ByteArrayInputStream(_)) getOrElse javaNull
  }

  override def getBinaryStream(columnLabel: String): InputStream =
    getBinaryStream(findColumn(columnLabel))

  override def getNClob(columnIndex: Int): NClob =
    throw new SQLFeatureNotSupportedException

  override def getNClob(columnLabel: String): NClob =
    throw new SQLFeatureNotSupportedException

  override def getCharacterStream(columnIndex: Int): Reader = notClosed {
    readString(columnIndex) map (new StringReader(_)) getOrElse javaNull
  }

  override def getCharacterStream(columnLabel: String): Reader =
    getCharacterStream(findColumn(columnLabel))

  override def getDouble(columnIndex: Int): Double = notClosed {
    notNull(columnIndex)(getOp(columnIndex.index)(cursor.getDouble)) getOrElse 0
  }

  override def getDouble(columnLabel: String): Double =
    getDouble(findColumn(columnLabel))

  override def getArray(columnIndex: Int): Array =
    throw new SQLFeatureNotSupportedException

  override def getArray(columnLabel: String): Array =
    throw new SQLFeatureNotSupportedException

  override def getURL(columnIndex: Int): URL =
    throw new SQLFeatureNotSupportedException

  override def getURL(columnLabel: String): URL =
    throw new SQLFeatureNotSupportedException

  override def getRowId(columnIndex: Int): RowId =
    throw new SQLFeatureNotSupportedException

  override def getRowId(columnLabel: String): RowId =
    throw new SQLFeatureNotSupportedException

  override def getFloat(columnIndex: Int): Float = notClosed {
    notNull(columnIndex)(getOp(columnIndex.index)(cursor.getFloat)) getOrElse 0
  }

  override def getFloat(columnLabel: String): Float =
    getFloat(findColumn(columnLabel))

  override def getBigDecimal(columnIndex: Int, scale: Int): BigDecimal =
    throw new SQLFeatureNotSupportedException

  override def getBigDecimal(columnLabel: String, scale: Int): BigDecimal =
    throw new SQLFeatureNotSupportedException

  override def getBigDecimal(columnIndex: Int): BigDecimal =
    throw new SQLFeatureNotSupportedException

  override def getBigDecimal(columnLabel: String): BigDecimal =
    throw new SQLFeatureNotSupportedException

  override def getClob(columnIndex: Int): Clob = notClosed {
    readString(columnIndex) map (new Clob(_)) getOrElse javaNull
  }

  override def getClob(columnLabel: String): Clob =
    getClob(findColumn(columnLabel))

  override def getLong(columnIndex: Int): Long = notClosed {
    notNull(columnIndex)(getOp(columnIndex.index)(cursor.getLong)) getOrElse 0
  }

  override def getLong(columnLabel: String): Long =
    getLong(findColumn(columnLabel))

  override def getString(columnIndex: Int): String = notClosed {
    readString(columnIndex) getOrElse javaNull
  }

  override def getString(columnLabel: String): String =
    getString(findColumn(columnLabel))

  override def getNString(columnIndex: Int): String =
    throw new SQLFeatureNotSupportedException

  override def getNString(columnLabel: String): String =
    throw new SQLFeatureNotSupportedException

  override def getTime(columnIndex: Int): Time = notClosed {
    readTime(columnIndex) map (new Time(_)) getOrElse javaNull
  }

  override def getTime(columnLabel: String): Time =
    getTime(findColumn(columnLabel))

  override def getTime(columnIndex: Int, cal: Calendar): Time = {
    log.w("getTime(columnIndex: Int, cal: Calendar) not implemented correctly, Calendar is not used")
    getTime(columnIndex)
  }

  override def getTime(columnLabel: String, cal: Calendar): Time = {
    log.w("getTime(columnIndex: Int, cal: Calendar) not implemented correctly, Calendar is not used")
    getTime(columnLabel)
  }

  override def getByte(columnIndex: Int): Byte = notClosed {
    notNull(columnIndex)(getOp(columnIndex.index)(cursor.getShort).asInstanceOf[Byte]) getOrElse 0
  }

  override def getByte(columnLabel: String): Byte =
    getByte(findColumn(columnLabel))

  override def getBoolean(columnIndex: Int): Boolean = notClosed {
    notNull(columnIndex)(getOp(columnIndex.index)(cursor.getInt) != 0) getOrElse false
  }

  override def getBoolean(columnLabel: String): Boolean =
    getBoolean(findColumn(columnLabel))

  override def getAsciiStream(columnIndex: Int): InputStream =
    throw new SQLFeatureNotSupportedException

  override def getAsciiStream(columnLabel: String): InputStream =
    throw new SQLFeatureNotSupportedException

  override def getShort(columnIndex: Int): Short = notClosed {
    notNull(columnIndex)(getOp(columnIndex.index)(cursor.getShort)) getOrElse 0
  }

  override def getShort(columnLabel: String): Short =
    getShort(findColumn(columnLabel))

  override def getObject(columnIndex: Int): AnyRef = notClosed {
    notNull(columnIndex) {
      cursor.getTypeSafe(columnIndex.index) match {
        case Cursor.FIELD_TYPE_INTEGER => new java.lang.Integer(getOp(columnIndex.index)(cursor.getInt))
        case Cursor.FIELD_TYPE_FLOAT => new java.lang.Float(getOp(columnIndex.index)(cursor.getFloat))
        case Cursor.FIELD_TYPE_BLOB => new Blob(getOp(columnIndex.index)(cursor.getBlob))
        case _ => getOp(columnIndex.index)(cursor.getString)
      }
    } getOrElse javaNull
  }

  override def getObject(columnLabel: String): AnyRef =
    getObject(findColumn(columnLabel))

  override def getObject(columnIndex: Int, map: util.Map[String, Class[_]]): AnyRef =
    throw new SQLFeatureNotSupportedException

  override def getObject(columnLabel: String, map: util.Map[String, Class[_]]): AnyRef =
    throw new SQLFeatureNotSupportedException

  override def getObject[T](columnIndex: Int, `type`: Class[T]): T =
    getObject(columnIndex) match {
      case `javaNull` => throw new SQLException("Object is null")
      case o => o.asInstanceOf[T]
    }

  override def getObject[T](columnLabel: String, `type`: Class[T]): T =
    getObject(findColumn(columnLabel), `type`)

  override def getNCharacterStream(columnIndex: Int): Reader =
    throw new SQLFeatureNotSupportedException

  override def getNCharacterStream(columnLabel: String): Reader =
    throw new SQLFeatureNotSupportedException

  override def getRef(columnIndex: Int): Ref =
    throw new SQLFeatureNotSupportedException

  override def getRef(columnLabel: String): Ref =
    throw new SQLFeatureNotSupportedException

  override def getBlob(columnIndex: Int): Blob = notClosed {
    readBytes(columnIndex) map (new Blob(_)) getOrElse javaNull
  }

  override def getBlob(columnLabel: String): Blob =
    getBlob(findColumn(columnLabel))

  override def getDate(columnIndex: Int): Date = notClosed {
    readTime(columnIndex) map (new Date(_)) getOrElse javaNull
  }

  override def getDate(columnLabel: String): Date =
    getDate(findColumn(columnLabel))

  override def getDate(columnIndex: Int, cal: Calendar): Date = {
    log.w("getDate(columnIndex: Int, cal: Calendar) not implemented correctly, Calendar is not used")
    getDate(columnIndex)
  }

  override def getDate(columnLabel: String, cal: Calendar): Date = {
    log.w("getDate(columnIndex: Int, cal: Calendar) not implemented correctly, Calendar is not used")
    getDate(columnLabel)
  }

  override def getSQLXML(columnIndex: Int): SQLXML =
    throw new SQLFeatureNotSupportedException

  override def getSQLXML(columnLabel: String): SQLXML =
    throw new SQLFeatureNotSupportedException

  override def getUnicodeStream(columnIndex: Int): InputStream =
    throw new SQLFeatureNotSupportedException

  override def getUnicodeStream(columnLabel: String): InputStream =
    throw new SQLFeatureNotSupportedException

  override def getInt(columnIndex: Int): Int = notClosed {
    notNull(columnIndex)(getOp(columnIndex.index)(cursor.getInt)) getOrElse 0
  }

  override def getInt(columnLabel: String): Int =
    getInt(findColumn(columnLabel))

  override def getBytes(columnIndex: Int): SArray[Byte] = notClosed {
    readBytes(columnIndex) getOrElse javaNull
  }

  override def getBytes(columnLabel: String): SArray[Byte] =
    getBytes(findColumn(columnLabel))

  override def getHoldability: Int = notClosed(SQLResultSet.CLOSE_CURSORS_AT_COMMIT)

  override def clearWarnings(): Unit = log.notImplemented(Unit)

  override def getWarnings: SQLWarning = log.notImplemented(javaNull)

  override def getConcurrency: Int = notClosed(SQLResultSet.CONCUR_READ_ONLY)

  override def setFetchSize(rows: Int): Unit = notClosed(log.notImplemented(Unit))

  override def getFetchSize: Int = notClosed(log.notImplemented(0))

  override def setFetchDirection(direction: Int): Unit = notClosed(log.notImplemented(Unit))

  override def getFetchDirection: Int = notClosed(SQLResultSet.FETCH_FORWARD)

  override def getCursorName: String =
    throw new SQLFeatureNotSupportedException

  override def getStatement: Statement = javaNull
}

