package com.fortysevendeg.android.sqlite.resultset

import java.sql._
import scala.{Array => SArray}
import java.util.Date

import com.fortysevendeg.android.sqlite._

import scala.util.Random

class SQLDroidResultSetDataSpec
  extends SQLDroidResultSetSpecification {

  "getTimestamp" should {

    "returns a right TimeStamp when time is stored in an number" in
      new WithMatrixCursor with WithData {
        val timeStamp = new Timestamp(rows.head(columnInteger - 1).asInstanceOf[Int])
        cursor.moveToNext()
        sqlDroid.getTimestamp(columnInteger) shouldEqual timeStamp
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
        sqlDroid.getTimestamp(1) shouldEqual timeStamp
    }

    "returns null when the field is null" in
      new WithMatrixCursor with WithData {
        cursor.moveToNext()
        sqlDroid.getTimestamp(columnNull) must beNull
    }

    "throws SQLException when the cursor is closed" in
      new WithMatrixCursor {
        cursor.close()
        sqlDroid.getTimestamp(1) must throwA[SQLException]
    }

  }

  "getBinaryStream" should {

    "returns a right InputStream when the bytes are stored in an string" in
      new WithMatrixCursor with WithData {
        val value = rows.head(columnString - 1)
        cursor.moveToNext()
        inputStreamToString(sqlDroid.getBinaryStream(columnString)) shouldEqual value
    }

    "returns a right InputStream when the bytes are stored in a byte array" in
      new WithMatrixCursor with WithData {
        val value = new String(rows.head(columnBytes - 1).asInstanceOf[SArray[Byte]])
        cursor.moveToNext()
        inputStreamToString(sqlDroid.getBinaryStream(columnBytes)) shouldEqual value
    }

    "returns null when the field is null" in
      new WithMatrixCursor with WithData {
        cursor.moveToNext()
        sqlDroid.getBinaryStream(columnNull) must beNull
    }

    "throws SQLException when the cursor is closed" in
      new WithMatrixCursor {
        cursor.close()
        sqlDroid.getBinaryStream(columnBytes) must throwA[SQLException]
    }

  }

  "getNClob" should {

    "throws a SQLFeatureNotSupportedException" in new WithCursorMocked {
      sqlDroid.getNClob(1) must throwA[SQLFeatureNotSupportedException]
    }

  }

  "getCharacterStream" should {

    "returns a right Reader when the stream is stored in an string" in
      new WithMatrixCursor with WithData {
        cursor.moveToNext()
        readerToString(sqlDroid.getCharacterStream(columnString)) shouldEqual rows.head(columnString - 1)
      }

    "returns null when the field is null" in
      new WithMatrixCursor with WithData {
        cursor.moveToNext()
        sqlDroid.getCharacterStream(columnNull) must beNull
      }

    "throws SQLException when the cursor is closed" in
      new WithMatrixCursor {
        cursor.close()
        sqlDroid.getCharacterStream(columnString) must throwA[SQLException]
      }

  }

  "getDouble" should {

    "returns a right Double when the value is stored in a Float" in
      new WithMatrixCursor with WithData {
        cursor.moveToNext()
        sqlDroid.getDouble(columnFloat) shouldEqual rows.head(columnFloat - 1)
      }

    "returns 0 when the field is null" in
      new WithMatrixCursor with WithData {
        cursor.moveToNext()
        sqlDroid.getDouble(columnNull) shouldEqual 0
      }

    "throws SQLException when the cursor is closed" in
      new WithMatrixCursor {
        cursor.close()
        sqlDroid.getDouble(columnFloat) must throwA[SQLException]
      }

  }

  "getArray" should {

    "throws a SQLFeatureNotSupportedException" in new WithCursorMocked {
      sqlDroid.getArray(1) must throwA[SQLFeatureNotSupportedException]
    }

  }

  "getURL" should {

    "throws a SQLFeatureNotSupportedException" in new WithCursorMocked {
      sqlDroid.getURL(1) must throwA[SQLFeatureNotSupportedException]
    }

  }

  "getRowId" should {

    "throws a SQLFeatureNotSupportedException" in new WithCursorMocked {
      sqlDroid.getRowId(1) must throwA[SQLFeatureNotSupportedException]
    }

  }

  "getFloat" should {

    "returns a right Float when the value is stored in a Float" in
      new WithMatrixCursor with WithData {
        cursor.moveToNext()
        sqlDroid.getFloat(columnFloat) shouldEqual rows.head(columnFloat - 1)
      }

    "returns 0 when the field is null" in
      new WithMatrixCursor with WithData {
        cursor.moveToNext()
        sqlDroid.getFloat(columnNull) shouldEqual 0
      }

    "throws SQLException when the cursor is closed" in
      new WithMatrixCursor {
        cursor.close()
        sqlDroid.getFloat(columnFloat) must throwA[SQLException]
      }

  }

  "getBigDecimal" should {

    "throws a SQLFeatureNotSupportedException" in new WithCursorMocked {
      sqlDroid.getBigDecimal(1) must throwA[SQLFeatureNotSupportedException]
    }

  }

  "getClob" should {

    "returns a right Clob when the data is stored in an string" in
      new WithMatrixCursor with WithData {
        cursor.moveToNext()
        readerToString(sqlDroid.getClob(columnString).getCharacterStream) shouldEqual rows.head(columnString - 1)
      }

    "returns null when the field is null" in
      new WithMatrixCursor with WithData {
        cursor.moveToNext()
        sqlDroid.getClob(columnNull) must beNull
      }

    "throws SQLException when the cursor is closed" in
      new WithMatrixCursor {
        cursor.close()
        sqlDroid.getClob(columnString) must throwA[SQLException]
      }

  }

  "getLong" should {

    "returns a right Long when the value is stored in a Integer" in
      new WithMatrixCursor with WithData {
        cursor.moveToNext()
        sqlDroid.getLong(columnInteger) shouldEqual rows.head(columnInteger - 1)
      }

    "returns 0 when the field is null" in
      new WithMatrixCursor with WithData {
        cursor.moveToNext()
        sqlDroid.getLong(columnNull) shouldEqual 0
      }

    "throws SQLException when the cursor is closed" in
      new WithMatrixCursor {
        cursor.close()
        sqlDroid.getLong(columnInteger) must throwA[SQLException]
      }

  }

  "getString" should {

    "returns a right String when the value is stored in a String" in
      new WithMatrixCursor with WithData {
        cursor.moveToNext()
        sqlDroid.getString(columnString) shouldEqual rows.head(columnString - 1)
      }

    "returns null when the field is null" in
      new WithMatrixCursor with WithData {
        cursor.moveToNext()
        sqlDroid.getString(columnNull) must beNull
      }

    "throws SQLException when the cursor is closed" in
      new WithMatrixCursor {
        cursor.close()
        sqlDroid.getString(columnString) must throwA[SQLException]
      }

  }

  "getNString" should {

    "throws a SQLFeatureNotSupportedException" in new WithCursorMocked {
      sqlDroid.getNString(1) must throwA[SQLFeatureNotSupportedException]
    }

  }

  "getTime" should {

    "returns a right Time when time is stored in an number" in
      new WithMatrixCursor with WithData {
        val timeStamp = new Time(rows.head(columnInteger - 1).asInstanceOf[Int])
        cursor.moveToNext()
        sqlDroid.getTime(columnInteger) shouldEqual timeStamp
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
        sqlDroid.getTime(1) shouldEqual time
      }

    "returns null when the field is null" in
      new WithMatrixCursor with WithData {
        cursor.moveToNext()
        sqlDroid.getTime(columnNull) must beNull
      }

    "throws SQLException when the cursor is closed" in
      new WithMatrixCursor {
        cursor.close()
        sqlDroid.getTime(1) must throwA[SQLException]
      }

  }

  "getByte" should {

    "returns a right Byte when the value is stored in a Integer" in
      new WithMatrixCursor with WithData {
        cursor.moveToNext()
        sqlDroid.getByte(columnInteger) shouldEqual rows.head(columnInteger - 1)
      }

    "returns 0 when the field is null" in
      new WithMatrixCursor with WithData {
        cursor.moveToNext()
        sqlDroid.getByte(columnNull) shouldEqual 0
      }

    "throws SQLException when the cursor is closed" in
      new WithMatrixCursor {
        cursor.close()
        sqlDroid.getByte(columnInteger) must throwA[SQLException]
      }

  }

  "getBoolean" should {

    "returns a right Boolean when the value is stored in a Integer" in
      new WithMatrixCursor with WithData {
        cursor.moveToNext()
        sqlDroid.getBoolean(columnInteger) shouldEqual rows.head(columnInteger - 1).asInstanceOf[Int] != 0
      }

    "returns false when the field is null" in
      new WithMatrixCursor with WithData {
        cursor.moveToNext()
        sqlDroid.getBoolean(columnNull) must beFalse
      }

    "throws SQLException when the cursor is closed" in
      new WithMatrixCursor {
        cursor.close()
        sqlDroid.getBoolean(columnInteger) must throwA[SQLException]
      }

  }

  "getAsciiStream" should {

    "throws a SQLFeatureNotSupportedException" in new WithCursorMocked {
      sqlDroid.getAsciiStream(1) must throwA[SQLFeatureNotSupportedException]
    }

  }

  "getShort" should {

    "returns a right Short when the value is stored in a Integer" in
      new WithMatrixCursor with WithData {
        cursor.moveToNext()
        sqlDroid.getShort(columnInteger) shouldEqual rows.head(columnInteger - 1)
      }

    "returns 0 when the field is null" in
      new WithMatrixCursor with WithData {
        cursor.moveToNext()
        sqlDroid.getShort(columnNull) shouldEqual 0
      }

    "throws SQLException when the cursor is closed" in
      new WithMatrixCursor {
        cursor.close()
        sqlDroid.getShort(columnInteger) must throwA[SQLException]
      }

  }

  "getObject" should {

    "returns the String when fetching the String value" in
      new WithMatrixCursor with WithData {
        cursor.moveToNext()
        val o = sqlDroid.getObject(columnString)
        o must haveClass[java.lang.String]
        o shouldEqual rows.head(columnString - 1)
      }

    "returns the Integer when fetching the Integer value" in
      new WithMatrixCursor with WithData {
        cursor.moveToNext()
        val o = sqlDroid.getObject(columnInteger)
        o must haveClass[java.lang.Integer]
        o shouldEqual rows.head(columnInteger - 1)
      }

    "returns the Float when fetching the Float value" in
      new WithMatrixCursor with WithData {
        cursor.moveToNext()
        val o = sqlDroid.getObject(columnFloat)
        o must haveClass[java.lang.Float]
        o shouldEqual rows.head(columnFloat - 1)
      }

    "returns the Blob when fetching the byte array value" in
      new WithMatrixCursor with WithData {
        cursor.moveToNext()
        val o = sqlDroid.getObject(columnBytes)
        o must haveClass[SQLDroidBlob]
      }

    "returns null when the field is null" in
      new WithMatrixCursor with WithData {
        cursor.moveToNext()
        sqlDroid.getObject(columnNull) must beNull
      }

  }

  "getNCharacterStream" should {

    "throws a SQLFeatureNotSupportedException" in new WithCursorMocked {
      sqlDroid.getNCharacterStream(1) must throwA[SQLFeatureNotSupportedException]
    }

  }

  "getRef" should {

    "throws a SQLFeatureNotSupportedException" in new WithCursorMocked {
      sqlDroid.getRef(1) must throwA[SQLFeatureNotSupportedException]
    }

  }

  "getBlob" should {

    "returns a right Blob when the data is stored in a byte array" in
      new WithMatrixCursor with WithData {
        cursor.moveToNext()
        val value = new String(rows.head(columnBytes - 1).asInstanceOf[SArray[Byte]])
        inputStreamToString(sqlDroid.getBlob(columnBytes).getBinaryStream) shouldEqual value
      }

    "returns null when the field is null" in
      new WithMatrixCursor with WithData {
        cursor.moveToNext()
        sqlDroid.getBlob(columnNull) must beNull
      }

    "throws SQLException when the cursor is closed" in
      new WithMatrixCursor {
        cursor.close()
        sqlDroid.getBlob(columnBytes) must throwA[SQLException]
      }

  }

  "getDate" should {

    "returns a right Date when is stored in an number" in
      new WithMatrixCursor with WithData {
        val date = new java.sql.Date(rows.head(columnInteger - 1).asInstanceOf[Int])
        cursor.moveToNext()
        sqlDroid.getDate(columnInteger) shouldEqual date
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
        sqlDroid.getDate(1) shouldEqual date
      }

    "returns null when the field is null" in
      new WithMatrixCursor with WithData {
        cursor.moveToNext()
        sqlDroid.getDate(columnNull) must beNull
      }

    "throws SQLException when the cursor is closed" in
      new WithMatrixCursor {
        cursor.close()
        sqlDroid.getDate(1) must throwA[SQLException]
      }

  }

  "getSQLXML" should {

    "throws a SQLFeatureNotSupportedException" in new WithCursorMocked {
      sqlDroid.getSQLXML(1) must throwA[SQLFeatureNotSupportedException]
    }

  }

  "getUnicodeStream" should {

    "throws a SQLFeatureNotSupportedException" in new WithCursorMocked {
      sqlDroid.getUnicodeStream(1) must throwA[SQLFeatureNotSupportedException]
    }

  }

  "getInt" should {

    "returns a right Int when the value is stored in a Integer" in
      new WithMatrixCursor with WithData {
        cursor.moveToNext()
        sqlDroid.getInt(columnInteger) shouldEqual rows.head(columnInteger - 1)
      }

    "returns 0 when the field is null" in
      new WithMatrixCursor with WithData {
        cursor.moveToNext()
        sqlDroid.getInt(columnNull) shouldEqual 0
      }

    "throws SQLException when the cursor is closed" in
      new WithMatrixCursor {
        cursor.close()
        sqlDroid.getInt(columnInteger) must throwA[SQLException]
      }

  }

  "getBytes" should {

    "returns a right byte array when the value is stored in byte array" in
      new WithMatrixCursor with WithData {
        cursor.moveToNext()
        sqlDroid.getBytes(columnBytes) shouldEqual rows.head(columnBytes - 1)
      }

    "returns null when the field is null" in
      new WithMatrixCursor with WithData {
        cursor.moveToNext()
        sqlDroid.getBytes(columnNull) must beNull
      }

    "throws SQLException when the cursor is closed" in
      new WithMatrixCursor {
        cursor.close()
        sqlDroid.getBytes(columnBytes) must throwA[SQLException]
      }

  }

  "getHoldability" should {

    "returns ResultSet.CLOSE_CURSORS_AT_COMMIT on a valid cursor" in new WithCursorMocked {
      sqlDroid.getHoldability shouldEqual ResultSet.CLOSE_CURSORS_AT_COMMIT
    }

    "throws SQLException when the cursor is closed" in
      new WithMatrixCursor {
        cursor.close()
        sqlDroid.getHoldability must throwA[SQLException]
      }

  }

  "getConcurrency" should {

    "returns ResultSet.CONCUR_READ_ONLY on a valid cursor" in new WithCursorMocked {
      sqlDroid.getConcurrency shouldEqual ResultSet.CONCUR_READ_ONLY
    }

    "throws SQLException when the cursor is closed" in
      new WithMatrixCursor {
        cursor.close()
        sqlDroid.getConcurrency must throwA[SQLException]
      }

  }

  "getFetchDirection" should {

    "returns ResultSet.FETCH_FORWARD on a valid cursor" in new WithCursorMocked {
      sqlDroid.getFetchDirection shouldEqual ResultSet.FETCH_FORWARD
    }

    "throws SQLException when the cursor is closed" in
      new WithMatrixCursor {
        cursor.close()
        sqlDroid.getFetchDirection must throwA[SQLException]
      }

  }

  "getCursorName" should {

    "throws a SQLFeatureNotSupportedException" in new WithCursorMocked {
      sqlDroid.getCursorName must throwA[SQLFeatureNotSupportedException]
    }

  }

  "getStatement" should {

    "returns null with a valid cursor" in new WithCursorMocked {
      sqlDroid.getStatement must beNull
    }

  }

}
