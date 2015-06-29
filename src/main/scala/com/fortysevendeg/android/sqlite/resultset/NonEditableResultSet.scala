package com.fortysevendeg.android.sqlite.resultset

import java.io.{Reader, InputStream}
import java.sql._
import scala.{Array => SArray}

trait NonEditableResultSet extends ResultSet {

  override def updateString(columnIndex: Int, x: String): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateString(columnLabel: String, x: String): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateNString(columnIndex: Int, nString: String): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateNString(columnLabel: String, nString: String): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateTimestamp(columnIndex: Int, x: Timestamp): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateTimestamp(columnLabel: String, x: Timestamp): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateByte(columnIndex: Int, x: Byte): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateByte(columnLabel: String, x: Byte): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateBigDecimal(columnIndex: Int, x: java.math.BigDecimal): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateBigDecimal(columnLabel: String, x: java.math.BigDecimal): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateDouble(columnIndex: Int, x: Double): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateDouble(columnLabel: String, x: Double): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateDate(columnIndex: Int, x: Date): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateDate(columnLabel: String, x: Date): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateBoolean(columnIndex: Int, x: Boolean): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateBoolean(columnLabel: String, x: Boolean): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateNCharacterStream(columnIndex: Int, x: Reader, length: Long): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateNCharacterStream(columnLabel: String, reader: Reader, length: Long): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateNCharacterStream(columnIndex: Int, x: Reader): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateNCharacterStream(columnLabel: String, reader: Reader): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateNClob(columnIndex: Int, nClob: NClob): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateNClob(columnLabel: String, nClob: NClob): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateNClob(columnIndex: Int, reader: Reader, length: Long): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateNClob(columnLabel: String, reader: Reader, length: Long): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateNClob(columnIndex: Int, reader: Reader): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateNClob(columnLabel: String, reader: Reader): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateArray(columnIndex: Int, x: Array): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateArray(columnLabel: String, x: Array): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateBlob(columnIndex: Int, x: Blob): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateBlob(columnLabel: String, x: Blob): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateBlob(columnIndex: Int, inputStream: InputStream, length: Long): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateBlob(columnLabel: String, inputStream: InputStream, length: Long): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateBlob(columnIndex: Int, inputStream: InputStream): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateBlob(columnLabel: String, inputStream: InputStream): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateBinaryStream(columnIndex: Int, x: InputStream, length: Int): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateBinaryStream(columnLabel: String, x: InputStream, length: Int): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateBinaryStream(columnIndex: Int, x: InputStream, length: Long): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateBinaryStream(columnLabel: String, x: InputStream, length: Long): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateBinaryStream(columnIndex: Int, x: InputStream): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateBinaryStream(columnLabel: String, x: InputStream): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateFloat(columnIndex: Int, x: Float): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateFloat(columnLabel: String, x: Float): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateObject(columnIndex: Int, x: scala.Any, scaleOrLength: Int): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateObject(columnIndex: Int, x: scala.Any): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateObject(columnLabel: String, x: scala.Any, scaleOrLength: Int): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateObject(columnLabel: String, x: scala.Any): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateCharacterStream(columnIndex: Int, x: Reader, length: Int): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateCharacterStream(columnLabel: String, reader: Reader, length: Int): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateCharacterStream(columnIndex: Int, x: Reader, length: Long): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateCharacterStream(columnLabel: String, reader: Reader, length: Long): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateCharacterStream(columnIndex: Int, x: Reader): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateCharacterStream(columnLabel: String, reader: Reader): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateRowId(columnIndex: Int, x: RowId): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateRowId(columnLabel: String, x: RowId): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateRef(columnIndex: Int, x: Ref): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateRef(columnLabel: String, x: Ref): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateShort(columnIndex: Int, x: Short): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateShort(columnLabel: String, x: Short): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateInt(columnIndex: Int, x: Int): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateInt(columnLabel: String, x: Int): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateLong(columnIndex: Int, x: Long): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateLong(columnLabel: String, x: Long): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateClob(columnIndex: Int, x: Clob): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateClob(columnLabel: String, x: Clob): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateClob(columnIndex: Int, reader: Reader, length: Long): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateClob(columnLabel: String, reader: Reader, length: Long): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateClob(columnIndex: Int, reader: Reader): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateClob(columnLabel: String, reader: Reader): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateNull(columnIndex: Int): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateNull(columnLabel: String): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateTime(columnIndex: Int, x: Time): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateTime(columnLabel: String, x: Time): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateAsciiStream(columnIndex: Int, x: InputStream, length: Int): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateAsciiStream(columnLabel: String, x: InputStream, length: Int): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateAsciiStream(columnIndex: Int, x: InputStream, length: Long): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateAsciiStream(columnLabel: String, x: InputStream, length: Long): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateAsciiStream(columnIndex: Int, x: InputStream): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateAsciiStream(columnLabel: String, x: InputStream): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateBytes(columnIndex: Int, x: SArray[Byte]): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateBytes(columnLabel: String, x: SArray[Byte]): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateSQLXML(columnIndex: Int, xmlObject: SQLXML): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateSQLXML(columnLabel: String, xmlObject: SQLXML): Unit =
    throw new SQLFeatureNotSupportedException

  override def updateRow(): Unit =
    throw new SQLFeatureNotSupportedException

  override def insertRow(): Unit =
    throw new SQLFeatureNotSupportedException

  override def rowInserted(): Boolean =
    throw new SQLFeatureNotSupportedException

  override def refreshRow(): Unit =
    throw new SQLFeatureNotSupportedException

  override def deleteRow(): Unit =
    throw new SQLFeatureNotSupportedException

  override def rowUpdated(): Boolean =
    throw new SQLFeatureNotSupportedException

  override def rowDeleted(): Boolean =
    throw new SQLFeatureNotSupportedException

  override def moveToCurrentRow(): Unit =
    throw new SQLFeatureNotSupportedException

  override def moveToInsertRow(): Unit =
    throw new SQLFeatureNotSupportedException

  override def cancelRowUpdates(): Unit =
    throw new SQLFeatureNotSupportedException

}
