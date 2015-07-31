package com.fortysevendeg.mvessel

import android.database.Cursor
import android.database.sqlite.{SQLiteDatabaseLockedException, SQLiteDatabase}
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

    val mockedDatabase = mock[SQLiteDatabase]

    val cursor = mock[Cursor]

    val databse = new Database(databaseName, timeout, retry, flags) {
      override def openDatabase(name: String, flags: Int): SQLiteDatabase = mockedDatabase
    }

  }

}

class DatabaseSpec extends DatabaseSpecification {

  "rawQuery" should {

    "returns the Cursor when the method rawQuery in SQLiteDatabase works" in new DatabaseScope {
      mockedDatabase.rawQuery(any, any) returns cursor
      databse.rawQuery(sql = sql, selectionArgs = Array()) shouldEqual cursor
      there was one(mockedDatabase).rawQuery(sql, Array())
    }

    "returns the Cursor when the method rawQuery in SQLiteDatabase throws an exception in the first call and returns cursor in the second call" in new DatabaseScope {
      mockedDatabase.rawQuery(any, any) throws new SQLiteDatabaseLockedException("Error") thenReturn cursor
      databse.rawQuery(sql = sql, selectionArgs = Array()) shouldEqual cursor
      there was two(mockedDatabase).rawQuery(sql, Array())
    }

    "throws SQLiteDatabaseLockedException when rawQuery in SQLiteDatabase throws always an exception" in new DatabaseScope {
      mockedDatabase.rawQuery(any, any) throws new SQLiteDatabaseLockedException("Error")
      databse.rawQuery(sql = sql, selectionArgs = Array()) must throwA[SQLiteDatabaseLockedException]
      there was two(mockedDatabase).rawQuery(sql, Array())
    }

  }

  "execSQL" should {

    "executes the SQL statement successful when the method execSQL in SQLite Database works fine" in new DatabaseScope {
      databse.execSQL(sql = sql)
      there was one(mockedDatabase).execSQL(sql)
    }

    "executes the SQL statement successful when the method execSQL in SQLiteDatabase throws an exception in the first call and returns cursor in the second call" in new DatabaseScope {
      mockedDatabase.execSQL(any) throws new SQLiteDatabaseLockedException("Error") thenAnswer new MockAnswer[Unit](args => Unit)
      databse.execSQL(sql = sql)
      there was two(mockedDatabase).execSQL(sql)
    }

    "throws SQLiteDatabaseLockedException when execSQL in SQLiteDatabase throws always an exception" in new DatabaseScope {
      mockedDatabase.execSQL(any) throws new SQLiteDatabaseLockedException("Error")
      databse.execSQL(sql = sql) must throwA[SQLiteDatabaseLockedException]
      there was two(mockedDatabase).execSQL(sql)
    }

  }

  "changedRowCount" should {

    "calls lastChangeCount in SQLiteDatabase" in new DatabaseScope {
      databse.changedRowCount() shouldEqual -1
    }

  }

  "setTransactionSuccessful" should {

    "executes successfully when the method setTransactionSuccessful in SQLiteDatabase works" in
      new DatabaseScope {
        databse.setTransactionSuccessful()
        there was one(mockedDatabase).setTransactionSuccessful()
      }

    "executes successfully when the method setTransactionSuccessful in SQLiteDatabase throws an exception in the first call and executes successfully in the second call" in
      new DatabaseScope {
        mockedDatabase.setTransactionSuccessful() throws new SQLiteDatabaseLockedException("Error") thenAnswer new MockAnswer[Unit](args => Unit)
        databse.setTransactionSuccessful()
        there was two(mockedDatabase).setTransactionSuccessful()
      }

    "throws SQLiteDatabaseLockedException when setTransactionSuccessful in SQLiteDatabase throws always an exception" in
      new DatabaseScope {
        mockedDatabase.setTransactionSuccessful() throws new SQLiteDatabaseLockedException("Error")
        databse.setTransactionSuccessful() must throwA[SQLiteDatabaseLockedException]
        there was two(mockedDatabase).setTransactionSuccessful()
      }

  }

  "beginTransaction" should {

    "executes successfully when the method beginTransaction in SQLiteDatabase works" in
      new DatabaseScope {
        databse.beginTransaction()
        there was one(mockedDatabase).beginTransaction()
      }

    "executes successfully when the method beginTransaction in SQLiteDatabase throws an exception in the first call and executes successfully in the second call" in
      new DatabaseScope {
        mockedDatabase.beginTransaction() throws new SQLiteDatabaseLockedException("Error") thenAnswer new MockAnswer[Unit](args => Unit)
        databse.beginTransaction()
        there was two(mockedDatabase).beginTransaction()
      }

    "throws SQLiteDatabaseLockedException when beginTransaction in SQLiteDatabase throws always an exception" in
      new DatabaseScope {
        mockedDatabase.beginTransaction() throws new SQLiteDatabaseLockedException("Error")
        databse.beginTransaction() must throwA[SQLiteDatabaseLockedException]
        there was two(mockedDatabase).beginTransaction()
      }

  }

  "endTransaction" should {

    "executes successfully when the method endTransaction in SQLiteDatabase works" in
      new DatabaseScope {
        databse.endTransaction()
        there was one(mockedDatabase).endTransaction()
      }

    "executes successfully when the method endTransaction in SQLiteDatabase throws an exception in the first call and executes successfully in the second call" in
      new DatabaseScope {
        mockedDatabase.endTransaction() throws new SQLiteDatabaseLockedException("Error") thenAnswer new MockAnswer[Unit](args => Unit)
        databse.endTransaction()
        there was two(mockedDatabase).endTransaction()
      }

    "throws SQLiteDatabaseLockedException when endTransaction in SQLiteDatabase throws always an exception" in
      new DatabaseScope {
        mockedDatabase.endTransaction() throws new SQLiteDatabaseLockedException("Error")
        databse.endTransaction() must throwA[SQLiteDatabaseLockedException]
        there was two(mockedDatabase).endTransaction()
      }

  }

  "close" should {

    "executes successfully when the method close in SQLiteDatabase works" in
      new DatabaseScope {
        databse.close()
        there was one(mockedDatabase).close()
      }

    "executes successfully when the method close in SQLiteDatabase throws an exception in the first call and executes successfully in the second call" in
      new DatabaseScope {
        mockedDatabase.close() throws new SQLiteDatabaseLockedException("Error") thenAnswer new MockAnswer[Unit](args => Unit)
        databse.close()
        there was two(mockedDatabase).close()
      }

    "throws SQLiteDatabaseLockedException when close in SQLiteDatabase throws always an exception" in
      new DatabaseScope {
        mockedDatabase.close() throws new SQLiteDatabaseLockedException("Error")
        databse.close() must throwA[SQLiteDatabaseLockedException]
        there was two(mockedDatabase).close()
      }

  }

}
