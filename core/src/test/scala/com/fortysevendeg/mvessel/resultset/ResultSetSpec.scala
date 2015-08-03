package com.fortysevendeg.mvessel.resultset

import java.sql.{ResultSet => SQLResultSet, SQLException}

import com.fortysevendeg.mvessel._

import scala.util.Random

class ResultSetSpec
  extends ResultSetSpecification {

  "isBeforeFirst" should {

    "returns true if the cursor is before to the first" in new WithMatrixCursor with WithData {
      resultSet.isBeforeFirst must beTrue
    }

    "returns false if the cursor is in the first position" in new WithMatrixCursor with WithData {
      cursor.moveToNext()
      resultSet.isBeforeFirst must beFalse
    }

    "returns false if the cursor is after last" in new WithMatrixCursor with WithData {
      cursor.moveToLast()
      cursor.moveToNext()
      resultSet.isBeforeFirst must beFalse
    }

    "throws SQLException if the cursor is closed" in new WithMatrixCursor with WithData {
      cursor.close()
      resultSet.isBeforeFirst must throwA[SQLException]
    }
  }

  "isAfterLast" should {

    "returns false if the cursor is before to the first" in new WithMatrixCursor with WithData {
      resultSet.isAfterLast must beFalse
    }

    "returns false if the cursor is in the first position" in new WithMatrixCursor with WithData {
      cursor.moveToNext()
      resultSet.isAfterLast must beFalse
    }

    "returns true if the cursor is after last" in new WithMatrixCursor with WithData {
      cursor.moveToLast()
      cursor.moveToNext()
      resultSet.isAfterLast must beTrue
    }

    "throws SQLException if the cursor is closed" in new WithMatrixCursor with WithData {
      cursor.close()
      resultSet.isAfterLast must throwA[SQLException]
    }

  }

  "isFirst" should {

    "returns false if the cursor is before to the first" in new WithMatrixCursor with WithData {
      resultSet.isFirst must beFalse
    }

    "returns true if the cursor is in the first position" in new WithMatrixCursor with WithData {
      cursor.moveToNext()
      resultSet.isFirst must beTrue
    }

    "returns true if the cursor is in the second position" in new WithMatrixCursor with WithData {
      cursor.moveToNext()
      cursor.moveToNext()
      resultSet.isFirst must beFalse
    }

    "returns false if the cursor is after last" in new WithMatrixCursor with WithData {
      cursor.moveToLast()
      cursor.moveToNext()
      resultSet.isFirst must beFalse
    }

    "throws SQLException if the cursor is closed" in new WithMatrixCursor with WithData {
      cursor.close()
      resultSet.isFirst must throwA[SQLException]
    }
  }

  "isLast" should {

    "returns false if the cursor is before to the first" in new WithMatrixCursor with WithData {
      resultSet.isLast must beFalse
    }

    "returns false if the cursor is in the first position" in new WithMatrixCursor with WithData {
      cursor.moveToNext()
      resultSet.isLast must beFalse
    }

    "returns true if the cursor is in the last position" in new WithMatrixCursor with WithData {
      cursor.moveToLast()
      resultSet.isLast must beTrue
    }

    "returns false if the cursor is after last" in new WithMatrixCursor with WithData {
      cursor.moveToLast()
      cursor.moveToNext()
      resultSet.isLast must beFalse
    }

    "throws SQLException if the cursor is closed" in new WithMatrixCursor with WithData {
      cursor.close()
      resultSet.isLast must throwA[SQLException]
    }
  }

  "absolute" should {

    "returns false for an empty cursor" in new WithMatrixCursor {
      resultSet.absolute(1) must beFalse
    }

    "returns false and move before first when passing 0" in new WithMatrixCursor with WithData {
      resultSet.absolute(0) must beFalse
      cursor.isBeforeFirst must beTrue
    }

    "returns false when move to a position greater than number of rows" in new WithMatrixCursor with WithData {
      resultSet.absolute(numRows + 1) must beFalse
    }

    "returns true and move to the first position when passing 1" in new WithMatrixCursor with WithData {
      resultSet.absolute(1) must beTrue
      cursor.isFirst must beTrue
    }

    "returns false when move to a negative position whose absolute value is greater than number of rows" in new WithMatrixCursor with WithData {
      resultSet.absolute(-numRows - 1) must beFalse
    }

    "returns true and move to the last position when passing -1" in new WithMatrixCursor with WithData {
      resultSet.absolute(-1) must beTrue
      cursor.isLast must beTrue
    }

    "throws SQLException if the cursor is closed" in new WithMatrixCursor with WithData {
      cursor.close()
      resultSet.absolute(0) must throwA[SQLException]
    }

  }

  "relative" should {

    "returns false for an empty cursor" in new WithMatrixCursor {
      resultSet.relative(1) must beFalse
    }

      "returns true and move to previous position when passing -1" in new WithMatrixCursor with WithData {
      cursor.moveToNext()
      val position = cursor.getPosition
      cursor.moveToNext()
      resultSet.relative(-1) must beTrue
      cursor.getPosition shouldEqual position
    }

    "returns false when move to a position greater than number of rows" in new WithMatrixCursor with WithData {
      cursor.moveToNext()
      resultSet.relative(numRows + 1) must beFalse
    }

    "returns true and move to the first position when passing 1" in new WithMatrixCursor with WithData {
      resultSet.relative(1) must beTrue
      cursor.isFirst must beTrue
    }

    "returns false when move to a negative position whose absolute value is greater than number of rows" in new WithMatrixCursor with WithData {
      resultSet.relative(-numRows - 1) must beFalse
    }

    "returns true and move to the previous position when passing -1" in new WithMatrixCursor with WithData {
      cursor.moveToNext()
      cursor.moveToNext()
      val position = cursor.getPosition
      cursor.moveToNext()
      resultSet.relative(-1) must beTrue
      cursor.getPosition shouldEqual position
    }

    "throws SQLException if the cursor is closed" in new WithMatrixCursor with WithData {
      cursor.close()
      resultSet.relative(0) must throwA[SQLException]
    }

  }

  "beforeFirst" should {

    "change the position to -1 with an empty cursor" in new WithMatrixCursor {
      resultSet.beforeFirst()
      cursor.getPosition shouldEqual -1
    }

    "change the position to -1 if the cursor is before to the first" in new WithMatrixCursor with WithData {
      resultSet.beforeFirst()
      cursor.getPosition shouldEqual -1
    }

    "change the position to -1 if the cursor is in the first position" in new WithMatrixCursor {
      cursor.moveToNext()
      resultSet.beforeFirst()
      cursor.getPosition shouldEqual -1
    }

    "throws SQLException if the cursor is closed" in new WithMatrixCursor with WithData {
      cursor.close()
      resultSet.beforeFirst() must throwA[SQLException]
    }

  }

  "afterLast" should {

    "don't change the position with an empty cursor" in new WithMatrixCursor {
      val position = cursor.getPosition
      resultSet.afterLast()
      cursor.getPosition shouldEqual position
    }

    "change the position to num rows with a cursor with data" in new WithMatrixCursor with WithData {
      resultSet.afterLast()
      cursor.getPosition shouldEqual numRows
    }

    "throws SQLException if the cursor is closed" in new WithMatrixCursor with WithData {
      cursor.close()
      resultSet.afterLast() must throwA[SQLException]
    }

  }

  "next" should {

    "returns true and increment position by one with a cursor with data" in new WithMatrixCursor with WithData {
      val position = cursor.getPosition
      resultSet.next() must beTrue
      cursor.getPosition shouldEqual (position + 1)
    }

    "throws SQLException if the cursor is closed" in new WithMatrixCursor with WithData {
      cursor.close()
      resultSet.next() must throwA[SQLException]
    }

  }

  "previous" should {

    "returns true and decrement position by one with a cursor with data" in new WithMatrixCursor with WithData {
      cursor.moveToNext()
      cursor.moveToNext()
      val position = cursor.getPosition
      resultSet.previous() must beTrue
      cursor.getPosition shouldEqual (position - 1)
    }

    "throws SQLException if the cursor is closed" in new WithMatrixCursor with WithData {
      cursor.close()
      resultSet.previous() must throwA[SQLException]
    }

  }

  "last" should {

    "returns true and don't change the position with an empty cursor" in new WithMatrixCursor {
      val position = cursor.getPosition
      resultSet.last() must beFalse
      cursor.getPosition shouldEqual position
    }

    "change the position to num rows minus 1 with a cursor with data" in new WithMatrixCursor with WithData {
      resultSet.last()
      cursor.getPosition shouldEqual numRows - 1
    }

    "throws SQLException if the cursor is closed" in new WithMatrixCursor with WithData {
      cursor.close()
      resultSet.last() must throwA[SQLException]
    }

  }

  "first" should {

    "returns false with an empty cursor" in new WithMatrixCursor {
      resultSet.first() must beFalse
    }

    "change the position to 0 with a cursor with data" in new WithMatrixCursor with WithData {
      resultSet.first()
      cursor.getPosition shouldEqual 0
    }

    "throws SQLException if the cursor is closed" in new WithMatrixCursor with WithData {
      cursor.close()
      resultSet.first() must throwA[SQLException]
    }

  }

  "getRow" should {

    "returns position plus 1" in new WithMatrixCursor with WithData {
      cursor.moveToNext()
      val position = cursor.getPosition
      resultSet.getRow shouldEqual position + 1
    }

    "throws SQLException if the cursor is closed" in new WithMatrixCursor with WithData {
      cursor.close()
      resultSet.getRow must throwA[SQLException]
    }

  }

  "findColumn" should {

    "returns the column position for an existing one" in new WithMatrixCursor {
      resultSet.findColumn(columnNames(1)) shouldEqual 2
    }

    "throws SQLException if the cursor is closed" in new WithMatrixCursor with WithData {
      cursor.close()
      resultSet.findColumn(columnNames(1)) must throwA[SQLException]
    }

    "throws SQLException if the column doesn't exists" in new WithMatrixCursor with WithData {
      cursor.close()
      resultSet.findColumn(nonExistentColumnName) must throwA[SQLException]
    }

  }

  "close" should {

    "call close on cursor" in new WithCursorMocked {
      resultSet.close()
      there was one(cursor).close()
    }

  }

  "isClosed" should {

    "returns true if close method in cursor return true" in new WithCursorMocked {
      cursor.isClosed returns true
      resultSet.isClosed must beTrue
    }

    "returns false if close method in cursor return false" in new WithCursorMocked {
      cursor.isClosed returns false
      resultSet.isClosed must beFalse
    }

  }

  "wasNull" should {

    "returns true if the last fetch value was null" in new WithMatrixCursor {
      cursor.addRow(Array[AnyRef](
        javaNull,
        new java.lang.Integer(Random.nextInt(10)),
        new java.lang.Float(Random.nextFloat() * Random.nextInt(10)),
        Random.nextString(10),
        javaNull))
      cursor.moveToNext()
      resultSet.getString(1)
      resultSet.wasNull() must beTrue
    }

    "returns false if the last fetch value wasn't null" in new WithMatrixCursor with WithData {
      cursor.moveToNext()
      resultSet.getString(1)
      resultSet.wasNull() must beFalse
    }

  }

  "getType" should {

    "returns ResultSet.TYPE_SCROLL_SENSITIVE" in new WithCursorMocked {
      resultSet.getType shouldEqual SQLResultSet.TYPE_SCROLL_SENSITIVE
    }

  }

  "getMetaData" should {

    "returns a ResultSetMetaData" in new WithCursorMocked {
      resultSet.getMetaData must beAnInstanceOf[ResultSetMetaData]
    }

  }

}
