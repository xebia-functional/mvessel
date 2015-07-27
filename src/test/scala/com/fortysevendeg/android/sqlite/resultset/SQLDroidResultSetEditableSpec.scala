package com.fortysevendeg.android.sqlite.resultset

import java.io.{ByteArrayInputStream, StringReader}
import java.sql._

import com.fortysevendeg.android.sqlite._

class SQLDroidResultSetEditableSpec
  extends SQLDroidResultSetSpecification {

  "NonEditableResultSet" should {

    "throws an SQLFeatureNotSupportedException in updateString(columnIndex: Int, x: String)" in new WithCursorMocked {
      sqlDroid.updateString(0, "") must throwA[SQLFeatureNotSupportedException]
    }

    "throws an SQLFeatureNotSupportedException in updateString(columnLabel: String, x: String)" in new WithCursorMocked {
      sqlDroid.updateString("", "") must throwA[SQLFeatureNotSupportedException]
    }

    "throws an SQLFeatureNotSupportedException in updateNString(columnIndex: Int, x: String)" in new WithCursorMocked {
      sqlDroid.updateNString(0, "") must throwA[SQLFeatureNotSupportedException]
    }

    "throws an SQLFeatureNotSupportedException in updateNString(columnLabel: String, x: String)" in new WithCursorMocked {
      sqlDroid.updateNString("", "") must throwA[SQLFeatureNotSupportedException]
    }

    "throws an SQLFeatureNotSupportedException in updateTimestamp(columnIndex: Int, x: Timestamp)" in new WithCursorMocked {
      sqlDroid.updateTimestamp(0, new Timestamp(0)) must throwA[SQLFeatureNotSupportedException]
    }

    "throws an SQLFeatureNotSupportedException in updateTimestamp(columnLabel: String, x: Timestamp)" in new WithCursorMocked {
      sqlDroid.updateTimestamp("", new Timestamp(0)) must throwA[SQLFeatureNotSupportedException]
    }

    "throws an SQLFeatureNotSupportedException in updateByte(columnIndex: Int, x: Byte)" in new WithCursorMocked {
      sqlDroid.updateByte(0, 0.toByte) must throwA[SQLFeatureNotSupportedException]
    }

    "throws an SQLFeatureNotSupportedException in updateByte(columnLabel: String, x: Byte)" in new WithCursorMocked {
      sqlDroid.updateByte("", 0.toByte) must throwA[SQLFeatureNotSupportedException]
    }

    "throws an SQLFeatureNotSupportedException in updateBigDecimal(columnIndex: Int, x: BigDecimal)" in new WithCursorMocked {
      sqlDroid.updateBigDecimal(0, new java.math.BigDecimal(0)) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateBigDecimal(columnLabel: String, x: BigDecimal)" in new WithCursorMocked {
      sqlDroid.updateBigDecimal("", new java.math.BigDecimal(0)) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateDouble(columnIndex: Int, x: Double)" in new WithCursorMocked {
      sqlDroid.updateDouble(0, 1.5) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateDouble(columnLabel: String, x: Double)" in new WithCursorMocked {
      sqlDroid.updateDouble("", 1.5) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateDate(columnIndex: Int, x: Date)" in new WithCursorMocked {
      sqlDroid.updateDate(0, new java.sql.Date(0)) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateDate(columnLabel: String, x: Date)" in new WithCursorMocked {
      sqlDroid.updateDate("", new java.sql.Date(0)) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateBoolean(columnIndex: Int, x: Boolean)" in new WithCursorMocked {
      sqlDroid.updateBoolean(0, false) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateBoolean(columnLabel: String, x: Boolean)" in new WithCursorMocked {
      sqlDroid.updateBoolean("", false) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateNCharacterStream(columnIndex: Int, x: Reader, length: Long)" in new WithCursorMocked {
      sqlDroid.updateNCharacterStream(0, new StringReader(""), 0) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateNCharacterStream(columnLabel: String, reader: Reader, length: Long)" in new WithCursorMocked {
      sqlDroid.updateNCharacterStream("", new StringReader(""), 0) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateNCharacterStream(columnIndex: Int, x: Reader)" in new WithCursorMocked {
      sqlDroid.updateNCharacterStream(0, new StringReader("")) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateNCharacterStream(columnLabel: String, reader: Reader)" in new WithCursorMocked {
      sqlDroid.updateNCharacterStream("", new StringReader("")) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateNClob(columnIndex: Int, nClob: NClob)" in new WithCursorMocked {
      sqlDroid.updateNClob(0, javaNull.asInstanceOf[NClob]) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateNClob(columnLabel: String, nClob: NClob)" in new WithCursorMocked {
      sqlDroid.updateNClob("", javaNull.asInstanceOf[NClob]) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateNClob(columnIndex: Int, reader: Reader, length: Long)" in new WithCursorMocked {
      sqlDroid.updateNClob(0, new StringReader(""), 0) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateNClob(columnLabel: String, reader: Reader, length: Long)" in new WithCursorMocked {
      sqlDroid.updateNClob("", new StringReader(""), 0) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateNClob(columnIndex: Int, reader: Reader)" in new WithCursorMocked {
      sqlDroid.updateNClob(0, new StringReader("")) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateNClob(columnLabel: String, reader: Reader)" in new WithCursorMocked {
      sqlDroid.updateNClob("", new StringReader("")) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateArray(columnIndex: Int, x: Array)" in new WithCursorMocked {
      sqlDroid.updateArray(0, javaNull.asInstanceOf[java.sql.Array]) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateArray(columnLabel: String, x: Array)" in new WithCursorMocked {
      sqlDroid.updateArray("", javaNull.asInstanceOf[java.sql.Array]) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateBlob(columnIndex: Int, x: Blob)" in new WithCursorMocked {
      sqlDroid.updateBlob(0, javaNull.asInstanceOf[Blob]) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateBlob(columnLabel: String, x: Blob)" in new WithCursorMocked {
      sqlDroid.updateBlob("", javaNull.asInstanceOf[Blob]) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateBlob(columnIndex: Int, inputStream: InputStream, length: Long)" in new WithCursorMocked {
      sqlDroid.updateBlob(0, new ByteArrayInputStream(scala.Array[Byte](), 0, 0), 0) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateBlob(columnLabel: String, inputStream: InputStream, length: Long)" in new WithCursorMocked {
      sqlDroid.updateBlob("", new ByteArrayInputStream(scala.Array[Byte](), 0, 0), 0) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateBlob(columnIndex: Int, inputStream: InputStream)" in new WithCursorMocked {
      sqlDroid.updateBlob(0, new ByteArrayInputStream(scala.Array[Byte](), 0, 0)) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateBlob(columnLabel: String, inputStream: InputStream)" in new WithCursorMocked {
      sqlDroid.updateBlob("", new ByteArrayInputStream(scala.Array[Byte](), 0, 0)) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateBinaryStream(columnIndex: Int, x: InputStream, length: Int)" in new WithCursorMocked {
      sqlDroid.updateBinaryStream(0, new ByteArrayInputStream(scala.Array[Byte](), 0, 0), 0) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateBinaryStream(columnLabel: String, x: InputStream, length: Int)" in new WithCursorMocked {
      sqlDroid.updateBinaryStream("", new ByteArrayInputStream(scala.Array[Byte](), 0, 0), 0) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateBinaryStream(columnIndex: Int, x: InputStream, length: Long)" in new WithCursorMocked {
      sqlDroid.updateBinaryStream(0, new ByteArrayInputStream(scala.Array[Byte](), 0, 0), 0.toLong) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateBinaryStream(columnLabel: String, x: InputStream, length: Long)" in new WithCursorMocked {
      sqlDroid.updateBinaryStream("", new ByteArrayInputStream(scala.Array[Byte](), 0, 0), 0.toLong) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateBinaryStream(columnIndex: Int, x: InputStream)" in new WithCursorMocked {
      sqlDroid.updateBinaryStream(0, new ByteArrayInputStream(scala.Array[Byte](), 0, 0)) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateBinaryStream(columnLabel: String, x: InputStream)" in new WithCursorMocked {
      sqlDroid.updateBinaryStream("", new ByteArrayInputStream(scala.Array[Byte](), 0, 0)) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateFloat(columnIndex: Int, x: Float)" in new WithCursorMocked {
      sqlDroid.updateFloat(0, 0.toFloat) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateFloat(columnLabel: String, x: Float)" in new WithCursorMocked {
      sqlDroid.updateFloat("", 0.toFloat) must throwA[SQLFeatureNotSupportedException]
    }

    "throws an SQLFeatureNotSupportedException in updateObject(columnIndex: Int, x: scala.Any, scaleOrLength: Int)" in new WithCursorMocked {
      sqlDroid.updateObject(0, javaNull, 0) must throwA[SQLFeatureNotSupportedException]
    }

    "throws an SQLFeatureNotSupportedException in updateObject(columnIndex: Int, x: scala.Any)" in new WithCursorMocked {
      sqlDroid.updateObject(0, javaNull) must throwA[SQLFeatureNotSupportedException]
    }

    "throws an SQLFeatureNotSupportedException in updateObject(columnLabel: String, x: scala.Any)" in new WithCursorMocked {
      sqlDroid.updateObject("", javaNull) must throwA[SQLFeatureNotSupportedException]
    }

    "throws an SQLFeatureNotSupportedException in updateObject(columnLabel: String, x: scala.Any, scaleOrLength: Int)" in new WithCursorMocked {
      sqlDroid.updateObject("", javaNull, 0) must throwA[SQLFeatureNotSupportedException]
    }

    "throws an SQLFeatureNotSupportedException in updateCharacterStream(columnIndex: Int, x: Reader, length: Int)" in new WithCursorMocked {
      sqlDroid.updateCharacterStream(0, new StringReader(""), 0) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateCharacterStream(columnLabel: String, reader: Reader, length: Int)" in new WithCursorMocked {
      sqlDroid.updateCharacterStream("", new StringReader(""), 0) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateCharacterStream(columnIndex: Int, x: Reader, length: Long)" in new WithCursorMocked {
      sqlDroid.updateCharacterStream(0, new StringReader(""), 0.toLong) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateCharacterStream(columnLabel: String, reader: Reader, length: Long)" in new WithCursorMocked {
      sqlDroid.updateCharacterStream("", new StringReader(""), 0.toLong) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateCharacterStream(columnIndex: Int, x: Reader)" in new WithCursorMocked {
      sqlDroid.updateCharacterStream(0, new StringReader("")) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateCharacterStream(columnLabel: String, reader: Reader)" in new WithCursorMocked {
      sqlDroid.updateCharacterStream("", new StringReader("")) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateRowId(columnIndex: Int, x: RowId)" in new WithCursorMocked {
      sqlDroid.updateRowId(0, javaNull.asInstanceOf[RowId]) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateRowId(columnLabel: String, x: RowId)" in new WithCursorMocked {
      sqlDroid.updateRowId("", javaNull.asInstanceOf[RowId]) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateRef(columnIndex: Int, x: Ref)" in new WithCursorMocked {
      sqlDroid.updateRef(0, javaNull.asInstanceOf[Ref]) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateRef(columnLabel: String, x: Ref)" in new WithCursorMocked {
      sqlDroid.updateRef("", javaNull.asInstanceOf[Ref]) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateShort(columnIndex: Int, x: Short)" in new WithCursorMocked {
      sqlDroid.updateShort(0, 0.toShort) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateShort(columnLabel: String, x: Short)" in new WithCursorMocked {
      sqlDroid.updateShort("", 0.toShort) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateInt(columnIndex: Int, x: Int)" in new WithCursorMocked {
      sqlDroid.updateInt(0, 0) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateInt(columnLabel: String, x: Int)" in new WithCursorMocked {
      sqlDroid.updateInt("", 0) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateLong(columnIndex: Int, x: Long)" in new WithCursorMocked {
      sqlDroid.updateLong(0, 0) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateLong(columnLabel: String, x: Long)" in new WithCursorMocked {
      sqlDroid.updateLong("", 0) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateClob(columnIndex: Int, x: Clob)" in new WithCursorMocked {
      sqlDroid.updateClob(0, javaNull.asInstanceOf[Clob]) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateClob(columnLabel: String, x: Clob)" in new WithCursorMocked {
      sqlDroid.updateClob("", javaNull.asInstanceOf[Clob]) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateClob(columnIndex: Int, reader: Reader, length: Long)" in new WithCursorMocked {
      sqlDroid.updateClob(0, new StringReader(""), 0) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateClob(columnLabel: String, reader: Reader, length: Long)" in new WithCursorMocked {
      sqlDroid.updateClob("", new StringReader(""), 0) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateClob(columnIndex: Int, reader: Reader)" in new WithCursorMocked {
      sqlDroid.updateClob(0, new StringReader("")) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateClob(columnLabel: String, reader: Reader)" in new WithCursorMocked {
      sqlDroid.updateClob("", new StringReader("")) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateNull(columnIndex: Int)" in new WithCursorMocked {
      sqlDroid.updateNull(0) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateNull(columnLabel: String)" in new WithCursorMocked {
      sqlDroid.updateNull("") must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateTime(columnIndex: Int, x: Time)" in new WithCursorMocked {
      sqlDroid.updateTime(0, new java.sql.Time(0)) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateTime(columnLabel: String, x: Time)" in new WithCursorMocked {
      sqlDroid.updateTime("", new java.sql.Time(0)) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateAsciiStream(columnIndex: Int, x: InputStream, length: Int)" in new WithCursorMocked {
      sqlDroid.updateAsciiStream(0, new ByteArrayInputStream(scala.Array[Byte](), 0, 0), 0) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateAsciiStream(columnLabel: String, x: InputStream, length: Int)" in new WithCursorMocked {
      sqlDroid.updateAsciiStream("", new ByteArrayInputStream(scala.Array[Byte](), 0, 0), 0) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateAsciiStream(columnIndex: Int, x: InputStream, length: Long)" in new WithCursorMocked {
      sqlDroid.updateAsciiStream(0, new ByteArrayInputStream(scala.Array[Byte](), 0, 0), 0.toLong) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateAsciiStream(columnLabel: String, x: InputStream, length: Long)" in new WithCursorMocked {
      sqlDroid.updateAsciiStream("", new ByteArrayInputStream(scala.Array[Byte](), 0, 0), 0.toLong) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateAsciiStream(columnIndex: Int, x: InputStream)" in new WithCursorMocked {
      sqlDroid.updateAsciiStream(0, new ByteArrayInputStream(scala.Array[Byte](), 0, 0)) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateAsciiStream(columnLabel: String, x: InputStream)" in new WithCursorMocked {
      sqlDroid.updateAsciiStream("", new ByteArrayInputStream(scala.Array[Byte](), 0, 0)) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateBytes(columnIndex: Int, x: SArray[Byte])" in new WithCursorMocked {
      sqlDroid.updateBytes(0, scala.Array[Byte]()) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateBytes(columnLabel: String, x: SArray[Byte])" in new WithCursorMocked {
      sqlDroid.updateBytes("", scala.Array[Byte]()) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateSQLXML(columnIndex: Int, xmlObject: SQLXML)" in new WithCursorMocked {
      sqlDroid.updateSQLXML(0, javaNull.asInstanceOf[SQLXML]) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateSQLXML(columnLabel: String, xmlObject: SQLXML)" in new WithCursorMocked {
      sqlDroid.updateSQLXML("", javaNull.asInstanceOf[SQLXML]) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateRow()" in new WithCursorMocked {
      sqlDroid.updateRow() must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in insertRow()" in new WithCursorMocked {
      sqlDroid.insertRow() must throwA[SQLFeatureNotSupportedException]
    }

    "throws an SQLFeatureNotSupportedException in rowInserted()" in new WithCursorMocked {
      sqlDroid.rowInserted() must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in refreshRow()" in new WithCursorMocked {
      sqlDroid.refreshRow() must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in deleteRow()" in new WithCursorMocked {
      sqlDroid.deleteRow() must throwA[SQLFeatureNotSupportedException]
    }

    "throws an SQLFeatureNotSupportedException in rowUpdated()" in new WithCursorMocked {
      sqlDroid.rowUpdated() must throwA[SQLFeatureNotSupportedException]
    }
    "throws an SQLFeatureNotSupportedException in rowDeleted()" in new WithCursorMocked {
      sqlDroid.rowDeleted() must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in moveToCurrentRow()" in new WithCursorMocked {
      sqlDroid.moveToCurrentRow() must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in moveToInsertRow()" in new WithCursorMocked {
      sqlDroid.moveToInsertRow() must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in cancelRowUpdates()" in new WithCursorMocked {
      sqlDroid.cancelRowUpdates() must throwA[SQLFeatureNotSupportedException]
    }


  }
}
