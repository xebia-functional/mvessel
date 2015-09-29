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

package com.fortysevendeg.mvessel

import com.fortysevendeg.mvessel.api.{CursorProxy, DatabaseProxy, DatabaseProxyFactory}
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.specification.Scope

trait DatabaseSpecification
  extends Specification
  with Mockito {

  val databaseName = "database"

  val timeout = 999

  val retry = 500

  val flags = 0
  
  val sql = "SELECT * FROM table"

  trait DatabaseScope
    extends Scope {

    val exception = new RuntimeException("Error")

    val mockedDatabaseFactory = mock[DatabaseProxyFactory[CursorProxy]]

    val mockedDatabase = mock[DatabaseProxy[CursorProxy]]

    mockedDatabaseFactory.openDatabase(any, any) returns mockedDatabase

    mockedDatabaseFactory.isLockedException(exception) returns true

    val cursor = mock[CursorProxy]

    val database = new Database[CursorProxy](
      databaseFactory = mockedDatabaseFactory,
      name = databaseName,
      timeout = timeout,
      retry = retry,
      flags = flags)

  }

}

class DatabaseSpec extends DatabaseSpecification {

  "rawQuery" should {

    "returns the Cursor when the method rawQuery in SQLiteDatabase works" in new DatabaseScope {
      mockedDatabase.rawQuery(any, any) returns cursor
      database.rawQuery(sql = sql, selectionArgs = Array()) shouldEqual cursor
      there was one(mockedDatabase).rawQuery(sql, Array())
    }

    "returns the Cursor when the method rawQuery in SQLiteDatabase throws an exception in the first call and returns cursor in the second call" in new DatabaseScope {
      mockedDatabase.rawQuery(any, any) throws exception thenReturn cursor
      database.rawQuery(sql = sql, selectionArgs = Array()) shouldEqual cursor
      there was two(mockedDatabase).rawQuery(sql, Array())
    }

    "throws RuntimeException when rawQuery in SQLiteDatabase throws always an exception" in new DatabaseScope {
      mockedDatabase.rawQuery(any, any) throws exception
      database.rawQuery(sql = sql, selectionArgs = Array()) must throwA[RuntimeException]
      there was two(mockedDatabase).rawQuery(sql, Array())
    }

  }

  "execSQL" should {

    "executes the SQL statement successful when the method execSQL in SQLite Database works fine" in new DatabaseScope {
      database.execSQL(sql = sql)
      there was one(mockedDatabase).execSQL(sql)
    }

    "executes the SQL statement successful when the method execSQL in SQLiteDatabase throws an exception in the first call and returns cursor in the second call" in new DatabaseScope {
      mockedDatabase.execSQL(any, any) throws exception thenAnswer new MockAnswer[Unit](args => Unit)
      database.execSQL(sql = sql)
      there was two(mockedDatabase).execSQL(sql)
    }

    "throws RuntimeException when execSQL in SQLiteDatabase throws always an exception" in new DatabaseScope {
      mockedDatabase.execSQL(any, any) throws exception
      database.execSQL(sql = sql) must throwA[RuntimeException]
    }

  }

  "changedRowCount" should {

    "calls lastChangeCount in SQLiteDatabase" in new DatabaseScope {
      val changedRowCount = -1
      mockedDatabase.changedRowCount returns Some(changedRowCount)
      database.changedRowCount() shouldEqual changedRowCount
    }

  }

  "setTransactionSuccessful" should {

    "executes successfully when the method setTransactionSuccessful in SQLiteDatabase works" in
      new DatabaseScope {
        database.setTransactionSuccessful()
        there was one(mockedDatabase).setTransactionSuccessful()
      }

    "executes successfully when the method setTransactionSuccessful in SQLiteDatabase throws an exception in the first call and executes successfully in the second call" in
      new DatabaseScope {
        mockedDatabase.setTransactionSuccessful() throws exception thenAnswer new MockAnswer[Unit](args => Unit)
        database.setTransactionSuccessful()
        there was two(mockedDatabase).setTransactionSuccessful()
      }

    "throws RuntimeException when setTransactionSuccessful in SQLiteDatabase throws always an exception" in
      new DatabaseScope {
        mockedDatabase.setTransactionSuccessful() throws exception
        database.setTransactionSuccessful() must throwA[RuntimeException]
        there was two(mockedDatabase).setTransactionSuccessful()
      }

  }

  "beginTransaction" should {

    "executes successfully when the method beginTransaction in SQLiteDatabase works" in
      new DatabaseScope {
        database.beginTransaction()
        there was one(mockedDatabase).beginTransaction()
      }

    "executes successfully when the method beginTransaction in SQLiteDatabase throws an exception in the first call and executes successfully in the second call" in
      new DatabaseScope {
        mockedDatabase.beginTransaction() throws exception thenAnswer new MockAnswer[Unit](args => Unit)
        database.beginTransaction()
        there was two(mockedDatabase).beginTransaction()
      }

    "throws RuntimeException when beginTransaction in SQLiteDatabase throws always an exception" in
      new DatabaseScope {
        mockedDatabase.beginTransaction() throws exception
        database.beginTransaction() must throwA[RuntimeException]
        there was two(mockedDatabase).beginTransaction()
      }

  }

  "endTransaction" should {

    "executes successfully when the method endTransaction in SQLiteDatabase works" in
      new DatabaseScope {
        database.endTransaction()
        there was one(mockedDatabase).endTransaction()
      }

    "executes successfully when the method endTransaction in SQLiteDatabase throws an exception in the first call and executes successfully in the second call" in
      new DatabaseScope {
        mockedDatabase.endTransaction() throws exception thenAnswer new MockAnswer[Unit](args => Unit)
        database.endTransaction()
        there was two(mockedDatabase).endTransaction()
      }

    "throws RuntimeException when endTransaction in SQLiteDatabase throws always an exception" in
      new DatabaseScope {
        mockedDatabase.endTransaction() throws exception
        database.endTransaction() must throwA[RuntimeException]
        there was two(mockedDatabase).endTransaction()
      }

  }

  "close" should {

    "executes successfully when the method close in SQLiteDatabase works" in
      new DatabaseScope {
        database.close()
        there was one(mockedDatabase).close()
      }

    "executes successfully when the method close in SQLiteDatabase throws an exception in the first call and executes successfully in the second call" in
      new DatabaseScope {
        mockedDatabase.close() throws exception thenAnswer new MockAnswer[Unit](args => Unit)
        database.close()
        there was two(mockedDatabase).close()
      }

    "throws RuntimeException when close in SQLiteDatabase throws always an exception" in
      new DatabaseScope {
        mockedDatabase.close() throws exception
        database.close() must throwA[RuntimeException]
        there was two(mockedDatabase).close()
      }

  }

}
