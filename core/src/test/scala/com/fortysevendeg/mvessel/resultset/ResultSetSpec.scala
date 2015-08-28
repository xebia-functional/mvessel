package com.fortysevendeg.mvessel.resultset

import java.sql.{ResultSet => SQLResultSet, SQLException}

class ResultSetSpec
  extends ResultSetSpecification {

  "isBeforeFirst" should {

    "returns true if the cursor is before to the first" in new WithCursor {
      resultSet.isBeforeFirst must beTrue
    }

    "returns false if the cursor is in the first position" in new WithCursor {
      cursor.moveToNext()
      resultSet.isBeforeFirst must beFalse
    }

    "returns false if the cursor is after last" in new WithCursor {
      cursor.moveToLast()
      cursor.moveToNext()
      resultSet.isBeforeFirst must beFalse
    }

    "throws SQLException if the cursor is closed" in new WithCursor {
      cursor.close()
      resultSet.isBeforeFirst must throwA[SQLException]
    }
  }

  "isAfterLast" should {

    "returns false if the cursor is before to the first" in new WithCursor {
      resultSet.isAfterLast must beFalse
    }

    "returns false if the cursor is in the first position" in new WithCursor {
      cursor.moveToNext()
      resultSet.isAfterLast must beFalse
    }

    "returns true if the cursor is after last" in new WithCursor {
      cursor.moveToLast()
      cursor.moveToNext()
      resultSet.isAfterLast must beTrue
    }

    "throws SQLException if the cursor is closed" in new WithCursor {
      cursor.close()
      resultSet.isAfterLast must throwA[SQLException]
    }

  }

  "isFirst" should {

    "returns false if the cursor is before to the first" in new WithCursor {
      resultSet.isFirst must beFalse
    }

    "returns true if the cursor is in the first position" in new WithCursor {
      cursor.moveToNext()
      resultSet.isFirst must beTrue
    }

    "returns true if the cursor is in the second position" in new WithCursor {
      cursor.moveToNext()
      cursor.moveToNext()
      resultSet.isFirst must beFalse
    }

    "returns false if the cursor is after last" in new WithCursor {
      cursor.moveToLast()
      cursor.moveToNext()
      resultSet.isFirst must beFalse
    }

    "throws SQLException if the cursor is closed" in new WithCursor {
      cursor.close()
      resultSet.isFirst must throwA[SQLException]
    }
  }

  "isLast" should {

    "returns false if the cursor is before to the first" in new WithCursor {
      resultSet.isLast must beFalse
    }

    "returns false if the cursor is in the first position" in new WithCursor {
      cursor.moveToNext()
      resultSet.isLast must beFalse
    }

    "returns true if the cursor is in the last position" in new WithCursor {
      cursor.moveToLast()
      resultSet.isLast must beTrue
    }

    "returns false if the cursor is after last" in new WithCursor {
      cursor.moveToLast()
      cursor.moveToNext()
      resultSet.isLast must beFalse
    }

    "throws SQLException if the cursor is closed" in new WithCursor {
      cursor.close()
      resultSet.isLast must throwA[SQLException]
    }
  }

  "absolute" should {

    "returns false for an empty cursor" in new WithEmptyCursor {
      resultSet.absolute(1) must beFalse
    }

    "returns false and move before first when passing 0" in new WithCursor {
      resultSet.absolute(0) must beFalse
      cursor.isBeforeFirst must beTrue
    }

    "returns false when move to a position greater than number of rows" in new WithCursor {
      resultSet.absolute(numRows + 1) must beFalse
    }

    "returns true and move to the first position when passing 1" in new WithCursor {
      resultSet.absolute(1) must beTrue
      cursor.isFirst must beTrue
    }

    "returns false when move to a negative position whose absolute value is greater than number of rows" in new WithCursor {
      resultSet.absolute(-numRows - 1) must beFalse
    }

    "returns true and move to the last position when passing -1" in new WithCursor {
      resultSet.absolute(-1) must beTrue
      cursor.isLast must beTrue
    }

    "throws SQLException if the cursor is closed" in new WithCursor {
      cursor.close()
      resultSet.absolute(0) must throwA[SQLException]
    }

  }

  "relative" should {

    "returns false for an empty cursor" in new WithEmptyCursor {
      resultSet.relative(1) must beFalse
    }

      "returns true and move to previous position when passing -1" in new WithCursor {
      cursor.moveToNext()
      val position = cursor.getPosition
      cursor.moveToNext()
      resultSet.relative(-1) must beTrue
      cursor.getPosition shouldEqual position
    }

    "returns false when move to a position greater than number of rows" in new WithCursor {
      cursor.moveToNext()
      resultSet.relative(numRows + 1) must beFalse
    }

    "returns true and move to the first position when passing 1" in new WithCursor {
      resultSet.relative(1) must beTrue
      cursor.isFirst must beTrue
    }

    "returns false when move to a negative position whose absolute value is greater than number of rows" in new WithCursor {
      resultSet.relative(-numRows - 1) must beFalse
    }

    "returns true and move to the previous position when passing -1" in new WithCursor {
      cursor.moveToNext()
      cursor.moveToNext()
      val position = cursor.getPosition
      cursor.moveToNext()
      resultSet.relative(-1) must beTrue
      cursor.getPosition shouldEqual position
    }

    "throws SQLException if the cursor is closed" in new WithCursor {
      cursor.close()
      resultSet.relative(0) must throwA[SQLException]
    }

  }

  "beforeFirst" should {

    "change the position to -1 with an empty cursor" in new WithEmptyCursor {
      resultSet.beforeFirst()
      cursor.getPosition shouldEqual -1
    }

    "change the position to -1 if the cursor is before to the first" in new WithCursor {
      resultSet.beforeFirst()
      cursor.getPosition shouldEqual -1
    }

    "change the position to -1 if the cursor is in the first position" in new WithEmptyCursor {
      cursor.moveToNext()
      resultSet.beforeFirst()
      cursor.getPosition shouldEqual -1
    }

    "throws SQLException if the cursor is closed" in new WithCursor {
      cursor.close()
      resultSet.beforeFirst() must throwA[SQLException]
    }

  }

  "afterLast" should {

    "don't change the position with an empty cursor" in new WithEmptyCursor {
      val position = cursor.getPosition
      resultSet.afterLast()
      cursor.getPosition shouldEqual position
    }

    "change the position to num rows with a cursor with data" in new WithCursor {
      resultSet.afterLast()
      cursor.getPosition shouldEqual numRows
    }

    "throws SQLException if the cursor is closed" in new WithCursor {
      cursor.close()
      resultSet.afterLast() must throwA[SQLException]
    }

  }

  "next" should {

    "returns true and increment position by one with a cursor with data" in new WithCursor {
      val position = cursor.getPosition
      resultSet.next() must beTrue
      cursor.getPosition shouldEqual (position + 1)
    }

    "throws SQLException if the cursor is closed" in new WithCursor {
      cursor.close()
      resultSet.next() must throwA[SQLException]
    }

  }

  "previous" should {

    "returns true and decrement position by one with a cursor with data" in new WithCursor {
      cursor.moveToNext()
      cursor.moveToNext()
      val position = cursor.getPosition
      resultSet.previous() must beTrue
      cursor.getPosition shouldEqual (position - 1)
    }

    "throws SQLException if the cursor is closed" in new WithCursor {
      cursor.close()
      resultSet.previous() must throwA[SQLException]
    }

  }

  "last" should {

    "returns false and don't change the position with an empty cursor" in new WithEmptyCursor {
      val position = cursor.getPosition
      resultSet.last() must beFalse
      cursor.getPosition shouldEqual position
    }

    "change the position to num rows minus 1 with a cursor with data" in new WithCursor {
      resultSet.last()
      cursor.getPosition shouldEqual numRows - 1
    }

    "throws SQLException if the cursor is closed" in new WithCursor {
      cursor.close()
      resultSet.last() must throwA[SQLException]
    }

  }

  "first" should {

    "returns false with an empty cursor" in new WithEmptyCursor {
      resultSet.first() must beFalse
    }

    "change the position to 0 with a cursor with data" in new WithCursor {
      resultSet.first()
      cursor.getPosition shouldEqual 0
    }

    "throws SQLException if the cursor is closed" in new WithCursor {
      cursor.close()
      resultSet.first() must throwA[SQLException]
    }

  }

  "getRow" should {

    "returns position plus 1" in new WithCursor {
      cursor.moveToNext()
      val position = cursor.getPosition
      resultSet.getRow shouldEqual position + 1
    }

    "throws SQLException if the cursor is closed" in new WithCursor {
      cursor.close()
      resultSet.getRow must throwA[SQLException]
    }

  }

  "findColumn" should {

    "returns the column position for an existing one" in new WithEmptyCursor {
      resultSet.findColumn(columnNames(1)) shouldEqual 2
    }

    "throws SQLException if the cursor is closed" in new WithCursor {
      cursor.close()
      resultSet.findColumn(columnNames(1)) must throwA[SQLException]
    }

    "throws SQLException if the column doesn't exists" in new WithCursor {
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

    "returns true if the last fetch value was null" in new WithCursor {
      cursor.moveToNext()
      resultSet.getString(columnNull)
      resultSet.wasNull() must beTrue
    }

    "returns false if the last fetch value wasn't null" in new WithCursor {
      cursor.moveToNext()
      resultSet.getString(columnString)
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
