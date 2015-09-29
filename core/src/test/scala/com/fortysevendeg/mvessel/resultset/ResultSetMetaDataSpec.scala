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

import java.sql.{ResultSetMetaData => SQLResultSetMetaData, Types}
import com.fortysevendeg.mvessel._

import scala.util.Random

class ResultSetMetaDataSpec
  extends ResultSetMetaDataSpecification {

  "getCatalogName" should {

    "returns an empty string" in new WithCursorMocked {
      resultSetMetaData.getCatalogName(Random.nextInt(10)) shouldEqual ""
    }
  }

  "getSchemaName" should {

    "returns an empty string" in new WithCursorMocked {
      resultSetMetaData.getSchemaName(Random.nextInt(10)) shouldEqual ""
    }
  }

  "getTableName" should {

    "returns an empty string" in new WithCursorMocked {
      resultSetMetaData.getTableName(Random.nextInt(10)) shouldEqual ""
    }

  }

  "getColumnCount" should {

    "returns the value returned by getColumnCount in cursor" in new WithCursorMocked {
      val count = Random.nextInt(10)

      cursor.getColumnCount returns count

      resultSetMetaData.getColumnCount shouldEqual count
    }

  }

  "getColumnName" should {

    "returns the value returned by getColumnName in cursor with right column index" in new WithCursorMocked {
      val index = Random.nextInt(10)

      val name = Random.nextString(10)

      cursor.getColumnName(index) returns name

      resultSetMetaData.getColumnName(index + 1) shouldEqual name
    }

  }

  "getColumnLabel" should {

    "returns the value returned by getColumnName in cursor with right column index" in new WithCursorMocked {
      val index = Random.nextInt(10)

      val name = Random.nextString(10)

      cursor.getColumnName(index) returns name

      resultSetMetaData.getColumnLabel(index + 1) shouldEqual name
    }

  }

  "getColumnType" should {

    "returns type NULL when the cursor is empty" in new WithEmptyCursor {
      resultSetMetaData.getColumnType(1) shouldEqual Types.NULL
    }

    "returns type VARCHAR for first column when the cursor has data and positioned on first row" in
      new WithCursorData {
      cursor.moveToNext()
      resultSetMetaData.getColumnType(1) shouldEqual Types.VARCHAR
    }

    "returns type INTEGER for second column when the cursor has data and positioned on first row" in
      new WithCursorData {
      cursor.moveToNext()
      resultSetMetaData.getColumnType(2) shouldEqual Types.INTEGER
    }

    "returns type FLOAT for third column when the cursor has data and positioned on first row" in
      new WithCursorData {
      cursor.moveToNext()
      resultSetMetaData.getColumnType(3) shouldEqual Types.FLOAT
    }

    "returns type VARCHAR and restore position when the cursor has data but has been read" in
      new WithCursorData {
      cursor.moveToNext()
      cursor.moveToNext()
      resultSetMetaData.getColumnType(1) shouldEqual Types.VARCHAR
      cursor.getPosition shouldEqual 1
    }

    "returns type VARCHAR and restore position when the cursor is unread" in
      new WithCursorData {
      resultSetMetaData.getColumnType(1) shouldEqual Types.VARCHAR
      cursor.getPosition shouldEqual -1
    }

  }

  "getColumnTypeName" should {

    "returns name NULL when the cursor is empty" in new WithEmptyCursor {
      resultSetMetaData.getColumnTypeName(1) shouldEqual nullString
    }

    "returns name TEXT when the cursor has data and positioned on first row" in
      new WithCursorData {
      cursor.moveToNext()
      resultSetMetaData.getColumnTypeName(1) shouldEqual "TEXT"
    }

    "returns type VARCHAR and restore position when the cursor has data but has been read" in
      new WithCursorData {
      cursor.moveToNext()
      cursor.moveToNext()
      resultSetMetaData.getColumnTypeName(1) shouldEqual "TEXT"
      cursor.getPosition shouldEqual 1
    }

    "returns type VARCHAR and restore position when the cursor is unread" in
      new WithCursorData {
      resultSetMetaData.getColumnTypeName(1) shouldEqual "TEXT"
      cursor.getPosition shouldEqual -1
    }

  }

  "getColumnDisplaySize" should {

    "throws an UnsupportedOperationException" in new WithCursorMocked {
      resultSetMetaData.getColumnDisplaySize(Random.nextInt(10)) must throwA[UnsupportedOperationException]
    }

  }

  "getColumnClassName" should {

    "throws an UnsupportedOperationException" in new WithCursorMocked {
      resultSetMetaData.getColumnClassName(Random.nextInt(10)) must throwA[UnsupportedOperationException]
    }

  }

  "getPrecision" should {

    "returns 0" in new WithCursorMocked {
      resultSetMetaData.getPrecision(Random.nextInt(10)) shouldEqual 0
    }

  }

  "getScale" should {

    "returns 0" in new WithCursorMocked {
      resultSetMetaData.getScale(Random.nextInt(10)) shouldEqual 0
    }

  }

  "isDefinitelyWritable" should {

    "returns false" in new WithCursorMocked {
      resultSetMetaData.isDefinitelyWritable(Random.nextInt(10)) must beFalse
    }

  }

  "isCurrency" should {

    "returns false" in new WithCursorMocked {
      resultSetMetaData.isCurrency(Random.nextInt(10)) must beFalse
    }

  }

  "isCaseSensitive" should {

    "returns false" in new WithCursorMocked {
      resultSetMetaData.isCaseSensitive(Random.nextInt(10)) must beFalse
    }

  }

  "isSearchable" should {

    "returns false" in new WithCursorMocked {
      resultSetMetaData.isSearchable(Random.nextInt(10)) must beFalse
    }

  }

  "isReadOnly" should {

    "returns false" in new WithCursorMocked {
      resultSetMetaData.isReadOnly(Random.nextInt(10)) must beFalse
    }

  }

  "isWritable" should {

    "returns false" in new WithCursorMocked {
      resultSetMetaData.isWritable(Random.nextInt(10)) must beFalse
    }

  }

  "isNullable" should {

    "returns columnNullableUnknown" in new WithCursorMocked {
      resultSetMetaData.isNullable(Random.nextInt(10)) shouldEqual
        SQLResultSetMetaData.columnNullableUnknown
    }

  }

  "isSigned" should {

    "returns false when the column type is VARCHAR" in new WithCursorData {
      resultSetMetaData.isSigned(1) must beFalse
    }

    "returns true when the column type is INTEGER" in new WithCursorData {
      resultSetMetaData.isSigned(2) must beTrue
    }

    "returns true when the column type is FLOAT" in new WithCursorData {
      resultSetMetaData.isSigned(3) must beTrue
    }

  }

  "isAutoIncrement" should {

    "throws an UnsupportedOperationException" in new WithCursorMocked {
      resultSetMetaData.isAutoIncrement(Random.nextInt(10)) must throwA[UnsupportedOperationException]
    }

  }

  "unwrap" should {

    "throws an UnsupportedOperationException" in new WithCursorMocked {
      resultSetMetaData.unwrap(classOf[String]) must throwA[UnsupportedOperationException]
    }

  }

  "isWrapperFor" should {

    "throws an UnsupportedOperationException" in new WithCursorMocked {
      resultSetMetaData.unwrap(classOf[String]) must throwA[UnsupportedOperationException]
    }

  }

}
