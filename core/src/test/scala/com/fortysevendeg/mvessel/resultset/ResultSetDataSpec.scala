package com.fortysevendeg.mvessel.resultset

import java.sql.{ResultSet => SQLResultSet, SQLException, SQLFeatureNotSupportedException, Time, Timestamp}
import java.util.{Calendar, Date}

import com.fortysevendeg.mvessel._
import com.fortysevendeg.mvessel.api.impl.CursorSeq
import com.fortysevendeg.mvessel.data.Blob

import scala.util.Random
import scala.{Array => SArray}

class ResultSetDataSpec
  extends ResultSetSpecification {

  "getTimestamp" should {

    "return a right TimeStamp when time is stored in an number" in
      new WithCursor {
        val time = rows.head(columnInteger - 1).asInstanceOf[Int].toLong
        cursor.moveToNext()
        resultSet.getTimestamp(columnInteger).getTime shouldEqual time
    }

    "return the right TimeStamp when passing a right column name" in new WithCursor {
      val time = rows.head(columnInteger - 1).asInstanceOf[Int].toLong
      cursor.moveToNext()
      resultSet.getTimestamp(columnNames(columnInteger - 1)).getTime shouldEqual time
    }

    "return the right TimeStamp when passing a right column index and a Calendar instance" in new WithCursor {
      val time = rows.head(columnInteger - 1).asInstanceOf[Int].toLong
      cursor.moveToNext()
      resultSet.getTimestamp(columnInteger, mock[Calendar]).getTime shouldEqual time
    }

    "return the right TimeStamp when passing a right column name and a Calendar instance" in new WithCursor {
      val time = rows.head(columnInteger - 1).asInstanceOf[Int].toLong
      cursor.moveToNext()
      resultSet.getTimestamp(columnNames(columnInteger - 1), mock[Calendar]).getTime shouldEqual time
    }

    "return a right TimeStamp when time is stored in a string with valid format" in
      new WithEmptyCursor {
        val timeStamp = new Timestamp(Random.nextInt(100))
        val row = Seq[Any](formatDateOrThrow(new Date(timeStamp.getTime)))
        val c = new CursorSeq(Seq("column"), Seq(row))
        val rs = new ResultSet(c, new TestLogWrapper)

        c.moveToNext()
        rs.getTimestamp(1) shouldEqual timeStamp
    }

    "return null when the field is null" in
      new WithCursor {
        cursor.moveToNext()
        resultSet.getTimestamp(columnNull) must beNull
    }

    "throw SQLException when the cursor is closed" in
      new WithEmptyCursor {
        cursor.close()
        resultSet.getTimestamp(1) must throwA[SQLException]
    }

  }

  "getBinaryStream" should {

    "return a right InputStream when the bytes are stored in an string" in
      new WithCursor {
        val value = rows.head(columnString - 1)
        cursor.moveToNext()
        inputStreamToString(resultSet.getBinaryStream(columnString)) shouldEqual value
    }

    "return a right InputStream when the bytes are stored in a byte array" in
      new WithCursor {
        val value = new String(rows.head(columnBytes - 1).asInstanceOf[SArray[Byte]])
        cursor.moveToNext()
        inputStreamToString(resultSet.getBinaryStream(columnBytes)) shouldEqual value
    }

    "return a right InputStream when the bytes are stored in an string and pass the column name" in
      new WithCursor {
        val value = rows.head(columnString - 1)
        cursor.moveToNext()
        inputStreamToString(resultSet.getBinaryStream(columnNames(columnString - 1))) shouldEqual value
    }

    "return a right InputStream when the bytes are stored in a byte array and pass the column name" in
      new WithCursor {
        val value = new String(rows.head(columnBytes - 1).asInstanceOf[SArray[Byte]])
        cursor.moveToNext()
        inputStreamToString(resultSet.getBinaryStream(columnNames(columnBytes - 1))) shouldEqual value
    }

    "return null when the field is null" in
      new WithCursor {
        cursor.moveToNext()
        resultSet.getBinaryStream(columnNull) must beNull
    }

    "throw SQLException when the cursor is closed" in
      new WithEmptyCursor {
        cursor.close()
        resultSet.getBinaryStream(columnBytes) must throwA[SQLException]
    }

  }

  "getNClob" should {

    "throw a SQLFeatureNotSupportedException" in new WithCursorMocked {
      resultSet.getNClob(1) must throwA[SQLFeatureNotSupportedException]
    }

    "throw a SQLFeatureNotSupportedException when pass a column name" in new WithCursorMocked {
      resultSet.getNClob("") must throwA[SQLFeatureNotSupportedException]
    }

  }

  "getCharacterStream" should {

    "return a right Reader when the stream is stored in an string" in
      new WithCursor {
        cursor.moveToNext()
        readerToString(resultSet.getCharacterStream(columnString)) shouldEqual rows.head(columnString - 1)
      }

    "return a right Reader when the stream is stored in an string and pass the column name" in
      new WithCursor {
        cursor.moveToNext()
        readerToString(resultSet.getCharacterStream(columnNames(columnString - 1))) shouldEqual rows.head(columnString - 1)
      }

    "return null when the field is null" in
      new WithCursor {
        cursor.moveToNext()
        resultSet.getCharacterStream(columnNull) must beNull
      }

    "throw SQLException when the cursor is closed" in
      new WithEmptyCursor {
        cursor.close()
        resultSet.getCharacterStream(columnString) must throwA[SQLException]
      }

  }

  "getDouble" should {

    "return a right Double when the value is stored in a Float" in
      new WithCursor {
        cursor.moveToNext()
        resultSet.getDouble(columnFloat) shouldEqual rows.head(columnFloat - 1)
      }

    "return a right Double when the value is stored in a Float and pass the column name" in
      new WithCursor {
        cursor.moveToNext()
        resultSet.getDouble(columnNames(columnFloat - 1)) shouldEqual rows.head(columnFloat - 1)
      }

    "return 0 when the field is null" in
      new WithCursor {
        cursor.moveToNext()
        resultSet.getDouble(columnNull) shouldEqual 0
      }

    "throw SQLException when the cursor is closed" in
      new WithEmptyCursor {
        cursor.close()
        resultSet.getDouble(columnFloat) must throwA[SQLException]
      }

  }

  "getArray" should {

    "throw a SQLFeatureNotSupportedException" in new WithCursorMocked {
      resultSet.getArray(1) must throwA[SQLFeatureNotSupportedException]
    }

    "throw a SQLFeatureNotSupportedException when pass a column name" in new WithCursorMocked {
      resultSet.getArray("") must throwA[SQLFeatureNotSupportedException]
    }

  }

  "getURL" should {

    "throw a SQLFeatureNotSupportedException" in new WithCursorMocked {
      resultSet.getURL(1) must throwA[SQLFeatureNotSupportedException]
    }

    "throw a SQLFeatureNotSupportedException when pass a column name" in new WithCursorMocked {
      resultSet.getURL("") must throwA[SQLFeatureNotSupportedException]
    }

  }

  "getRowId" should {

    "throw a SQLFeatureNotSupportedException" in new WithCursorMocked {
      resultSet.getRowId(1) must throwA[SQLFeatureNotSupportedException]
    }

    "throw a SQLFeatureNotSupportedException when pass a column name" in new WithCursorMocked {
      resultSet.getRowId(1) must throwA[SQLFeatureNotSupportedException]
    }

  }

  "getFloat" should {

    "return a right Float when the value is stored in a Float" in
      new WithCursor {
        cursor.moveToNext()
        resultSet.getFloat(columnFloat) shouldEqual rows.head(columnFloat - 1)
      }

    "return a right Float when the value is stored in a Float and pass the column name" in
      new WithCursor {
        cursor.moveToNext()
        resultSet.getFloat(columnNames(columnFloat - 1)) shouldEqual rows.head(columnFloat - 1)
      }

    "return 0 when the field is null" in
      new WithCursor {
        cursor.moveToNext()
        resultSet.getFloat(columnNull) shouldEqual 0
      }

    "throw SQLException when the cursor is closed" in
      new WithEmptyCursor {
        cursor.close()
        resultSet.getFloat(columnFloat) must throwA[SQLException]
      }

  }

  "getBigDecimal" should {

    "throw a SQLFeatureNotSupportedException" in new WithCursorMocked {
      resultSet.getBigDecimal(1) must throwA[SQLFeatureNotSupportedException]
    }

    "throw a SQLFeatureNotSupportedException when pass a column name" in new WithCursorMocked {
      resultSet.getBigDecimal("") must throwA[SQLFeatureNotSupportedException]
    }

  }

  "getClob" should {

    "return a right Clob when the data is stored in an string" in
      new WithCursor {
        cursor.moveToNext()
        readerToString(resultSet.getClob(columnString).getCharacterStream) shouldEqual rows.head(columnString - 1)
      }

    "return a right Clob when the data is stored in an string and pass the column name" in
      new WithCursor {
        cursor.moveToNext()
        readerToString(resultSet.getClob(columnNames(columnString - 1)).getCharacterStream) shouldEqual rows.head(columnString - 1)
      }

    "return null when the field is null" in
      new WithCursor {
        cursor.moveToNext()
        resultSet.getClob(columnNull) must beNull
      }

    "throw SQLException when the cursor is closed" in
      new WithEmptyCursor {
        cursor.close()
        resultSet.getClob(columnString) must throwA[SQLException]
      }

  }

  "getLong" should {

    "return a right Long when the value is stored in a Integer" in
      new WithCursor {
        cursor.moveToNext()
        resultSet.getLong(columnInteger) shouldEqual rows.head(columnInteger - 1)
      }

    "return a right Long when the value is stored in a Integer and pass the column name" in
      new WithCursor {
        cursor.moveToNext()
        resultSet.getLong(columnNames(columnInteger - 1)) shouldEqual rows.head(columnInteger - 1)
      }

    "return 0 when the field is null" in
      new WithCursor {
        cursor.moveToNext()
        resultSet.getLong(columnNull) shouldEqual 0
      }

    "throw SQLException when the cursor is closed" in
      new WithEmptyCursor {
        cursor.close()
        resultSet.getLong(columnInteger) must throwA[SQLException]
      }

  }

  "getString" should {

    "return a right String when the value is stored in a String" in
      new WithCursor {
        cursor.moveToNext()
        resultSet.getString(columnString) shouldEqual rows.head(columnString - 1)
      }

    "return a right String when the value is stored in a String and pass the column name" in
      new WithCursor {
        cursor.moveToNext()
        resultSet.getString(columnNames(columnString - 1)) shouldEqual rows.head(columnString - 1)
      }

    "return null when the field is null" in
      new WithCursor {
        cursor.moveToNext()
        resultSet.getString(columnNull) must beNull
      }

    "throw SQLException when the cursor is closed" in
      new WithEmptyCursor {
        cursor.close()
        resultSet.getString(columnString) must throwA[SQLException]
      }

  }

  "getNString" should {

    "throw a SQLFeatureNotSupportedException" in new WithCursorMocked {
      resultSet.getNString(1) must throwA[SQLFeatureNotSupportedException]
    }

    "throw a SQLFeatureNotSupportedException when pass a column name" in new WithCursorMocked {
      resultSet.getNString("") must throwA[SQLFeatureNotSupportedException]
    }

  }

  "getTime" should {

    "return a right Time when time is stored in an number" in
      new WithCursor {
        val timeStamp = new Time(rows.head(columnInteger - 1).asInstanceOf[Int])
        cursor.moveToNext()
        resultSet.getTime(columnInteger) shouldEqual timeStamp
      }

    "return a right Time when time is stored in a string with valid format" in
      new WithEmptyCursor {
        val time = new Time(Random.nextInt(100))
        val row = Seq[Any](formatDateOrThrow(new Date(time.getTime)))

        val c = new CursorSeq(Seq("column"), Seq(row))
        val rs = new ResultSet(c, new TestLogWrapper)

        c.moveToNext()
        rs.getTime(1) shouldEqual time
      }

    "return a right Time when time is stored in an number and pass the column name" in
      new WithCursor {
        val timeStamp = new Time(rows.head(columnInteger - 1).asInstanceOf[Int])
        cursor.moveToNext()
        resultSet.getTime(columnNames(columnInteger - 1)) shouldEqual timeStamp
      }

    "return a right Time when time is stored in a string with valid format and pass the column name" in
      new WithEmptyCursor {
        val time = new Time(Random.nextInt(100))
        val row = Seq[Any](formatDateOrThrow(new Date(time.getTime)))
        val column = "column"
        val c = new CursorSeq(Seq(column), Seq(row))
        val rs = new ResultSet(c, new TestLogWrapper)

        c.moveToNext()
        rs.getTime(column) shouldEqual time
      }

    "return null when the field is null" in
      new WithCursor {
        cursor.moveToNext()
        resultSet.getTime(columnNull) must beNull
      }

    "throw SQLException when the cursor is closed" in
      new WithEmptyCursor {
        cursor.close()
        resultSet.getTime(1) must throwA[SQLException]
      }

  }

  "getByte" should {

    "return a right Byte when the value is stored in a Integer" in
      new WithCursor {
        cursor.moveToNext()
        resultSet.getByte(columnInteger) shouldEqual rows.head(columnInteger - 1)
      }

    "return a right Byte when the value is stored in a Integer and pass the column name" in
      new WithCursor {
        cursor.moveToNext()
        resultSet.getByte(columnNames(columnInteger - 1)) shouldEqual rows.head(columnInteger - 1)
      }

    "return 0 when the field is null" in
      new WithCursor {
        cursor.moveToNext()
        resultSet.getByte(columnNull) shouldEqual 0
      }

    "throw SQLException when the cursor is closed" in
      new WithEmptyCursor {
        cursor.close()
        resultSet.getByte(columnInteger) must throwA[SQLException]
      }

  }

  "getBoolean" should {

    "return a right Boolean when the value is stored in a Integer" in
      new WithCursor {
        cursor.moveToNext()
        resultSet.getBoolean(columnInteger) shouldEqual rows.head(columnInteger - 1).asInstanceOf[Int] != 0
      }

    "return a right Boolean when the value is stored in a Integer and pass the column name" in
      new WithCursor {
        cursor.moveToNext()
        resultSet.getBoolean(columnNames(columnInteger - 1)) shouldEqual rows.head(columnInteger - 1).asInstanceOf[Int] != 0
      }

    "return false when the field is null" in
      new WithCursor {
        cursor.moveToNext()
        resultSet.getBoolean(columnNull) must beFalse
      }

    "throw SQLException when the cursor is closed" in
      new WithEmptyCursor {
        cursor.close()
        resultSet.getBoolean(columnInteger) must throwA[SQLException]
      }

  }

  "getAsciiStream" should {

    "throw a SQLFeatureNotSupportedException" in new WithCursorMocked {
      resultSet.getAsciiStream(1) must throwA[SQLFeatureNotSupportedException]
    }

    "throw a SQLFeatureNotSupportedException when pass a column name" in new WithCursorMocked {
      resultSet.getAsciiStream("") must throwA[SQLFeatureNotSupportedException]
    }

  }

  "getShort" should {

    "return a right Short when the value is stored in a Integer" in
      new WithCursor {
        cursor.moveToNext()
        resultSet.getShort(columnInteger) shouldEqual rows.head(columnInteger - 1)
      }

    "return a right Short when the value is stored in a Integer and pass the column name" in
      new WithCursor {
        cursor.moveToNext()
        resultSet.getShort(columnNames(columnInteger - 1)) shouldEqual rows.head(columnInteger - 1)
      }

    "return 0 when the field is null" in
      new WithCursor {
        cursor.moveToNext()
        resultSet.getShort(columnNull) shouldEqual 0
      }

    "throw SQLException when the cursor is closed" in
      new WithEmptyCursor {
        cursor.close()
        resultSet.getShort(columnInteger) must throwA[SQLException]
      }

  }

  "getObject" should {

    "return the String when fetching the String value" in new WithCursor {
        cursor.moveToNext()
        val o = resultSet.getObject(columnString)
        o must haveClass[java.lang.String]
        o shouldEqual rows.head(columnString - 1)
      }

    "return the String when fetching the String value and pass the column name" in new WithCursor {
        cursor.moveToNext()
        val o = resultSet.getObject(columnNames(columnString - 1))
        o must haveClass[java.lang.String]
        o shouldEqual rows.head(columnString - 1)
      }

    "return the Integer when fetching the Integer value" in new WithCursor {
        cursor.moveToNext()
        val o = resultSet.getObject(columnInteger)
        o must haveClass[java.lang.Integer]
        o shouldEqual rows.head(columnInteger - 1)
      }

    "return the Integer when fetching the Integer value and pass the column name" in new WithCursor {
        cursor.moveToNext()
        val o = resultSet.getObject(columnNames(columnInteger - 1))
        o must haveClass[java.lang.Integer]
        o shouldEqual rows.head(columnInteger - 1)
      }

    "return the Float when fetching the Float value" in new WithCursor {
        cursor.moveToNext()
        val o = resultSet.getObject(columnFloat)
        o must haveClass[java.lang.Float]
        o shouldEqual rows.head(columnFloat - 1)
      }

    "return the Float when fetching the Float value and pass the column name" in new WithCursor {
        cursor.moveToNext()
        val o = resultSet.getObject(columnNames(columnFloat - 1))
        o must haveClass[java.lang.Float]
        o shouldEqual rows.head(columnFloat - 1)
      }

    "return the Blob when fetching the byte array value" in new WithCursor {
        cursor.moveToNext()
        val o = resultSet.getObject(columnBytes)
        o must haveClass[Blob]
      }

    "return the Blob when fetching the byte array value and pass the column name" in new WithCursor {
        cursor.moveToNext()
        val o = resultSet.getObject(columnNames(columnBytes - 1))
        o must haveClass[Blob]
      }

    "return null when the field is null" in new WithCursor {
        cursor.moveToNext()
        resultSet.getObject(columnNull) must beNull
      }

    "throw a SQLFeatureNotSupportedException when pass a map" in new WithCursor {
      import collection.JavaConversions._
      cursor.moveToNext()
      resultSet.getObject(
        columnString,
        mapAsJavaMap(Map.empty[String, Class[_]])) must throwA[SQLFeatureNotSupportedException]
    }

    "throw a SQLFeatureNotSupportedException when pass the column name and a map" in new WithCursor {
      import collection.JavaConversions._
      cursor.moveToNext()
      resultSet.getObject(
        columnNames(columnString - 1),
        mapAsJavaMap(Map.empty[String, Class[_]])) must throwA[SQLFeatureNotSupportedException]
    }

    "return the String when specify the right class type" in new WithCursor {
      cursor.moveToNext()
      val string = resultSet.getObject(columnString, classOf[String])
      string must beAnInstanceOf[String]
      string shouldEqual rows.head(columnString - 1)
    }

    "return the String when specify the right class type and the column name" in new WithCursor {
      cursor.moveToNext()
      val string = resultSet.getObject(columnNames(columnString - 1), classOf[String])
      string must beAnInstanceOf[String]
      string shouldEqual rows.head(columnString - 1)
    }

    "throw ClassCastException when specify the wrong class type" in new WithCursor {
      cursor.moveToNext()
      val string = resultSet.getObject(
        columnString,
        classOf[Integer]) must throwA[ClassCastException]
    }

    "throw SQLException when the value is null" in new WithCursor {
      cursor.moveToNext()
      val string = resultSet.getObject(
        columnNull,
        classOf[Integer]) must throwA[SQLException]
    }

  }

  "getNCharacterStream" should {

    "throw a SQLFeatureNotSupportedException" in new WithCursorMocked {
      resultSet.getNCharacterStream(1) must throwA[SQLFeatureNotSupportedException]
    }

    "throw a SQLFeatureNotSupportedException when pass a column name" in new WithCursorMocked {
      resultSet.getNCharacterStream("") must throwA[SQLFeatureNotSupportedException]
    }

  }

  "getRef" should {

    "throw a SQLFeatureNotSupportedException" in new WithCursorMocked {
      resultSet.getRef(1) must throwA[SQLFeatureNotSupportedException]
    }

    "throw a SQLFeatureNotSupportedException when pass a column name" in new WithCursorMocked {
      resultSet.getRef("") must throwA[SQLFeatureNotSupportedException]
    }

  }

  "getBlob" should {

    "return a right Blob when the data is stored in a byte array" in
      new WithCursor {
        cursor.moveToNext()
        val value = new String(rows.head(columnBytes - 1).asInstanceOf[SArray[Byte]])
        inputStreamToString(resultSet.getBlob(columnBytes).getBinaryStream) shouldEqual value
      }

    "return a right Blob when the data is stored in a byte array and pass the column name" in
      new WithCursor {
        cursor.moveToNext()
        val value = new String(rows.head(columnBytes - 1).asInstanceOf[SArray[Byte]])
        inputStreamToString(resultSet.getBlob(columnNames(columnBytes - 1)).getBinaryStream) shouldEqual value
      }

    "return null when the field is null" in
      new WithCursor {
        cursor.moveToNext()
        resultSet.getBlob(columnNull) must beNull
      }

    "throw SQLException when the cursor is closed" in
      new WithEmptyCursor {
        cursor.close()
        resultSet.getBlob(columnBytes) must throwA[SQLException]
      }

  }

  "getDate" should {

    "return a right Date when is stored in an number" in
      new WithCursor {
        val date = new java.sql.Date(rows.head(columnInteger - 1).asInstanceOf[Int])
        cursor.moveToNext()
        resultSet.getDate(columnInteger) shouldEqual date
      }

    "return a right Date when is stored in a string with valid format" in
      new WithEmptyCursor {
        val date = new java.sql.Date(Random.nextInt(100).toLong)
        val row = Seq[Any](formatDateOrThrow(new Date(date.getTime)))

        val c = new CursorSeq(Seq("column"), Seq(row))
        val rs = new ResultSet(c, new TestLogWrapper)

        c.moveToNext()
        rs.getDate(1) shouldEqual date
      }

    "return a right Date when is stored in an number and pass the column name" in
      new WithCursor {
        val date = new java.sql.Date(rows.head(columnInteger - 1).asInstanceOf[Int])
        cursor.moveToNext()
        resultSet.getDate(columnNames(columnInteger - 1)) shouldEqual date
      }

    "return a right Date when is stored in a string with valid format and pass the column name" in
      new WithEmptyCursor {
        val date = new java.sql.Date(Random.nextInt(100).toLong)
        val row = Seq[Any](formatDateOrThrow(new Date(date.getTime)))
        val column = "column"
        val c = new CursorSeq(Seq(column), Seq(row))
        val rs = new ResultSet(c, new TestLogWrapper)

        c.moveToNext()
        rs.getDate(column) shouldEqual date
      }

    "return null when the field is null" in
      new WithCursor {
        cursor.moveToNext()
        resultSet.getDate(columnNull) must beNull
      }

    "throw SQLException when the cursor is closed" in
      new WithEmptyCursor {
        cursor.close()
        resultSet.getDate(1) must throwA[SQLException]
      }

  }

  "getSQLXML" should {

    "throw a SQLFeatureNotSupportedException" in new WithCursorMocked {
      resultSet.getSQLXML(1) must throwA[SQLFeatureNotSupportedException]
    }

    "throw a SQLFeatureNotSupportedException when pass a column name" in new WithCursorMocked {
      resultSet.getSQLXML("") must throwA[SQLFeatureNotSupportedException]
    }

  }

  "getUnicodeStream" should {

    "throw a SQLFeatureNotSupportedException" in new WithCursorMocked {
      resultSet.getUnicodeStream(1) must throwA[SQLFeatureNotSupportedException]
    }

    "throw a SQLFeatureNotSupportedException when pass a column name" in new WithCursorMocked {
      resultSet.getUnicodeStream("") must throwA[SQLFeatureNotSupportedException]
    }

  }

  "getInt" should {

    "return a right Int when the value is stored in a Integer" in
      new WithCursor {
        cursor.moveToNext()
        resultSet.getInt(columnInteger) shouldEqual rows.head(columnInteger - 1)
      }

    "return a right Int when the value is stored in a Integer and pass the column name" in
      new WithCursor {
        cursor.moveToNext()
        resultSet.getInt(columnNames(columnInteger - 1)) shouldEqual rows.head(columnInteger - 1)
      }

    "return 0 when the field is null" in
      new WithCursor {
        cursor.moveToNext()
        resultSet.getInt(columnNull) shouldEqual 0
      }

    "throw SQLException when the cursor is closed" in
      new WithEmptyCursor {
        cursor.close()
        resultSet.getInt(columnInteger) must throwA[SQLException]
      }

  }

  "getBytes" should {

    "return a right byte array when the value is stored in byte array" in
      new WithCursor {
        cursor.moveToNext()
        resultSet.getBytes(columnBytes) shouldEqual rows.head(columnBytes - 1)
      }

    "return a right byte array when the value is stored in byte array and pass the column name" in
      new WithCursor {
        cursor.moveToNext()
        resultSet.getBytes(columnNames(columnBytes - 1)) shouldEqual rows.head(columnBytes - 1)
      }

    "return null when the field is null" in
      new WithCursor {
        cursor.moveToNext()
        resultSet.getBytes(columnNull) must beNull
      }

    "throw SQLException when the cursor is closed" in
      new WithEmptyCursor {
        cursor.close()
        resultSet.getBytes(columnBytes) must throwA[SQLException]
      }

  }

  "getHoldability" should {

    "return ResultSet.CLOSE_CURSORS_AT_COMMIT on a valid cursor" in new WithCursorMocked {
      resultSet.getHoldability shouldEqual SQLResultSet.CLOSE_CURSORS_AT_COMMIT
    }

    "throw SQLException when the cursor is closed" in
      new WithEmptyCursor {
        cursor.close()
        resultSet.getHoldability must throwA[SQLException]
      }

  }

  "getConcurrency" should {

    "return ResultSet.CONCUR_READ_ONLY on a valid cursor" in new WithCursorMocked {
      resultSet.getConcurrency shouldEqual SQLResultSet.CONCUR_READ_ONLY
    }

    "throw SQLException when the cursor is closed" in
      new WithEmptyCursor {
        cursor.close()
        resultSet.getConcurrency must throwA[SQLException]
      }

  }

  "setFetchSize" should {

    "call to notImplemented on log" in new WithCursorAndLogMocked {
      resultSet.setFetchSize(0)
      there was one(logger).notImplemented(Unit)
    }

    "throw SQLException when the cursor is closed" in
      new WithEmptyCursor {
        cursor.close()
        resultSet.setFetchSize(0) must throwA[SQLException]
      }

  }

  "getFetchSize" should {

    "call to notImplemented on log" in new WithCursorAndLogMocked {
      resultSet.getFetchSize
      there was one(logger).notImplemented(0)
    }

    "throw SQLException when the cursor is closed" in
      new WithEmptyCursor {
        cursor.close()
        resultSet.getFetchSize must throwA[SQLException]
      }

  }

  "setFetchDirection" should {

    "call to notImplemented on log" in new WithCursorAndLogMocked {
      resultSet.setFetchDirection(0)
      there was one(logger).notImplemented(Unit)
    }

    "throw SQLException when the cursor is closed" in
      new WithEmptyCursor {
        cursor.close()
        resultSet.setFetchDirection(0) must throwA[SQLException]
      }

  }

  "getFetchDirection" should {

    "return ResultSet.FETCH_FORWARD on a valid cursor" in new WithCursorMocked {
      resultSet.getFetchDirection shouldEqual SQLResultSet.FETCH_FORWARD
    }

    "throw SQLException when the cursor is closed" in
      new WithEmptyCursor {
        cursor.close()
        resultSet.getFetchDirection must throwA[SQLException]
      }

  }

  "getCursorName" should {

    "throw a SQLFeatureNotSupportedException" in new WithCursorMocked {
      resultSet.getCursorName must throwA[SQLFeatureNotSupportedException]
    }

  }

  "getStatement" should {

    "return null with a valid cursor" in new WithCursorMocked {
      resultSet.getStatement must beNull
    }

  }

}
