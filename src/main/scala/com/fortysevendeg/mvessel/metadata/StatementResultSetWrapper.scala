package com.fortysevendeg.mvessel.metadata

import java.io.{InputStream, Reader}
import java.math.BigDecimal
import java.net.URL
import java.sql.{ResultSet => SQLResultSet, ResultSetMetaData => SQLResultSetMetadata, Statement => SQLStatement, _}
import java.util
import java.util.Calendar

import scala.util.{Failure, Try}

/**
 * This class defines a ResultSet with a 1:1 relation with a Statement
 *
 * It allows us to guarantee that, if the ResultSet is closed, the Statement will be closed too
 *
 * @param statement the statement
 * @param resultSet the result set
 */
class StatementResultSetWrapper(
  statement: SQLStatement,
  resultSet: SQLResultSet) extends SQLResultSet {

  override def close(): Unit = {
    val maybeException = Try(resultSet.close()) match {
      case Failure(e) => Some(e)
      case _ => None
    }
    Try(statement.close())
    maybeException foreach (e => throw e)
  }

  override def isBeforeFirst: Boolean = resultSet.isBeforeFirst

  override def isAfterLast: Boolean = resultSet.isAfterLast

  override def isFirst: Boolean = resultSet.isFirst

  override def isLast: Boolean = resultSet.isLast

  override def absolute(row: Int): Boolean = resultSet.absolute(row)

  override def relative(rows: Int): Boolean = resultSet.relative(rows)

  override def beforeFirst(): Unit = resultSet.beforeFirst()

  override def afterLast(): Unit = resultSet.afterLast()

  override def next(): Boolean = resultSet.next()

  override def previous(): Boolean = resultSet.previous()

  override def last(): Boolean = resultSet.last()

  override def first(): Boolean = resultSet.first()

  override def getRow: Int = resultSet.getRow

  override def findColumn(columnLabel: String): Int = resultSet.findColumn(columnLabel)

  override def isClosed: Boolean = resultSet.isClosed

  override def wasNull(): Boolean = resultSet.wasNull()

  override def getType: Int = resultSet.getType

  override def getMetaData: SQLResultSetMetadata = resultSet.getMetaData

  override def getTimestamp(columnIndex: Int): Timestamp = resultSet.getTimestamp(columnIndex)

  override def getTimestamp(columnLabel: String): Timestamp = resultSet.getTimestamp(columnLabel)

  override def getTimestamp(columnIndex: Int, cal: Calendar): Timestamp = resultSet.getTimestamp(columnIndex, cal)

  override def getTimestamp(columnLabel: String, cal: Calendar): Timestamp = resultSet.getTimestamp(columnLabel, cal)

  override def getBinaryStream(columnIndex: Int): InputStream = resultSet.getBinaryStream(columnIndex)

  override def getBinaryStream(columnLabel: String): InputStream = resultSet.getBinaryStream(columnLabel)

  override def getNClob(columnIndex: Int): NClob = resultSet.getNClob(columnIndex)

  override def getNClob(columnLabel: String): NClob = resultSet.getNClob(columnLabel)

  override def getCharacterStream(columnIndex: Int): Reader = resultSet.getCharacterStream(columnIndex)

  override def getCharacterStream(columnLabel: String): Reader = resultSet.getCharacterStream(columnLabel)

  override def getDouble(columnIndex: Int): Double = resultSet.getDouble(columnIndex)

  override def getDouble(columnLabel: String): Double = resultSet.getDouble(columnLabel)

  override def getArray(columnIndex: Int): java.sql.Array = resultSet.getArray(columnIndex)

  override def getArray(columnLabel: String): java.sql.Array = resultSet.getArray(columnLabel)

  override def getURL(columnIndex: Int): URL = resultSet.getURL(columnIndex)

  override def getURL(columnLabel: String): URL = resultSet.getURL(columnLabel)

  override def getRowId(columnIndex: Int): RowId = resultSet.getRowId(columnIndex)

  override def getRowId(columnLabel: String): RowId = resultSet.getRowId(columnLabel)

  override def getFloat(columnIndex: Int): Float = resultSet.getFloat(columnIndex)

  override def getFloat(columnLabel: String): Float = resultSet.getFloat(columnLabel)

  override def getBigDecimal(columnIndex: Int, scale: Int): BigDecimal = resultSet.getBigDecimal(columnIndex, scale)

  override def getBigDecimal(columnLabel: String, scale: Int): BigDecimal = resultSet.getBigDecimal(columnLabel, scale)

  override def getBigDecimal(columnIndex: Int): BigDecimal = resultSet.getBigDecimal(columnIndex)

  override def getBigDecimal(columnLabel: String): BigDecimal = resultSet.getBigDecimal(columnLabel)

  override def getClob(columnIndex: Int): Clob = resultSet.getClob(columnIndex)

  override def getClob(columnLabel: String): Clob = resultSet.getClob(columnLabel)

  override def getLong(columnIndex: Int): Long = resultSet.getLong(columnIndex)

  override def getLong(columnLabel: String): Long = resultSet.getLong(columnLabel)

  override def getString(columnIndex: Int): String = resultSet.getString(columnIndex)

  override def getString(columnLabel: String): String = resultSet.getString(columnLabel)

  override def getNString(columnIndex: Int): String = resultSet.getNString(columnIndex)

  override def getNString(columnLabel: String): String = resultSet.getNString(columnLabel)

  override def getTime(columnIndex: Int): Time = resultSet.getTime(columnIndex)

  override def getTime(columnLabel: String): Time = resultSet.getTime(columnLabel)

  override def getTime(columnIndex: Int, cal: Calendar): Time = resultSet.getTime(columnIndex, cal)

  override def getTime(columnLabel: String, cal: Calendar): Time = resultSet.getTime(columnLabel, cal)

  override def getByte(columnIndex: Int): Byte = resultSet.getByte(columnIndex)

  override def getByte(columnLabel: String): Byte = resultSet.getByte(columnLabel)

  override def getBoolean(columnIndex: Int): Boolean = resultSet.getBoolean(columnIndex)

  override def getBoolean(columnLabel: String): Boolean = resultSet.getBoolean(columnLabel)

  override def getAsciiStream(columnIndex: Int): InputStream = resultSet.getAsciiStream(columnIndex)

  override def getAsciiStream(columnLabel: String): InputStream = resultSet.getAsciiStream(columnLabel)

  override def getShort(columnIndex: Int): Short = resultSet.getShort(columnIndex)

  override def getShort(columnLabel: String): Short = resultSet.getShort(columnLabel)

  override def getObject(columnIndex: Int): AnyRef = resultSet.getObject(columnIndex)

  override def getObject(columnLabel: String): AnyRef = resultSet.getObject(columnLabel)

  override def getObject(columnIndex: Int, map: util.Map[String, Class[_]]): AnyRef = resultSet.getObject(columnIndex, map)

  override def getObject(columnLabel: String, map: util.Map[String, Class[_]]): AnyRef = resultSet.getObject(columnLabel, map)

  override def getObject[T](columnIndex: Int, `type`: Class[T]): T = resultSet.getObject[T](columnIndex, `type`)

  override def getObject[T](columnLabel: String, `type`: Class[T]): T = resultSet.getObject[T](columnLabel, `type`)

  override def getNCharacterStream(columnIndex: Int): Reader = resultSet.getNCharacterStream(columnIndex)

  override def getNCharacterStream(columnLabel: String): Reader = resultSet.getNCharacterStream(columnLabel)

  override def getRef(columnIndex: Int): Ref = resultSet.getRef(columnIndex)

  override def getRef(columnLabel: String): Ref = resultSet.getRef(columnLabel)

  override def getBlob(columnIndex: Int): Blob = resultSet.getBlob(columnIndex)

  override def getBlob(columnLabel: String): Blob = resultSet.getBlob(columnLabel)

  override def getDate(columnIndex: Int): Date = resultSet.getDate(columnIndex)

  override def getDate(columnLabel: String): Date = resultSet.getDate(columnLabel)

  override def getDate(columnIndex: Int, cal: Calendar): Date = resultSet.getDate(columnIndex, cal)

  override def getDate(columnLabel: String, cal: Calendar): Date = resultSet.getDate(columnLabel, cal)

  override def getSQLXML(columnIndex: Int): SQLXML = resultSet.getSQLXML(columnIndex)

  override def getSQLXML(columnLabel: String): SQLXML = resultSet.getSQLXML(columnLabel)

  override def getUnicodeStream(columnIndex: Int): InputStream = resultSet.getUnicodeStream(columnIndex)

  override def getUnicodeStream(columnLabel: String): InputStream = resultSet.getUnicodeStream(columnLabel)

  override def getInt(columnIndex: Int): Int = resultSet.getInt(columnIndex)

  override def getInt(columnLabel: String): Int = resultSet.getInt(columnLabel)

  override def getBytes(columnIndex: Int): scala.Array[Byte] = resultSet.getBytes(columnIndex)

  override def getBytes(columnLabel: String): scala.Array[Byte] = resultSet.getBytes(columnLabel)

  override def getHoldability: Int = resultSet.getHoldability

  override def clearWarnings(): Unit = resultSet.clearWarnings()

  override def getWarnings: SQLWarning = resultSet.getWarnings

  override def getConcurrency: Int = resultSet.getConcurrency

  override def setFetchSize(rows: Int): Unit = resultSet.setFetchSize(rows)

  override def getFetchSize: Int = resultSet.getFetchSize

  override def setFetchDirection(direction: Int): Unit = resultSet.setFetchDirection(direction)

  override def getFetchDirection: Int = resultSet.getFetchDirection

  override def getCursorName: String = resultSet.getCursorName

  override def getStatement: SQLStatement = statement

  override def updateString(columnIndex: Int, x: String): Unit = resultSet.updateString(columnIndex, x)

  override def updateString(columnLabel: String, x: String): Unit = resultSet.updateString(columnLabel, x)

  override def updateNString(columnIndex: Int, nString: String): Unit = resultSet.updateNString(columnIndex, nString)

  override def updateNString(columnLabel: String, nString: String): Unit = resultSet.updateNString(columnLabel, nString)

  override def updateTimestamp(columnIndex: Int, x: Timestamp): Unit = resultSet.updateTimestamp(columnIndex, x)

  override def updateTimestamp(columnLabel: String, x: Timestamp): Unit = resultSet.updateTimestamp(columnLabel, x)

  override def updateByte(columnIndex: Int, x: Byte): Unit = resultSet.updateByte(columnIndex, x)

  override def updateByte(columnLabel: String, x: Byte): Unit = resultSet.updateByte(columnLabel, x)

  override def updateBigDecimal(columnIndex: Int, x: BigDecimal): Unit = resultSet.updateBigDecimal(columnIndex, x)

  override def updateBigDecimal(columnLabel: String, x: BigDecimal): Unit = resultSet.updateBigDecimal(columnLabel, x)

  override def updateDouble(columnIndex: Int, x: Double): Unit = resultSet.updateDouble(columnIndex, x)

  override def updateDouble(columnLabel: String, x: Double): Unit = resultSet.updateDouble(columnLabel, x)

  override def updateDate(columnIndex: Int, x: Date): Unit = resultSet.updateDate(columnIndex, x)

  override def updateDate(columnLabel: String, x: Date): Unit = resultSet.updateDate(columnLabel, x)

  override def updateBoolean(columnIndex: Int, x: Boolean): Unit = resultSet.updateBoolean(columnIndex, x)

  override def updateBoolean(columnLabel: String, x: Boolean): Unit = resultSet.updateBoolean(columnLabel, x)

  override def updateNCharacterStream(columnIndex: Int, x: Reader, length: Long): Unit = resultSet.updateNCharacterStream(columnIndex, x, length)

  override def updateNCharacterStream(columnLabel: String, reader: Reader, length: Long): Unit = resultSet.updateNCharacterStream(columnLabel, reader, length)

  override def updateNCharacterStream(columnIndex: Int, x: Reader): Unit = resultSet.updateNCharacterStream(columnIndex, x)

  override def updateNCharacterStream(columnLabel: String, reader: Reader): Unit = resultSet.updateNCharacterStream(columnLabel, reader)

  override def updateNClob(columnIndex: Int, nClob: NClob): Unit = resultSet.updateNClob(columnIndex, nClob)

  override def updateNClob(columnLabel: String, nClob: NClob): Unit = resultSet.updateNClob(columnLabel, nClob)

  override def updateNClob(columnIndex: Int, reader: Reader, length: Long): Unit = resultSet.updateNClob(columnIndex, reader, length)

  override def updateNClob(columnLabel: String, reader: Reader, length: Long): Unit = resultSet.updateNClob(columnLabel, reader, length)

  override def updateNClob(columnIndex: Int, reader: Reader): Unit = resultSet.updateNClob(columnIndex, reader)

  override def updateNClob(columnLabel: String, reader: Reader): Unit = resultSet.updateNClob(columnLabel, reader)

  override def updateArray(columnIndex: Int, x: Array): Unit = resultSet.updateArray(columnIndex, x)

  override def updateArray(columnLabel: String, x: Array): Unit = resultSet.updateArray(columnLabel, x)

  override def updateBlob(columnIndex: Int, x: Blob): Unit = resultSet.updateBlob(columnIndex, x)

  override def updateBlob(columnLabel: String, x: Blob): Unit = resultSet.updateBlob(columnLabel, x)

  override def updateBlob(columnIndex: Int, inputStream: InputStream, length: Long): Unit = resultSet.updateBlob(columnIndex, inputStream, length)

  override def updateBlob(columnLabel: String, inputStream: InputStream, length: Long): Unit = resultSet.updateBlob(columnLabel, inputStream, length)

  override def updateBlob(columnIndex: Int, inputStream: InputStream): Unit = resultSet.updateBlob(columnIndex, inputStream)

  override def updateBlob(columnLabel: String, inputStream: InputStream): Unit = resultSet.updateBlob(columnLabel, inputStream)

  override def updateRow(): Unit = resultSet.updateRow()

  override def insertRow(): Unit = resultSet.insertRow()

  override def updateBinaryStream(columnIndex: Int, x: InputStream, length: Int): Unit = resultSet.updateBinaryStream(columnIndex, x, length)

  override def updateBinaryStream(columnLabel: String, x: InputStream, length: Int): Unit = resultSet.updateBinaryStream(columnLabel, x, length)

  override def updateBinaryStream(columnIndex: Int, x: InputStream, length: Long): Unit = resultSet.updateBinaryStream(columnIndex, x, length)

  override def updateBinaryStream(columnLabel: String, x: InputStream, length: Long): Unit = resultSet.updateBinaryStream(columnLabel, x, length)

  override def updateBinaryStream(columnIndex: Int, x: InputStream): Unit = resultSet.updateBinaryStream(columnIndex, x)

  override def updateBinaryStream(columnLabel: String, x: InputStream): Unit = resultSet.updateBinaryStream(columnLabel, x)

  override def updateRowId(columnIndex: Int, x: RowId): Unit = resultSet.updateRowId(columnIndex, x)

  override def updateRowId(columnLabel: String, x: RowId): Unit = resultSet.updateRowId(columnLabel, x)

  override def moveToInsertRow(): Unit = resultSet.moveToInsertRow()

  override def rowInserted(): Boolean = resultSet.rowInserted()

  override def updateFloat(columnIndex: Int, x: Float): Unit = resultSet.updateFloat(columnIndex, x)

  override def updateFloat(columnLabel: String, x: Float): Unit = resultSet.updateFloat(columnLabel, x)

  override def refreshRow(): Unit = resultSet.refreshRow()

  override def deleteRow(): Unit = resultSet.deleteRow()

  override def updateObject(columnIndex: Int, x: scala.Any, scaleOrLength: Int): Unit = resultSet.updateObject(columnIndex, x, scaleOrLength)

  override def updateObject(columnIndex: Int, x: scala.Any): Unit = resultSet.updateObject(columnIndex, x)

  override def updateObject(columnLabel: String, x: scala.Any, scaleOrLength: Int): Unit = resultSet.updateObject(columnLabel, x, scaleOrLength)

  override def updateObject(columnLabel: String, x: scala.Any): Unit = resultSet.updateObject(columnLabel, x)

  override def updateCharacterStream(columnIndex: Int, x: Reader, length: Int): Unit = resultSet.updateCharacterStream(columnIndex, x, length)

  override def updateCharacterStream(columnLabel: String, reader: Reader, length: Int): Unit = resultSet.updateCharacterStream(columnLabel, reader, length)

  override def updateCharacterStream(columnIndex: Int, x: Reader, length: Long): Unit = resultSet.updateCharacterStream(columnIndex, x, length)

  override def updateCharacterStream(columnLabel: String, reader: Reader, length: Long): Unit = resultSet.updateCharacterStream(columnLabel, reader, length)

  override def updateCharacterStream(columnIndex: Int, x: Reader): Unit = resultSet.updateCharacterStream(columnIndex, x)

  override def updateCharacterStream(columnLabel: String, reader: Reader): Unit = resultSet.updateCharacterStream(columnLabel, reader)

  override def updateRef(columnIndex: Int, x: Ref): Unit = resultSet.updateRef(columnIndex, x)

  override def updateRef(columnLabel: String, x: Ref): Unit = resultSet.updateRef(columnLabel, x)

  override def updateShort(columnIndex: Int, x: Short): Unit = resultSet.updateShort(columnIndex, x)

  override def updateShort(columnLabel: String, x: Short): Unit = resultSet.updateShort(columnLabel, x)

  override def updateInt(columnIndex: Int, x: Int): Unit = resultSet.updateInt(columnIndex, x)

  override def updateInt(columnLabel: String, x: Int): Unit = resultSet.updateInt(columnLabel, x)

  override def rowUpdated(): Boolean = resultSet.rowUpdated()

  override def updateLong(columnIndex: Int, x: Long): Unit = resultSet.updateLong(columnIndex, x)

  override def updateLong(columnLabel: String, x: Long): Unit = resultSet.updateLong(columnLabel, x)

  override def moveToCurrentRow(): Unit = resultSet.moveToCurrentRow()

  override def updateClob(columnIndex: Int, x: Clob): Unit = resultSet.updateClob(columnIndex, x)

  override def updateClob(columnLabel: String, x: Clob): Unit = resultSet.updateClob(columnLabel, x)

  override def updateClob(columnIndex: Int, reader: Reader, length: Long): Unit = resultSet.updateClob(columnIndex, reader, length)

  override def updateClob(columnLabel: String, reader: Reader, length: Long): Unit = resultSet.updateClob(columnLabel, reader, length)

  override def updateClob(columnIndex: Int, reader: Reader): Unit = resultSet.updateClob(columnIndex, reader)

  override def updateClob(columnLabel: String, reader: Reader): Unit = resultSet.updateClob(columnLabel, reader)

  override def updateNull(columnIndex: Int): Unit = resultSet.updateNull(columnIndex)

  override def updateNull(columnLabel: String): Unit = resultSet.updateNull(columnLabel)

  override def cancelRowUpdates(): Unit = resultSet.cancelRowUpdates()

  override def updateTime(columnIndex: Int, x: Time): Unit = resultSet.updateTime(columnIndex, x)

  override def updateTime(columnLabel: String, x: Time): Unit = resultSet.updateTime(columnLabel, x)

  override def updateAsciiStream(columnIndex: Int, x: InputStream, length: Int): Unit = resultSet.updateAsciiStream(columnIndex, x, length)

  override def updateAsciiStream(columnLabel: String, x: InputStream, length: Int): Unit = resultSet.updateAsciiStream(columnLabel, x, length)

  override def updateAsciiStream(columnIndex: Int, x: InputStream, length: Long): Unit = resultSet.updateAsciiStream(columnIndex, x, length)

  override def updateAsciiStream(columnLabel: String, x: InputStream, length: Long): Unit = resultSet.updateAsciiStream(columnLabel, x, length)

  override def updateAsciiStream(columnIndex: Int, x: InputStream): Unit = resultSet.updateAsciiStream(columnIndex, x)

  override def updateAsciiStream(columnLabel: String, x: InputStream): Unit = resultSet.updateAsciiStream(columnLabel, x)

  override def rowDeleted(): Boolean = resultSet.rowDeleted()

  override def updateBytes(columnIndex: Int, x: scala.Array[Byte]): Unit = resultSet.updateBytes(columnIndex, x)

  override def updateBytes(columnLabel: String, x: scala.Array[Byte]): Unit = resultSet.updateBytes(columnLabel, x)

  override def updateSQLXML(columnIndex: Int, xmlObject: SQLXML): Unit = resultSet.updateSQLXML(columnIndex, xmlObject)

  override def updateSQLXML(columnLabel: String, xmlObject: SQLXML): Unit = resultSet.updateSQLXML(columnLabel, xmlObject)

  override def unwrap[T](iface: Class[T]): T = resultSet.unwrap(iface)

  override def isWrapperFor(iface: Class[_]): Boolean = resultSet.isWrapperFor(iface)
}
