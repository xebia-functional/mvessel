package com.fortysevendeg.android.sqlite

import java.sql.{ResultSetMetaData, Types}

import android.database.{MatrixCursor, Cursor}
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.specification.Scope

import scala.util.Random

trait SQLDroidResultSetMetaDataSpecification
  extends Specification
  with Mockito {

  trait WithCursorMocked
    extends Scope {

    val cursor = mock[Cursor]

    val sqlDroid = new SQLDroidResultSetMetaData(cursor, new TestLogWrapper)

  }

  trait WithMatrixCursor
    extends Scope {

    val columnNames = Array("column1", "column2", "column3")

    val columnTypes = Array(Cursor.FIELD_TYPE_STRING, Cursor.FIELD_TYPE_INTEGER, Cursor.FIELD_TYPE_FLOAT)

    val cursor = new MatrixCursor(columnNames, columnTypes)

    val sqlDroid = new SQLDroidResultSetMetaData(cursor, new TestLogWrapper)

  }

}

class SQLDroidResultSetMetaDataSpec
  extends SQLDroidResultSetMetaDataSpecification {

  "getCatalogName" should {

    "return an empty string" in new WithCursorMocked {
      sqlDroid.getCatalogName(Random.nextInt(10)) shouldEqual ""
    }
  }

  "getSchemaName" should {

    "return an empty string" in new WithCursorMocked {
      sqlDroid.getSchemaName(Random.nextInt(10)) shouldEqual ""
    }
  }

  "getTableName" should {

    "return an empty string" in new WithCursorMocked {
      sqlDroid.getTableName(Random.nextInt(10)) shouldEqual ""
    }

  }

  "getColumnCount" should {

    "return the value returned by getColumnCount in cursor" in new WithCursorMocked {
      val count = Random.nextInt(10)

      cursor.getColumnCount returns count

      sqlDroid.getColumnCount shouldEqual count
    }

  }

  "getColumnName" should {

    "return the value returned by getColumnName in cursor with right column index" in new WithCursorMocked {
      val index = Random.nextInt(10)

      val name = Random.nextString(10)

      cursor.getColumnName(index) returns name

      sqlDroid.getColumnName(index + 1) shouldEqual name
    }

  }

  "getColumnLabel" should {

    "return the value returned by getColumnName in cursor with right column index" in new WithCursorMocked {
      val index = Random.nextInt(10)

      val name = Random.nextString(10)

      cursor.getColumnName(index) returns name

      sqlDroid.getColumnLabel(index + 1) shouldEqual name
    }

  }

  "getColumnType" should {

    "return type NULL when the cursor is empty" in new WithMatrixCursor {
      sqlDroid.getColumnType(1) shouldEqual Types.NULL
    }

    "return type VARCHAR for first column when the cursor has data and positioned on first row" in new WithMatrixCursor {
      cursor.addRow(Array("value1", "value2", "value3"))
      cursor.moveToNext()
      sqlDroid.getColumnType(1) shouldEqual Types.VARCHAR
    }

    "return type INTEGER for second column when the cursor has data and positioned on first row" in new WithMatrixCursor {
      cursor.addRow(Array("value1", "value2", "value3"))
      cursor.moveToNext()
      sqlDroid.getColumnType(2) shouldEqual Types.INTEGER
    }

    "return type FLOAT for third column when the cursor has data and positioned on first row" in new WithMatrixCursor {
      cursor.addRow(Array("value1", "value2", "value3"))
      cursor.moveToNext()
      sqlDroid.getColumnType(3) shouldEqual Types.FLOAT
    }

    "return type VARCHAR and restore position when the cursor has data but has been read" in new WithMatrixCursor {
      cursor.addRow(Array("value1", "value2", "value3"))
      cursor.moveToNext()
      cursor.moveToNext()
      sqlDroid.getColumnType(1) shouldEqual Types.VARCHAR
      cursor.getPosition shouldEqual 1
    }

    "return type VARCHAR and restore position when the cursor is unread" in new WithMatrixCursor {
      cursor.addRow(Array("value1", "value2", "value3"))
      sqlDroid.getColumnType(1) shouldEqual Types.VARCHAR
      cursor.getPosition shouldEqual -1
    }

  }

  "getColumnTypeName" should {

    "return name NULL when the cursor is empty" in new WithMatrixCursor {
      sqlDroid.getColumnTypeName(1) shouldEqual "NULL"
    }

    "return name TEXT when the cursor has data and positioned on first row" in new WithMatrixCursor {
      cursor.addRow(Array("value1", "value2", "value3"))
      cursor.moveToNext()
      sqlDroid.getColumnTypeName(1) shouldEqual "TEXT"
    }

    "return type VARCHAR and restore position when the cursor has data but has been read" in new WithMatrixCursor {
      cursor.addRow(Array("value1", "value2", "value3"))
      cursor.moveToNext()
      cursor.moveToNext()
      sqlDroid.getColumnTypeName(1) shouldEqual "TEXT"
      cursor.getPosition shouldEqual 1
    }

    "return type VARCHAR and restore position when the cursor is unread" in new WithMatrixCursor {
      cursor.addRow(Array("value1", "value2", "value3"))
      sqlDroid.getColumnTypeName(1) shouldEqual "TEXT"
      cursor.getPosition shouldEqual -1
    }

  }

  "getColumnDisplaySize" should {

    "throws an UnsupportedOperationException" in new WithCursorMocked {
      sqlDroid.getColumnDisplaySize(Random.nextInt(10)) must throwA[UnsupportedOperationException]
    }

  }

  "getColumnClassName" should {

    "throws an UnsupportedOperationException" in new WithCursorMocked {
      sqlDroid.getColumnClassName(Random.nextInt(10)) must throwA[UnsupportedOperationException]
    }

  }

  "getPrecision" should {

    "returns 0" in new WithCursorMocked {
      sqlDroid.getPrecision(Random.nextInt(10)) shouldEqual 0
    }

  }

  "getScale" should {

    "returns 0" in new WithCursorMocked {
      sqlDroid.getScale(Random.nextInt(10)) shouldEqual 0
    }

  }

  "isDefinitelyWritable" should {

    "returns false" in new WithCursorMocked {
      sqlDroid.isDefinitelyWritable(Random.nextInt(10)) shouldEqual false
    }

  }

  "isCurrency" should {

    "returns false" in new WithCursorMocked {
      sqlDroid.isCurrency(Random.nextInt(10)) shouldEqual false
    }

  }

  "isCaseSensitive" should {

    "returns false" in new WithCursorMocked {
      sqlDroid.isCaseSensitive(Random.nextInt(10)) shouldEqual false
    }

  }

  "isSearchable" should {

    "returns false" in new WithCursorMocked {
      sqlDroid.isSearchable(Random.nextInt(10)) shouldEqual false
    }

  }

  "isReadOnly" should {

    "returns false" in new WithCursorMocked {
      sqlDroid.isReadOnly(Random.nextInt(10)) shouldEqual false
    }

  }

  "isWritable" should {

    "returns false" in new WithCursorMocked {
      sqlDroid.isWritable(Random.nextInt(10)) shouldEqual false
    }

  }

  "isNullable" should {

    "returns columnNullableUnknown" in new WithCursorMocked {
      sqlDroid.isNullable(Random.nextInt(10)) shouldEqual ResultSetMetaData.columnNullableUnknown
    }

  }

  "isSigned" should {

    "returns false when the column type is VARCHAR" in new WithMatrixCursor {
      cursor.addRow(Array("value1", "value2", "value3"))
      sqlDroid.isSigned(1) shouldEqual false
    }

    "returns true when the column type is INTEGER" in new WithMatrixCursor {
      cursor.addRow(Array("value1", "value2", "value3"))
      sqlDroid.isSigned(2) shouldEqual true
    }

    "returns true when the column type is FLOAT" in new WithMatrixCursor {
      cursor.addRow(Array("value1", "value2", "value3"))
      sqlDroid.isSigned(3) shouldEqual true
    }

  }

  "isAutoIncrement" should {

    "throws an UnsupportedOperationException" in new WithCursorMocked {
      sqlDroid.isAutoIncrement(Random.nextInt(10)) must throwA[UnsupportedOperationException]
    }

  }

  "unwrap" should {

    "throws an UnsupportedOperationException" in new WithCursorMocked {
      sqlDroid.unwrap(classOf[String]) must throwA[UnsupportedOperationException]
    }

  }

  "isWrapperFor" should {

    "returns false" in new WithCursorMocked {
      sqlDroid.isWrapperFor(classOf[String]) shouldEqual false
    }

  }

}
