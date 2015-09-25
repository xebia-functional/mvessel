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

import java.io.{ByteArrayInputStream, StringReader}
import java.sql._

import com.fortysevendeg.mvessel._

class ResultSetEditableSpec
  extends ResultSetSpecification {

  "NonEditableResultSet" should {

    "throws an SQLFeatureNotSupportedException in updateString(columnIndex: Int, x: String)" in new WithCursorMocked {
      resultSet.updateString(0, "") must throwA[SQLFeatureNotSupportedException]
    }

    "throws an SQLFeatureNotSupportedException in updateString(columnLabel: String, x: String)" in new WithCursorMocked {
      resultSet.updateString("", "") must throwA[SQLFeatureNotSupportedException]
    }

    "throws an SQLFeatureNotSupportedException in updateNString(columnIndex: Int, x: String)" in new WithCursorMocked {
      resultSet.updateNString(0, "") must throwA[SQLFeatureNotSupportedException]
    }

    "throws an SQLFeatureNotSupportedException in updateNString(columnLabel: String, x: String)" in new WithCursorMocked {
      resultSet.updateNString("", "") must throwA[SQLFeatureNotSupportedException]
    }

    "throws an SQLFeatureNotSupportedException in updateTimestamp(columnIndex: Int, x: Timestamp)" in new WithCursorMocked {
      resultSet.updateTimestamp(0, new Timestamp(0)) must throwA[SQLFeatureNotSupportedException]
    }

    "throws an SQLFeatureNotSupportedException in updateTimestamp(columnLabel: String, x: Timestamp)" in new WithCursorMocked {
      resultSet.updateTimestamp("", new Timestamp(0)) must throwA[SQLFeatureNotSupportedException]
    }

    "throws an SQLFeatureNotSupportedException in updateByte(columnIndex: Int, x: Byte)" in new WithCursorMocked {
      resultSet.updateByte(0, 0.toByte) must throwA[SQLFeatureNotSupportedException]
    }

    "throws an SQLFeatureNotSupportedException in updateByte(columnLabel: String, x: Byte)" in new WithCursorMocked {
      resultSet.updateByte("", 0.toByte) must throwA[SQLFeatureNotSupportedException]
    }

    "throws an SQLFeatureNotSupportedException in updateBigDecimal(columnIndex: Int, x: BigDecimal)" in new WithCursorMocked {
      resultSet.updateBigDecimal(0, new java.math.BigDecimal(0)) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateBigDecimal(columnLabel: String, x: BigDecimal)" in new WithCursorMocked {
      resultSet.updateBigDecimal("", new java.math.BigDecimal(0)) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateDouble(columnIndex: Int, x: Double)" in new WithCursorMocked {
      resultSet.updateDouble(0, 1.5) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateDouble(columnLabel: String, x: Double)" in new WithCursorMocked {
      resultSet.updateDouble("", 1.5) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateDate(columnIndex: Int, x: Date)" in new WithCursorMocked {
      resultSet.updateDate(0, new java.sql.Date(0)) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateDate(columnLabel: String, x: Date)" in new WithCursorMocked {
      resultSet.updateDate("", new java.sql.Date(0)) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateBoolean(columnIndex: Int, x: Boolean)" in new WithCursorMocked {
      resultSet.updateBoolean(0, false) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateBoolean(columnLabel: String, x: Boolean)" in new WithCursorMocked {
      resultSet.updateBoolean("", false) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateNCharacterStream(columnIndex: Int, x: Reader, length: Long)" in new WithCursorMocked {
      resultSet.updateNCharacterStream(0, new StringReader(""), 0) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateNCharacterStream(columnLabel: String, reader: Reader, length: Long)" in new WithCursorMocked {
      resultSet.updateNCharacterStream("", new StringReader(""), 0) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateNCharacterStream(columnIndex: Int, x: Reader)" in new WithCursorMocked {
      resultSet.updateNCharacterStream(0, new StringReader("")) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateNCharacterStream(columnLabel: String, reader: Reader)" in new WithCursorMocked {
      resultSet.updateNCharacterStream("", new StringReader("")) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateNClob(columnIndex: Int, nClob: NClob)" in new WithCursorMocked {
      resultSet.updateNClob(0, javaNull.asInstanceOf[NClob]) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateNClob(columnLabel: String, nClob: NClob)" in new WithCursorMocked {
      resultSet.updateNClob("", javaNull.asInstanceOf[NClob]) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateNClob(columnIndex: Int, reader: Reader, length: Long)" in new WithCursorMocked {
      resultSet.updateNClob(0, new StringReader(""), 0) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateNClob(columnLabel: String, reader: Reader, length: Long)" in new WithCursorMocked {
      resultSet.updateNClob("", new StringReader(""), 0) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateNClob(columnIndex: Int, reader: Reader)" in new WithCursorMocked {
      resultSet.updateNClob(0, new StringReader("")) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateNClob(columnLabel: String, reader: Reader)" in new WithCursorMocked {
      resultSet.updateNClob("", new StringReader("")) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateArray(columnIndex: Int, x: Array)" in new WithCursorMocked {
      resultSet.updateArray(0, javaNull.asInstanceOf[java.sql.Array]) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateArray(columnLabel: String, x: Array)" in new WithCursorMocked {
      resultSet.updateArray("", javaNull.asInstanceOf[java.sql.Array]) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateBlob(columnIndex: Int, x: Blob)" in new WithCursorMocked {
      resultSet.updateBlob(0, javaNull.asInstanceOf[Blob]) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateBlob(columnLabel: String, x: Blob)" in new WithCursorMocked {
      resultSet.updateBlob("", javaNull.asInstanceOf[Blob]) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateBlob(columnIndex: Int, inputStream: InputStream, length: Long)" in new WithCursorMocked {
      resultSet.updateBlob(0, new ByteArrayInputStream(scala.Array[Byte](), 0, 0), 0) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateBlob(columnLabel: String, inputStream: InputStream, length: Long)" in new WithCursorMocked {
      resultSet.updateBlob("", new ByteArrayInputStream(scala.Array[Byte](), 0, 0), 0) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateBlob(columnIndex: Int, inputStream: InputStream)" in new WithCursorMocked {
      resultSet.updateBlob(0, new ByteArrayInputStream(scala.Array[Byte](), 0, 0)) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateBlob(columnLabel: String, inputStream: InputStream)" in new WithCursorMocked {
      resultSet.updateBlob("", new ByteArrayInputStream(scala.Array[Byte](), 0, 0)) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateBinaryStream(columnIndex: Int, x: InputStream, length: Int)" in new WithCursorMocked {
      resultSet.updateBinaryStream(0, new ByteArrayInputStream(scala.Array[Byte](), 0, 0), 0) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateBinaryStream(columnLabel: String, x: InputStream, length: Int)" in new WithCursorMocked {
      resultSet.updateBinaryStream("", new ByteArrayInputStream(scala.Array[Byte](), 0, 0), 0) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateBinaryStream(columnIndex: Int, x: InputStream, length: Long)" in new WithCursorMocked {
      resultSet.updateBinaryStream(0, new ByteArrayInputStream(scala.Array[Byte](), 0, 0), 0l) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateBinaryStream(columnLabel: String, x: InputStream, length: Long)" in new WithCursorMocked {
      resultSet.updateBinaryStream("", new ByteArrayInputStream(scala.Array[Byte](), 0, 0), 0l) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateBinaryStream(columnIndex: Int, x: InputStream)" in new WithCursorMocked {
      resultSet.updateBinaryStream(0, new ByteArrayInputStream(scala.Array[Byte](), 0, 0)) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateBinaryStream(columnLabel: String, x: InputStream)" in new WithCursorMocked {
      resultSet.updateBinaryStream("", new ByteArrayInputStream(scala.Array[Byte](), 0, 0)) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateFloat(columnIndex: Int, x: Float)" in new WithCursorMocked {
      resultSet.updateFloat(0, 0.toFloat) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateFloat(columnLabel: String, x: Float)" in new WithCursorMocked {
      resultSet.updateFloat("", 0.toFloat) must throwA[SQLFeatureNotSupportedException]
    }

    "throws an SQLFeatureNotSupportedException in updateObject(columnIndex: Int, x: scala.Any, scaleOrLength: Int)" in new WithCursorMocked {
      resultSet.updateObject(0, javaNull, 0) must throwA[SQLFeatureNotSupportedException]
    }

    "throws an SQLFeatureNotSupportedException in updateObject(columnIndex: Int, x: scala.Any)" in new WithCursorMocked {
      resultSet.updateObject(0, javaNull) must throwA[SQLFeatureNotSupportedException]
    }

    "throws an SQLFeatureNotSupportedException in updateObject(columnLabel: String, x: scala.Any)" in new WithCursorMocked {
      resultSet.updateObject("", javaNull) must throwA[SQLFeatureNotSupportedException]
    }

    "throws an SQLFeatureNotSupportedException in updateObject(columnLabel: String, x: scala.Any, scaleOrLength: Int)" in new WithCursorMocked {
      resultSet.updateObject("", javaNull, 0) must throwA[SQLFeatureNotSupportedException]
    }

    "throws an SQLFeatureNotSupportedException in updateCharacterStream(columnIndex: Int, x: Reader, length: Int)" in new WithCursorMocked {
      resultSet.updateCharacterStream(0, new StringReader(""), 0) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateCharacterStream(columnLabel: String, reader: Reader, length: Int)" in new WithCursorMocked {
      resultSet.updateCharacterStream("", new StringReader(""), 0) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateCharacterStream(columnIndex: Int, x: Reader, length: Long)" in new WithCursorMocked {
      resultSet.updateCharacterStream(0, new StringReader(""), 0l) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateCharacterStream(columnLabel: String, reader: Reader, length: Long)" in new WithCursorMocked {
      resultSet.updateCharacterStream("", new StringReader(""), 0l) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateCharacterStream(columnIndex: Int, x: Reader)" in new WithCursorMocked {
      resultSet.updateCharacterStream(0, new StringReader("")) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateCharacterStream(columnLabel: String, reader: Reader)" in new WithCursorMocked {
      resultSet.updateCharacterStream("", new StringReader("")) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateRowId(columnIndex: Int, x: RowId)" in new WithCursorMocked {
      resultSet.updateRowId(0, javaNull.asInstanceOf[RowId]) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateRowId(columnLabel: String, x: RowId)" in new WithCursorMocked {
      resultSet.updateRowId("", javaNull.asInstanceOf[RowId]) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateRef(columnIndex: Int, x: Ref)" in new WithCursorMocked {
      resultSet.updateRef(0, javaNull.asInstanceOf[Ref]) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateRef(columnLabel: String, x: Ref)" in new WithCursorMocked {
      resultSet.updateRef("", javaNull.asInstanceOf[Ref]) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateShort(columnIndex: Int, x: Short)" in new WithCursorMocked {
      resultSet.updateShort(0, 0.toShort) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateShort(columnLabel: String, x: Short)" in new WithCursorMocked {
      resultSet.updateShort("", 0.toShort) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateInt(columnIndex: Int, x: Int)" in new WithCursorMocked {
      resultSet.updateInt(0, 0) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateInt(columnLabel: String, x: Int)" in new WithCursorMocked {
      resultSet.updateInt("", 0) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateLong(columnIndex: Int, x: Long)" in new WithCursorMocked {
      resultSet.updateLong(0, 0) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateLong(columnLabel: String, x: Long)" in new WithCursorMocked {
      resultSet.updateLong("", 0) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateClob(columnIndex: Int, x: Clob)" in new WithCursorMocked {
      resultSet.updateClob(0, javaNull.asInstanceOf[Clob]) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateClob(columnLabel: String, x: Clob)" in new WithCursorMocked {
      resultSet.updateClob("", javaNull.asInstanceOf[Clob]) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateClob(columnIndex: Int, reader: Reader, length: Long)" in new WithCursorMocked {
      resultSet.updateClob(0, new StringReader(""), 0) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateClob(columnLabel: String, reader: Reader, length: Long)" in new WithCursorMocked {
      resultSet.updateClob("", new StringReader(""), 0) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateClob(columnIndex: Int, reader: Reader)" in new WithCursorMocked {
      resultSet.updateClob(0, new StringReader("")) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateClob(columnLabel: String, reader: Reader)" in new WithCursorMocked {
      resultSet.updateClob("", new StringReader("")) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateNull(columnIndex: Int)" in new WithCursorMocked {
      resultSet.updateNull(0) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateNull(columnLabel: String)" in new WithCursorMocked {
      resultSet.updateNull("") must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateTime(columnIndex: Int, x: Time)" in new WithCursorMocked {
      resultSet.updateTime(0, new java.sql.Time(0)) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateTime(columnLabel: String, x: Time)" in new WithCursorMocked {
      resultSet.updateTime("", new java.sql.Time(0)) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateAsciiStream(columnIndex: Int, x: InputStream, length: Int)" in new WithCursorMocked {
      resultSet.updateAsciiStream(0, new ByteArrayInputStream(scala.Array[Byte](), 0, 0), 0) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateAsciiStream(columnLabel: String, x: InputStream, length: Int)" in new WithCursorMocked {
      resultSet.updateAsciiStream("", new ByteArrayInputStream(scala.Array[Byte](), 0, 0), 0) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateAsciiStream(columnIndex: Int, x: InputStream, length: Long)" in new WithCursorMocked {
      resultSet.updateAsciiStream(0, new ByteArrayInputStream(scala.Array[Byte](), 0, 0), 0l) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateAsciiStream(columnLabel: String, x: InputStream, length: Long)" in new WithCursorMocked {
      resultSet.updateAsciiStream("", new ByteArrayInputStream(scala.Array[Byte](), 0, 0), 0l) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateAsciiStream(columnIndex: Int, x: InputStream)" in new WithCursorMocked {
      resultSet.updateAsciiStream(0, new ByteArrayInputStream(scala.Array[Byte](), 0, 0)) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateAsciiStream(columnLabel: String, x: InputStream)" in new WithCursorMocked {
      resultSet.updateAsciiStream("", new ByteArrayInputStream(scala.Array[Byte](), 0, 0)) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateBytes(columnIndex: Int, x: SArray[Byte])" in new WithCursorMocked {
      resultSet.updateBytes(0, scala.Array[Byte]()) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateBytes(columnLabel: String, x: SArray[Byte])" in new WithCursorMocked {
      resultSet.updateBytes("", scala.Array[Byte]()) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateSQLXML(columnIndex: Int, xmlObject: SQLXML)" in new WithCursorMocked {
      resultSet.updateSQLXML(0, javaNull.asInstanceOf[SQLXML]) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateSQLXML(columnLabel: String, xmlObject: SQLXML)" in new WithCursorMocked {
      resultSet.updateSQLXML("", javaNull.asInstanceOf[SQLXML]) must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in updateRow()" in new WithCursorMocked {
      resultSet.updateRow() must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in insertRow()" in new WithCursorMocked {
      resultSet.insertRow() must throwA[SQLFeatureNotSupportedException]
    }

    "throws an SQLFeatureNotSupportedException in rowInserted()" in new WithCursorMocked {
      resultSet.rowInserted() must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in refreshRow()" in new WithCursorMocked {
      resultSet.refreshRow() must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in deleteRow()" in new WithCursorMocked {
      resultSet.deleteRow() must throwA[SQLFeatureNotSupportedException]
    }

    "throws an SQLFeatureNotSupportedException in rowUpdated()" in new WithCursorMocked {
      resultSet.rowUpdated() must throwA[SQLFeatureNotSupportedException]
    }
    "throws an SQLFeatureNotSupportedException in rowDeleted()" in new WithCursorMocked {
      resultSet.rowDeleted() must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in moveToCurrentRow()" in new WithCursorMocked {
      resultSet.moveToCurrentRow() must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in moveToInsertRow()" in new WithCursorMocked {
      resultSet.moveToInsertRow() must throwA[SQLFeatureNotSupportedException]
    }


    "throws an SQLFeatureNotSupportedException in cancelRowUpdates()" in new WithCursorMocked {
      resultSet.cancelRowUpdates() must throwA[SQLFeatureNotSupportedException]
    }


  }
}
