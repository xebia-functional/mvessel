package com.fortysevendeg.mvessel.resultset

import java.sql.{Time, SQLFeatureNotSupportedException, SQLException, Timestamp, ResultSet => SQLResultSet}

import com.fortysevendeg.mvessel.data.Blob

import scala.{Array => SArray}
import java.util.Date

import com.fortysevendeg.mvessel._

import scala.util.Random

class ResultSetDataSpec
  extends ResultSetSpecification {

  "getTimestamp" should {

    "returns a right TimeStamp when time is stored in an number" in
      new WithMatrixCursor with WithData {
        val timeStamp = new Timestamp(rows.head(columnInteger - 1).asInstanceOf[Int])
        cursor.moveToNext()
        resultSet.getTimestamp(columnInteger) shouldEqual timeStamp
    }

    "returns a right TimeStamp when time is stored in a string with valid format" in
      new WithMatrixCursor {
        val timeStamp = new Timestamp(Random.nextInt(100))
        cursor.addRow(SArray[AnyRef](
          formatDateOrThrow(new Date(timeStamp.getTime)),
          javaNull,
          javaNull,
          javaNull,
          javaNull))
        cursor.moveToNext()
        resultSet.getTimestamp(1) shouldEqual timeStamp
    }

    "returns null when the field is null" in
      new WithMatrixCursor with WithData {
        cursor.moveToNext()
        resultSet.getTimestamp(columnNull) must beNull
    }

    "throws SQLException when the cursor is closed" in
      new WithMatrixCursor {
        cursor.close()
        resultSet.getTimestamp(1) must throwA[SQLException]
    }

  }

  "getBinaryStream" should {

    "returns a right InputStream when the bytes are stored in an string" in
      new WithMatrixCursor with WithData {
        val value = rows.head(columnString - 1)
        cursor.moveToNext()
        inputStreamToString(resultSet.getBinaryStream(columnString)) shouldEqual value
    }

    "returns a right InputStream when the bytes are stored in a byte array" in
      new WithMatrixCursor with WithData {
        val value = new String(rows.head(columnBytes - 1).asInstanceOf[SArray[Byte]])
        cursor.moveToNext()
        inputStreamToString(resultSet.getBinaryStream(columnBytes)) shouldEqual value
    }

    "returns null when the field is null" in
      new WithMatrixCursor with WithData {
        cursor.moveToNext()
        resultSet.getBinaryStream(columnNull) must beNull
    }

    "throws SQLException when the cursor is closed" in
      new WithMatrixCursor {
        cursor.close()
        resultSet.getBinaryStream(columnBytes) must throwA[SQLException]
    }

  }

  "getNClob" should {

    "throws a SQLFeatureNotSupportedException" in new WithCursorMocked {
      resultSet.getNClob(1) must throwA[SQLFeatureNotSupportedException]
    }

  }

  "getCharacterStream" should {

    "returns a right Reader when the stream is stored in an string" in
      new WithMatrixCursor with WithData {
        cursor.moveToNext()
        readerToString(resultSet.getCharacterStream(columnString)) shouldEqual rows.head(columnString - 1)
      }

    "returns null when the field is null" in
      new WithMatrixCursor with WithData {
        cursor.moveToNext()
        resultSet.getCharacterStream(columnNull) must beNull
      }

    "throws SQLException when the cursor is closed" in
      new WithMatrixCursor {
        cursor.close()
        resultSet.getCharacterStream(columnString) must throwA[SQLException]
      }

  }

  "getDouble" should {

    "returns a right Double when the value is stored in a Float" in
      new WithMatrixCursor with WithData {
        cursor.moveToNext()
        resultSet.getDouble(columnFloat) shouldEqual rows.head(columnFloat - 1)
      }

    "returns 0 when the field is null" in
      new WithMatrixCursor with WithData {
        cursor.moveToNext()
        resultSet.getDouble(columnNull) shouldEqual 0
      }

    "throws SQLException when the cursor is closed" in
      new WithMatrixCursor {
        cursor.close()
        resultSet.getDouble(columnFloat) must throwA[SQLException]
      }

  }

  "getArray" should {

    "throws a SQLFeatureNotSupportedException" in new WithCursorMocked {
      resultSet.getArray(1) must throwA[SQLFeatureNotSupportedException]
    }

  }

  "getURL" should {

    "throws a SQLFeatureNotSupportedException" in new WithCursorMocked {
      resultSet.getURL(1) must throwA[SQLFeatureNotSupportedException]
    }

  }

  "getRowId" should {

    "throws a SQLFeatureNotSupportedException" in new WithCursorMocked {
      resultSet.getRowId(1) must throwA[SQLFeatureNotSupportedException]
    }

  }

  "getFloat" should {

    "returns a right Float when the value is stored in a Float" in
      new WithMatrixCursor with WithData {
        cursor.moveToNext()
        resultSet.getFloat(columnFloat) shouldEqual rows.head(columnFloat - 1)
      }

    "returns 0 when the field is null" in
      new WithMatrixCursor with WithData {
        cursor.moveToNext()
        resultSet.getFloat(columnNull) shouldEqual 0
      }

    "throws SQLException when the cursor is closed" in
      new WithMatrixCursor {
        cursor.close()
        resultSet.getFloat(columnFloat) must throwA[SQLException]
      }

  }

  "getBigDecimal" should {

    "throws a SQLFeatureNotSupportedException" in new WithCursorMocked {
      resultSet.getBigDecimal(1) must throwA[SQLFeatureNotSupportedException]
    }

  }

  "getClob" should {

    "returns a right Clob when the data is stored in an string" in
      new WithMatrixCursor with WithData {
        cursor.moveToNext()
        readerToString(resultSet.getClob(columnString).getCharacterStream) shouldEqual rows.head(columnString - 1)
      }

    "returns null when the field is null" in
      new WithMatrixCursor with WithData {
        cursor.moveToNext()
        resultSet.getClob(columnNull) must beNull
      }

    "throws SQLException when the cursor is closed" in
      new WithMatrixCursor {
        cursor.close()
        resultSet.getClob(columnString) must throwA[SQLException]
      }

  }

  "getLong" should {

    "returns a right Long when the value is stored in a Integer" in
      new WithMatrixCursor with WithData {
        cursor.moveToNext()
        resultSet.getLong(columnInteger) shouldEqual rows.head(columnInteger - 1)
      }

    "returns 0 when the field is null" in
      new WithMatrixCursor with WithData {
        cursor.moveToNext()
        resultSet.getLong(columnNull) shouldEqual 0
      }

    "throws SQLException when the cursor is closed" in
      new WithMatrixCursor {
        cursor.close()
        resultSet.getLong(columnInteger) must throwA[SQLException]
      }

  }

  "getString" should {

    "returns a right String when the value is stored in a String" in
      new WithMatrixCursor with WithData {
        cursor.moveToNext()
        resultSet.getString(columnString) shouldEqual rows.head(columnString - 1)
      }

    "returns null when the field is null" in
      new WithMatrixCursor with WithData {
        cursor.moveToNext()
        resultSet.getString(columnNull) must beNull
      }

    "throws SQLException when the cursor is closed" in
      new WithMatrixCursor {
        cursor.close()
        resultSet.getString(columnString) must throwA[SQLException]
      }

  }

  "getNString" should {

    "throws a SQLFeatureNotSupportedException" in new WithCursorMocked {
      resultSet.getNString(1) must throwA[SQLFeatureNotSupportedException]
    }

  }

  "getTime" should {

    "returns a right Time when time is stored in an number" in
      new WithMatrixCursor with WithData {
        val timeStamp = new Time(rows.head(columnInteger - 1).asInstanceOf[Int])
        cursor.moveToNext()
        resultSet.getTime(columnInteger) shouldEqual timeStamp
      }

    "returns a right Time when time is stored in a string with valid format" in
      new WithMatrixCursor {
        val time = new Time(Random.nextInt(100))
        cursor.addRow(SArray[AnyRef](
          formatDateOrThrow(new Date(time.getTime)),
          javaNull,
          javaNull,
          javaNull,
          javaNull))
        cursor.moveToNext()
        resultSet.getTime(1) shouldEqual time
      }

    "returns null when the field is null" in
      new WithMatrixCursor with WithData {
        cursor.moveToNext()
        resultSet.getTime(columnNull) must beNull
      }

    "throws SQLException when the cursor is closed" in
      new WithMatrixCursor {
        cursor.close()
        resultSet.getTime(1) must throwA[SQLException]
      }

  }

  "getByte" should {

    "returns a right Byte when the value is stored in a Integer" in
      new WithMatrixCursor with WithData {
        cursor.moveToNext()
        resultSet.getByte(columnInteger) shouldEqual rows.head(columnInteger - 1)
      }

    "returns 0 when the field is null" in
      new WithMatrixCursor with WithData {
        cursor.moveToNext()
        resultSet.getByte(columnNull) shouldEqual 0
      }

    "throws SQLException when the cursor is closed" in
      new WithMatrixCursor {
        cursor.close()
        resultSet.getByte(columnInteger) must throwA[SQLException]
      }

  }

  "getBoolean" should {

    "returns a right Boolean when the value is stored in a Integer" in
      new WithMatrixCursor with WithData {
        cursor.moveToNext()
        resultSet.getBoolean(columnInteger) shouldEqual rows.head(columnInteger - 1).asInstanceOf[Int] != 0
      }

    "returns false when the field is null" in
      new WithMatrixCursor with WithData {
        cursor.moveToNext()
        resultSet.getBoolean(columnNull) must beFalse
      }

    "throws SQLException when the cursor is closed" in
      new WithMatrixCursor {
        cursor.close()
        resultSet.getBoolean(columnInteger) must throwA[SQLException]
      }

  }

  "getAsciiStream" should {

    "throws a SQLFeatureNotSupportedException" in new WithCursorMocked {
      resultSet.getAsciiStream(1) must throwA[SQLFeatureNotSupportedException]
    }

  }

  "getShort" should {

    "returns a right Short when the value is stored in a Integer" in
      new WithMatrixCursor with WithData {
        cursor.moveToNext()
        resultSet.getShort(columnInteger) shouldEqual rows.head(columnInteger - 1)
      }

    "returns 0 when the field is null" in
      new WithMatrixCursor with WithData {
        cursor.moveToNext()
        resultSet.getShort(columnNull) shouldEqual 0
      }

    "throws SQLException when the cursor is closed" in
      new WithMatrixCursor {
        cursor.close()
        resultSet.getShort(columnInteger) must throwA[SQLException]
      }

  }

  "getObject" should {

    "returns the String when fetching the String value" in
      new WithMatrixCursor with WithData {
        cursor.moveToNext()
        val o = resultSet.getObject(columnString)
        o must haveClass[java.lang.String]
        o shouldEqual rows.head(columnString - 1)
      }

    "returns the Integer when fetching the Integer value" in
      new WithMatrixCursor with WithData {
        cursor.moveToNext()
        val o = resultSet.getObject(columnInteger)
        o must haveClass[java.lang.Integer]
        o shouldEqual rows.head(columnInteger - 1)
      }

    "returns the Float when fetching the Float value" in
      new WithMatrixCursor with WithData {
        cursor.moveToNext()
        val o = resultSet.getObject(columnFloat)
        o must haveClass[java.lang.Float]
        o shouldEqual rows.head(columnFloat - 1)
      }

    "returns the Blob when fetching the byte array value" in
      new WithMatrixCursor with WithData {
        cursor.moveToNext()
        val o = resultSet.getObject(columnBytes)
        o must haveClass[Blob]
      }

    "returns null when the field is null" in
      new WithMatrixCursor with WithData {
        cursor.moveToNext()
        resultSet.getObject(columnNull) must beNull
      }

  }

  "getNCharacterStream" should {

    "throws a SQLFeatureNotSupportedException" in new WithCursorMocked {
      resultSet.getNCharacterStream(1) must throwA[SQLFeatureNotSupportedException]
    }

  }

  "getRef" should {

    "throws a SQLFeatureNotSupportedException" in new WithCursorMocked {
      resultSet.getRef(1) must throwA[SQLFeatureNotSupportedException]
    }

  }

  "getBlob" should {

    "returns a right Blob when the data is stored in a byte array" in
      new WithMatrixCursor with WithData {
        cursor.moveToNext()
        val value = new String(rows.head(columnBytes - 1).asInstanceOf[SArray[Byte]])
        inputStreamToString(resultSet.getBlob(columnBytes).getBinaryStream) shouldEqual value
      }

    "returns null when the field is null" in
      new WithMatrixCursor with WithData {
        cursor.moveToNext()
        resultSet.getBlob(columnNull) must beNull
      }

    "throws SQLException when the cursor is closed" in
      new WithMatrixCursor {
        cursor.close()
        resultSet.getBlob(columnBytes) must throwA[SQLException]
      }

  }

  "getDate" should {

    "returns a right Date when is stored in an number" in
      new WithMatrixCursor with WithData {
        val date = new java.sql.Date(rows.head(columnInteger - 1).asInstanceOf[Int])
        cursor.moveToNext()
        resultSet.getDate(columnInteger) shouldEqual date
      }

    "returns a right Date when is stored in a string with valid format" in
      new WithMatrixCursor {
        val date = new java.sql.Date(Random.nextInt(100).toLong)
        cursor.addRow(SArray[AnyRef](
          formatDateOrThrow(new Date(date.getTime)),
          javaNull,
          javaNull,
          javaNull,
          javaNull))
        cursor.moveToNext()
        resultSet.getDate(1) shouldEqual date
      }

    "returns null when the field is null" in
      new WithMatrixCursor with WithData {
        cursor.moveToNext()
        resultSet.getDate(columnNull) must beNull
      }

    "throws SQLException when the cursor is closed" in
      new WithMatrixCursor {
        cursor.close()
        resultSet.getDate(1) must throwA[SQLException]
      }

  }

  "getSQLXML" should {

    "throws a SQLFeatureNotSupportedException" in new WithCursorMocked {
      resultSet.getSQLXML(1) must throwA[SQLFeatureNotSupportedException]
    }

  }

  "getUnicodeStream" should {

    "throws a SQLFeatureNotSupportedException" in new WithCursorMocked {
      resultSet.getUnicodeStream(1) must throwA[SQLFeatureNotSupportedException]
    }

  }

  "getInt" should {

    "returns a right Int when the value is stored in a Integer" in
      new WithMatrixCursor with WithData {
        cursor.moveToNext()
        resultSet.getInt(columnInteger) shouldEqual rows.head(columnInteger - 1)
      }

    "returns 0 when the field is null" in
      new WithMatrixCursor with WithData {
        cursor.moveToNext()
        resultSet.getInt(columnNull) shouldEqual 0
      }

    "throws SQLException when the cursor is closed" in
      new WithMatrixCursor {
        cursor.close()
        resultSet.getInt(columnInteger) must throwA[SQLException]
      }

  }

  "getBytes" should {

    "returns a right byte array when the value is stored in byte array" in
      new WithMatrixCursor with WithData {
        cursor.moveToNext()
        resultSet.getBytes(columnBytes) shouldEqual rows.head(columnBytes - 1)
      }

    "returns null when the field is null" in
      new WithMatrixCursor with WithData {
        cursor.moveToNext()
        resultSet.getBytes(columnNull) must beNull
      }

    "throws SQLException when the cursor is closed" in
      new WithMatrixCursor {
        cursor.close()
        resultSet.getBytes(columnBytes) must throwA[SQLException]
      }

  }

  "getHoldability" should {

    "returns ResultSet.CLOSE_CURSORS_AT_COMMIT on a valid cursor" in new WithCursorMocked {
      resultSet.getHoldability shouldEqual SQLResultSet.CLOSE_CURSORS_AT_COMMIT
    }

    "throws SQLException when the cursor is closed" in
      new WithMatrixCursor {
        cursor.close()
        resultSet.getHoldability must throwA[SQLException]
      }

  }

  "getConcurrency" should {

    "returns ResultSet.CONCUR_READ_ONLY on a valid cursor" in new WithCursorMocked {
      resultSet.getConcurrency shouldEqual SQLResultSet.CONCUR_READ_ONLY
    }

    "throws SQLException when the cursor is closed" in
      new WithMatrixCursor {
        cursor.close()
        resultSet.getConcurrency must throwA[SQLException]
      }

  }

  "getFetchDirection" should {

    "returns ResultSet.FETCH_FORWARD on a valid cursor" in new WithCursorMocked {
      resultSet.getFetchDirection shouldEqual SQLResultSet.FETCH_FORWARD
    }

    "throws SQLException when the cursor is closed" in
      new WithMatrixCursor {
        cursor.close()
        resultSet.getFetchDirection must throwA[SQLException]
      }

  }

  "getCursorName" should {

    "throws a SQLFeatureNotSupportedException" in new WithCursorMocked {
      resultSet.getCursorName must throwA[SQLFeatureNotSupportedException]
    }

  }

  "getStatement" should {

    "returns null with a valid cursor" in new WithCursorMocked {
      resultSet.getStatement must beNull
    }

  }

}
