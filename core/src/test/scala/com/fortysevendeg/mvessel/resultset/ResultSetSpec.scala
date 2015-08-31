package com.fortysevendeg.mvessel.resultset

import java.sql.{ResultSet => SQLResultSet, SQLException}
import java.util.Calendar

import com.fortysevendeg.mvessel.util.StreamUtils

class ResultSetSpec
  extends ResultSetSpecification {

  "isBeforeFirst" should {

    "return true if the cursor is before to the first" in new WithCursor {
      resultSet.isBeforeFirst must beTrue
    }

    "return false if the cursor is in the first position" in new WithCursor {
      cursor.moveToNext()
      resultSet.isBeforeFirst must beFalse
    }

    "return false if the cursor is after last" in new WithCursor {
      cursor.moveToLast()
      cursor.moveToNext()
      resultSet.isBeforeFirst must beFalse
    }

    "throw SQLException if the cursor is closed" in new WithCursor {
      cursor.close()
      resultSet.isBeforeFirst must throwA[SQLException]
    }

    "throw a SQLException with a cause equal to the exception thrown by the cursor" in new WithCursorMocked {
      val e = new RuntimeException("Error")
      cursor.isBeforeFirst throws e
      resultSet.isBeforeFirst must throwA[SQLException].like {
        case ex => ex.getCause shouldEqual e
      }
    }
  }

  "isAfterLast" should {

    "return false if the cursor is before to the first" in new WithCursor {
      resultSet.isAfterLast must beFalse
    }

    "return false if the cursor is in the first position" in new WithCursor {
      cursor.moveToNext()
      resultSet.isAfterLast must beFalse
    }

    "return true if the cursor is after last" in new WithCursor {
      cursor.moveToLast()
      cursor.moveToNext()
      resultSet.isAfterLast must beTrue
    }

    "throw SQLException if the cursor is closed" in new WithCursor {
      cursor.close()
      resultSet.isAfterLast must throwA[SQLException]
    }

    "throw a SQLException with a cause equal to the exception thrown by the cursor" in new WithCursorMocked {
      val e = new RuntimeException("Error")
      cursor.isAfterLast throws e
      resultSet.isAfterLast must throwA[SQLException].like {
        case ex => ex.getCause shouldEqual e
      }
    }

  }

  "isFirst" should {

    "return false if the cursor is before to the first" in new WithCursor {
      resultSet.isFirst must beFalse
    }

    "return true if the cursor is in the first position" in new WithCursor {
      cursor.moveToNext()
      resultSet.isFirst must beTrue
    }

    "return true if the cursor is in the second position" in new WithCursor {
      cursor.moveToNext()
      cursor.moveToNext()
      resultSet.isFirst must beFalse
    }

    "return false if the cursor is after last" in new WithCursor {
      cursor.moveToLast()
      cursor.moveToNext()
      resultSet.isFirst must beFalse
    }

    "throw SQLException if the cursor is closed" in new WithCursor {
      cursor.close()
      resultSet.isFirst must throwA[SQLException]
    }

    "throw a SQLException with a cause equal to the exception thrown by the cursor" in new WithCursorMocked {
      val e = new RuntimeException("Error")
      cursor.isFirst throws e
      resultSet.isFirst must throwA[SQLException].like {
        case ex => ex.getCause shouldEqual e
      }
    }
  }

  "isLast" should {

    "return false if the cursor is before to the first" in new WithCursor {
      resultSet.isLast must beFalse
    }

    "return false if the cursor is in the first position" in new WithCursor {
      cursor.moveToNext()
      resultSet.isLast must beFalse
    }

    "return true if the cursor is in the last position" in new WithCursor {
      cursor.moveToLast()
      resultSet.isLast must beTrue
    }

    "return false if the cursor is after last" in new WithCursor {
      cursor.moveToLast()
      cursor.moveToNext()
      resultSet.isLast must beFalse
    }

    "throw SQLException if the cursor is closed" in new WithCursor {
      cursor.close()
      resultSet.isLast must throwA[SQLException]
    }

    "throw a SQLException with a cause equal to the exception thrown by the cursor" in new WithCursorMocked {
      val e = new RuntimeException("Error")
      cursor.isLast throws e
      resultSet.isLast must throwA[SQLException].like {
        case ex => ex.getCause shouldEqual e
      }
    }
  }

  "absolute" should {

    "return false for an empty cursor" in new WithEmptyCursor {
      resultSet.absolute(1) must beFalse
    }

    "return false and move before first when passing 0" in new WithCursor {
      resultSet.absolute(0) must beFalse
      cursor.isBeforeFirst must beTrue
    }

    "return false when move to a position greater than number of rows" in new WithCursor {
      resultSet.absolute(numRows + 1) must beFalse
    }

    "return true and move to the first position when passing 1" in new WithCursor {
      resultSet.absolute(1) must beTrue
      cursor.isFirst must beTrue
    }

    "return false when move to a negative position whose absolute value is greater than number of rows" in new WithCursor {
      resultSet.absolute(-numRows - 1) must beFalse
    }

    "return true and move to the last position when passing -1" in new WithCursor {
      resultSet.absolute(-1) must beTrue
      cursor.isLast must beTrue
    }

    "throw SQLException if the cursor is closed" in new WithCursor {
      cursor.close()
      resultSet.absolute(0) must throwA[SQLException]
    }

    "throw a SQLException with a cause equal to the exception thrown by the cursor" in new WithCursorMocked {
      cursor.getCount returns 10
      val e = new RuntimeException("Error")
      cursor.moveToPosition(0) throws e
      resultSet.absolute(1) must throwA[SQLException].like {
        case ex => ex.getCause shouldEqual e
      }
    }

  }

  "relative" should {

    "return false for an empty cursor" in new WithEmptyCursor {
      resultSet.relative(1) must beFalse
    }

    "return true and move to previous position when passing -1" in new WithCursor {
      cursor.moveToNext()
      val position = cursor.getPosition
      cursor.moveToNext()
      resultSet.relative(-1) must beTrue
      cursor.getPosition shouldEqual position
    }

    "return false when move to a position greater than number of rows" in new WithCursor {
      cursor.moveToNext()
      resultSet.relative(numRows + 1) must beFalse
    }

    "return true and move to the first position when passing 1" in new WithCursor {
      resultSet.relative(1) must beTrue
      cursor.isFirst must beTrue
    }

    "return false when move to a negative position whose absolute value is greater than number of rows" in new WithCursor {
      resultSet.relative(-numRows - 1) must beFalse
    }

    "return true and move to the previous position when passing -1" in new WithCursor {
      cursor.moveToNext()
      cursor.moveToNext()
      val position = cursor.getPosition
      cursor.moveToNext()
      resultSet.relative(-1) must beTrue
      cursor.getPosition shouldEqual position
    }

    "return false when passing 0" in new WithCursorMocked {
      cursor.getCount returns 10
      cursor.getPosition returns 0
      resultSet.relative(0) must beFalse
    }

    "throw SQLException if the cursor is closed" in new WithCursor {
      cursor.close()
      resultSet.relative(0) must throwA[SQLException]
    }

    "throw a SQLException with a cause equal to the exception thrown by the cursor" in new WithCursorMocked {
      cursor.getCount returns 10
      val e = new RuntimeException("Error")
      cursor.move(1) throws e
      resultSet.relative(1) must throwA[SQLException].like {
        case ex => ex.getCause shouldEqual e
      }
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

    "throw SQLException if the cursor is closed" in new WithCursor {
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

    "throw SQLException if the cursor is closed" in new WithCursor {
      cursor.close()
      resultSet.afterLast() must throwA[SQLException]
    }

  }

  "next" should {

    "return true and increment position by one with a cursor with data" in new WithCursor {
      val position = cursor.getPosition
      resultSet.next() must beTrue
      cursor.getPosition shouldEqual (position + 1)
    }

    "throw SQLException if the cursor is closed" in new WithCursor {
      cursor.close()
      resultSet.next() must throwA[SQLException]
    }

    "throw a SQLException with a cause equal to the exception thrown by the cursor" in new WithCursorMocked {
      val e = new RuntimeException("Error")
      cursor.moveToNext() throws e
      resultSet.next() must throwA[SQLException].like {
        case ex => ex.getCause shouldEqual e
      }
    }

  }

  "previous" should {

    "return true and decrement position by one with a cursor with data" in new WithCursor {
      cursor.moveToNext()
      cursor.moveToNext()
      val position = cursor.getPosition
      resultSet.previous() must beTrue
      cursor.getPosition shouldEqual (position - 1)
    }

    "throw SQLException if the cursor is closed" in new WithCursor {
      cursor.close()
      resultSet.previous() must throwA[SQLException]
    }

    "throw a SQLException with a cause equal to the exception thrown by the cursor" in new WithCursorMocked {
      val e = new RuntimeException("Error")
      cursor.moveToPrevious() throws e
      resultSet.previous() must throwA[SQLException].like {
        case ex => ex.getCause shouldEqual e
      }
    }

  }

  "last" should {

    "return false and don't change the position with an empty cursor" in new WithEmptyCursor {
      val position = cursor.getPosition
      resultSet.last() must beFalse
      cursor.getPosition shouldEqual position
    }

    "change the position to num rows minus 1 with a cursor with data" in new WithCursor {
      resultSet.last()
      cursor.getPosition shouldEqual numRows - 1
    }

    "throw SQLException if the cursor is closed" in new WithCursor {
      cursor.close()
      resultSet.last() must throwA[SQLException]
    }

    "throw a SQLException with a cause equal to the exception thrown by the cursor" in new WithCursorMocked {
      val e = new RuntimeException("Error")
      cursor.moveToLast() throws e
      resultSet.last() must throwA[SQLException].like {
        case ex => ex.getCause shouldEqual e
      }
    }

  }

  "first" should {

    "return false with an empty cursor" in new WithEmptyCursor {
      resultSet.first() must beFalse
    }

    "change the position to 0 with a cursor with data" in new WithCursor {
      resultSet.first()
      cursor.getPosition shouldEqual 0
    }

    "throw SQLException if the cursor is closed" in new WithCursor {
      cursor.close()
      resultSet.first() must throwA[SQLException]
    }

    "throw a SQLException with a cause equal to the exception thrown by the cursor" in new WithCursorMocked {
      val e = new RuntimeException("Error")
      cursor.moveToFirst() throws e
      resultSet.first() must throwA[SQLException].like {
        case ex => ex.getCause shouldEqual e
      }
    }

  }

  "getRow" should {

    "return position plus 1" in new WithCursor {
      cursor.moveToNext()
      val position = cursor.getPosition
      resultSet.getRow shouldEqual position + 1
    }

    "throw SQLException if the cursor is closed" in new WithCursor {
      cursor.close()
      resultSet.getRow must throwA[SQLException]
    }

    "throw a SQLException with a cause equal to the exception thrown by the cursor" in new WithCursorMocked {
      val e = new RuntimeException("Error")
      cursor.getPosition throws e
      resultSet.getRow() must throwA[SQLException].like {
        case ex => ex.getCause shouldEqual e
      }
    }

  }

  "findColumn" should {

    "return the column position for an existing one" in new WithEmptyCursor {
      resultSet.findColumn(columnNames(1)) shouldEqual 2
    }

    "throw SQLException if the cursor is closed" in new WithCursor {
      cursor.close()
      resultSet.findColumn(columnNames(1)) must throwA[SQLException]
    }

    "throw SQLException if the column doesn't exists" in new WithCursor {
      cursor.close()
      resultSet.findColumn(nonExistentColumnName) must throwA[SQLException]
    }

    "throw a SQLException with a cause equal to the exception thrown by the cursor" in new WithCursorMocked {
      val e = new RuntimeException("Error")
      cursor.getColumnIndexOrThrow("") throws e
      resultSet.findColumn("") must throwA[SQLException].like {
        case ex => ex.getCause shouldEqual e
      }
    }

  }

  "close" should {

    "call close on cursor" in new WithCursorMocked {
      resultSet.close()
      there was one(cursor).close()
    }

  }

  "isClosed" should {

    "return true if close method in cursor return true" in new WithCursorMocked {
      cursor.isClosed returns true
      resultSet.isClosed must beTrue
    }

    "return false if close method in cursor return false" in new WithCursorMocked {
      cursor.isClosed returns false
      resultSet.isClosed must beFalse
    }

  }

  "wasNull" should {

    "return true if the last fetch value was null" in new WithCursor {
      cursor.moveToNext()
      resultSet.getString(columnNull)
      resultSet.wasNull() must beTrue
    }

    "return false if the last fetch value wasn't null" in new WithCursor {
      cursor.moveToNext()
      resultSet.getString(columnString)
      resultSet.wasNull() must beFalse
    }

  }

  "getType" should {

    "return ResultSet.TYPE_SCROLL_SENSITIVE" in new WithCursorMocked {
      resultSet.getType shouldEqual SQLResultSet.TYPE_SCROLL_SENSITIVE
    }

  }

  "getMetaData" should {

    "return a ResultSetMetaData" in new WithCursorMocked {
      resultSet.getMetaData must beAnInstanceOf[ResultSetMetaData]
    }

  }

}
