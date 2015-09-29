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

package com.fortysevendeg.mvessel.api.impl

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.fortysevendeg.mvessel.AndroidDriver
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.specification.Scope

import scala.util.Random

trait AndroidDatabaseSpecification
  extends Specification
  with Mockito {

  val selectSQL = "SELECT * FROM table;"

  trait AndroidDatabaseScope
    extends Scope {

    val sqliteDatabase = mock[SQLiteDatabase]

    val androidDatabase = new AndroidDatabase(sqliteDatabase)

  }

}

class AndroidDatabaseSpec
  extends AndroidDatabaseSpecification {

  "rawQuery" should {

    "return an AndroidCursor with the cursor returned by SQLiteDatabase" in
      new AndroidDatabaseScope {
        val cursor = mock[Cursor]
        sqliteDatabase.rawQuery(any, any) returns cursor
        val androidCursor = androidDatabase.rawQuery(selectSQL, Array.empty)
        androidCursor.cursor shouldEqual cursor
      }

  }

  "execSQL" should {

    "call execSQL with one parameter in the SQLiteDatabase when pass None as bindArgs" in
      new AndroidDatabaseScope {
        androidDatabase.execSQL(selectSQL, None)
        there was one(sqliteDatabase).execSQL(selectSQL)
      }

    "call execSQL with two parameters in the SQLiteDatabase when pass Some as bindArgs" in
      new AndroidDatabaseScope {
        val bindArgs = Array.empty[AnyRef]
        androidDatabase.execSQL(selectSQL, Some(bindArgs))
        there was one(sqliteDatabase).execSQL(selectSQL, bindArgs)
      }

  }

  "setTransactionSuccessful" should {

    "call setTransactionSuccessful in the SQLiteDatabase" in
      new AndroidDatabaseScope {
        androidDatabase.setTransactionSuccessful()
        there was one(sqliteDatabase).setTransactionSuccessful()
      }

  }

  "endTransaction" should {

    "call endTransaction in the SQLiteDatabase" in
      new AndroidDatabaseScope {
        androidDatabase.endTransaction()
        there was one(sqliteDatabase).endTransaction()
      }

  }

  "changedRowCount" should {

    "return some value" in
      new AndroidDatabaseScope {
        androidDatabase.changedRowCount must beSome[Int]
      }

  }

  "isOpen" should {

    "return true if SQLiteDatabase returns true" in
      new AndroidDatabaseScope {
        sqliteDatabase.isOpen returns true
        androidDatabase.isOpen must beTrue
      }

    "return false if SQLiteDatabase returns false" in
      new AndroidDatabaseScope {
        sqliteDatabase.isOpen returns false
        androidDatabase.isOpen must beFalse
      }

  }

  "close" should {

    "call close in the SQLiteDatabase" in
      new AndroidDatabaseScope {
        androidDatabase.close()
        there was one(sqliteDatabase).close()
      }

  }

  "beginTransaction" should {

    "call beginTransaction in the SQLiteDatabase" in
      new AndroidDatabaseScope {
        androidDatabase.beginTransaction()
        there was one(sqliteDatabase).beginTransaction()
      }

  }

  "getVersion" should {

    "return the value returned by SQLiteDatabase" in
      new AndroidDatabaseScope {
        val version = Random.nextInt(100)
        sqliteDatabase.getVersion returns version
        androidDatabase.getVersion shouldEqual version
      }

  }

  "getDriverName" should {

    "return AndroidDriver.driverName" in
      new AndroidDatabaseScope {
        androidDatabase.getDriverName shouldEqual AndroidDriver.driverName
      }

  }

  "getDriverVersion" should {

    "return AndroidDriver.driverVersion" in
      new AndroidDatabaseScope {
        androidDatabase.getDriverVersion shouldEqual AndroidDriver.driverVersion
      }

  }

}
