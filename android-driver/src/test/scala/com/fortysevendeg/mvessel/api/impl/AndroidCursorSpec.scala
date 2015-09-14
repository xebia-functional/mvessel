package com.fortysevendeg.mvessel.api.impl

import android.content.ContentResolver
import android.database.{CharArrayBuffer, DataSetObserver, ContentObserver, Cursor}
import android.net.Uri
import android.os.Bundle
import com.fortysevendeg.mvessel.api.CursorType
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.specification.Scope

import scala.util.Random

trait AndroidCursorSpecification
  extends Specification
  with Mockito {

  trait AndroidCursorScope
    extends Scope {

    val cursor = mock[Cursor]

    val androidCursor = new AndroidCursor(cursor)

  }

}

class AndroidCursorSpec
  extends AndroidCursorSpecification {

  "getCursorType" should {

    "return CursorType.Null if cursor return Cursor.FIELD_TYPE_NULL" in new AndroidCursorScope {
      val column = 1
      cursor.getType(column) returns Cursor.FIELD_TYPE_NULL
      androidCursor.getCursorType(column) shouldEqual CursorType.Null
    }

    "return CursorType.Integer if cursor return Cursor.FIELD_TYPE_INTEGER" in new AndroidCursorScope {
      val column = 1
      cursor.getType(column) returns Cursor.FIELD_TYPE_INTEGER
      androidCursor.getCursorType(column) shouldEqual CursorType.Integer
    }

    "return CursorType.Float if cursor return Cursor.FIELD_TYPE_FLOAT" in new AndroidCursorScope {
      val column = 1
      cursor.getType(column) returns Cursor.FIELD_TYPE_FLOAT
      androidCursor.getCursorType(column) shouldEqual CursorType.Float
    }

    "return CursorType.Blob if cursor return Cursor.FIELD_TYPE_BLOB" in new AndroidCursorScope {
      val column = 1
      cursor.getType(column) returns Cursor.FIELD_TYPE_BLOB
      androidCursor.getCursorType(column) shouldEqual CursorType.Blob
    }

    "return CursorType.String if cursor return Cursor.FIELD_TYPE_STRING" in new AndroidCursorScope {
      val column = 1
      cursor.getType(column) returns Cursor.FIELD_TYPE_STRING
      androidCursor.getCursorType(column) shouldEqual CursorType.String
    }

  }

  "isClosed" should {

    "return true if cursor return true" in new AndroidCursorScope {
      cursor.isClosed returns true
      androidCursor.isClosed must beTrue
    }

    "return false if cursor return false" in new AndroidCursorScope {
      cursor.isClosed returns false
      androidCursor.isClosed must beFalse
    }

  }

  "isBeforeFirst" should {

    "return true if cursor return true" in new AndroidCursorScope {
      cursor.isBeforeFirst returns true
      androidCursor.isBeforeFirst must beTrue
    }

    "return false if cursor return false" in new AndroidCursorScope {
      cursor.isBeforeFirst returns false
      androidCursor.isBeforeFirst must beFalse
    }

  }

  "isAfterLast" should {

    "return true if cursor return true" in new AndroidCursorScope {
      cursor.isAfterLast returns true
      androidCursor.isAfterLast must beTrue
    }

    "return false if cursor return false" in new AndroidCursorScope {
      cursor.isAfterLast returns false
      androidCursor.isAfterLast must beFalse
    }

  }

  "isFirst" should {

    "return true if cursor return true" in new AndroidCursorScope {
      cursor.isFirst returns true
      androidCursor.isFirst must beTrue
    }

    "return false if cursor return false" in new AndroidCursorScope {
      cursor.isFirst returns false
      androidCursor.isFirst must beFalse
    }

  }

  "isLast" should {

    "return true if cursor return true" in new AndroidCursorScope {
      cursor.isLast returns true
      androidCursor.isLast must beTrue
    }

    "return false if cursor return false" in new AndroidCursorScope {
      cursor.isLast returns false
      androidCursor.isLast must beFalse
    }

  }

  "getCount" should {

    "return the value returned by cursor" in new AndroidCursorScope {
      val count = Random.nextInt(100)
      cursor.getCount returns count
      androidCursor.getCount shouldEqual count
    }

  }

  "getPosition" should {

    "return the value returned by cursor" in new AndroidCursorScope {
      val position = Random.nextInt(100)
      cursor.getPosition returns position
      androidCursor.getPosition shouldEqual position
    }

  }

  "getColumnCount" should {

    "return the value returned by cursor" in new AndroidCursorScope {
      val count = Random.nextInt(100)
      cursor.getColumnCount returns count
      androidCursor.getColumnCount shouldEqual count
    }

  }

  "getColumnNames" should {

    "return the value returned by cursor" in new AndroidCursorScope {
      val columnNames = Array("column1", "column2", "column3")
      cursor.getColumnNames returns columnNames
      androidCursor.getColumnNames shouldEqual columnNames
    }

  }

  "getColumnIndex" should {

    "return the value returned by cursor" in new AndroidCursorScope {
      val column = "column"
      val index = Random.nextInt(100)
      cursor.getColumnIndex(column) returns index
      androidCursor.getColumnIndex(column) shouldEqual index
    }

  }

  "getColumnIndexOrThow" should {

    "return the value returned by cursor" in new AndroidCursorScope {
      val column = "column"
      val index = Random.nextInt(100)
      cursor.getColumnIndexOrThrow(column) returns index
      androidCursor.getColumnIndexOrThrow(column) shouldEqual index
    }

    "throw the exception throwed by cursor" in new AndroidCursorScope {
      val column = "column"
      val exception = new RuntimeException("Error")
      cursor.getColumnIndexOrThrow(column) throws exception
      androidCursor.getColumnIndexOrThrow(column) must throwA(exception)
    }

  }

  "getColumnName" should {

    "return the value returned by cursor" in new AndroidCursorScope {
      val column = 1
      val columnName = Random.nextString(10)
      cursor.getColumnName(column) returns columnName
      androidCursor.getColumnName(column) shouldEqual columnName
    }

  }

  "moveToFirst" should {

    "return true if cursor return true" in new AndroidCursorScope {
      cursor.moveToFirst returns true
      androidCursor.moveToFirst must beTrue
    }

    "return false if cursor return false" in new AndroidCursorScope {
      cursor.moveToFirst returns false
      androidCursor.moveToFirst must beFalse
    }

  }

  "moveToLast" should {

    "return true if cursor return true" in new AndroidCursorScope {
      cursor.moveToLast returns true
      androidCursor.moveToLast must beTrue
    }

    "return false if cursor return false" in new AndroidCursorScope {
      cursor.moveToLast returns false
      androidCursor.moveToLast must beFalse
    }

  }

  "moveToPrevious" should {

    "return true if cursor return true" in new AndroidCursorScope {
      cursor.moveToPrevious returns true
      androidCursor.moveToPrevious must beTrue
    }

    "return false if cursor return false" in new AndroidCursorScope {
      cursor.moveToPrevious returns false
      androidCursor.moveToPrevious must beFalse
    }

  }

  "moveToNext" should {

    "return true if cursor return true" in new AndroidCursorScope {
      cursor.moveToNext returns true
      androidCursor.moveToNext must beTrue
    }

    "return false if cursor return false" in new AndroidCursorScope {
      cursor.moveToNext returns false
      androidCursor.moveToNext must beFalse
    }

  }

  "move" should {

    "return true if cursor return true" in new AndroidCursorScope {
      val position = Random.nextInt(10)
      cursor.move(position) returns true
      androidCursor.move(position) must beTrue
    }

    "return false if cursor return false" in new AndroidCursorScope {
      val position = Random.nextInt(10)
      cursor.move(position) returns false
      androidCursor.move(position) must beFalse
    }

  }

  "moveToPosition" should {

    "return true if cursor return true" in new AndroidCursorScope {
      val position = Random.nextInt(10)
      cursor.moveToPosition(position) returns true
      androidCursor.moveToPosition(position) must beTrue
    }

    "return false if cursor return false" in new AndroidCursorScope {
      val position = Random.nextInt(10)
      cursor.moveToPosition(position) returns false
      androidCursor.moveToPosition(position) must beFalse
    }

  }

  "close" should {

    "call method in cursor" in new AndroidCursorScope {
      androidCursor.close()
      there was one(cursor).close()
    }

  }

  "getWantsAllOnMoveCalls" should {

    "return true if cursor return true" in new AndroidCursorScope {
      cursor.getWantsAllOnMoveCalls returns true
      androidCursor.getWantsAllOnMoveCalls must beTrue
    }

    "return false if cursor return false" in new AndroidCursorScope {
      cursor.getWantsAllOnMoveCalls returns false
      androidCursor.getWantsAllOnMoveCalls must beFalse
    }

  }

  "registerContentObserver" should {

    "call method in cursor" in new AndroidCursorScope {
      val observer = mock[ContentObserver]
      androidCursor.registerContentObserver(observer)
      there was one(cursor).registerContentObserver(observer)
    }

  }

  "unregisterContentObserver" should {

    "call method in cursor" in new AndroidCursorScope {
      val observer = mock[ContentObserver]
      androidCursor.unregisterContentObserver(observer)
      there was one(cursor).unregisterContentObserver(observer)
    }

  }

  "registerDataSetObserver" should {

    "call method in cursor" in new AndroidCursorScope {
      val observer = mock[DataSetObserver]
      androidCursor.registerDataSetObserver(observer)
      there was one(cursor).registerDataSetObserver(observer)
    }

  }

  "unregisterDataSetObserver" should {

    "call method in cursor" in new AndroidCursorScope {
      val observer = mock[DataSetObserver]
      androidCursor.unregisterDataSetObserver(observer)
      there was one(cursor).unregisterDataSetObserver(observer)
    }

  }

  "deactivate" should {

    "call method in cursor" in new AndroidCursorScope {
      androidCursor.deactivate()
      there was one(cursor).deactivate()
    }

  }

  "copyStringToBuffer" should {

    "call close method in cursor" in new AndroidCursorScope {
      val column = 1
      val buffer = mock[CharArrayBuffer]
      androidCursor.copyStringToBuffer(column, buffer)
      there was one(cursor).copyStringToBuffer(column, buffer)
    }

  }

  "requery" should {

    "return true if cursor return true" in new AndroidCursorScope {
      cursor.requery returns true
      androidCursor.requery must beTrue
    }

    "return false if cursor return false" in new AndroidCursorScope {
      cursor.requery returns false
      androidCursor.requery must beFalse
    }

  }

  "copyStringToBuffer" should {

    "call close method in cursor" in new AndroidCursorScope {
      val cr = mock[ContentResolver]
      val uri = mock[Uri]
      androidCursor.setNotificationUri(cr, uri)
      there was one(cursor).setNotificationUri(cr, uri)
    }

  }

  "getExtras" should {

    "return the value returned by cursor" in new AndroidCursorScope {
      val extras = mock[Bundle]
      cursor.getExtras returns extras
      androidCursor.getExtras shouldEqual extras
    }

  }

  "respond" should {

    "return the value returned by cursor" in new AndroidCursorScope {
      val extras = mock[Bundle]
      val result = mock[Bundle]
      cursor.respond(extras) returns result
      androidCursor.respond(extras) shouldEqual result
    }

  }

  "isNull" should {

    "return true if cursor return true" in new AndroidCursorScope {
      val column = 1
      cursor.isNull(column) returns true
      androidCursor.isNull(column) must beTrue
    }

    "return false if cursor return false" in new AndroidCursorScope {
      val column = 1
      cursor.isNull(column) returns false
      androidCursor.isNull(column) must beFalse
    }

  }

  "getDouble" should {

    "return the value returned by cursor" in new AndroidCursorScope {
      val column = 1
      val result = Random.nextDouble()
      cursor.getDouble(column) returns result
      androidCursor.getDouble(column) shouldEqual result
    }

  }

  "getFloat" should {

    "return the value returned by cursor" in new AndroidCursorScope {
      val column = 1
      val result = Random.nextFloat()
      cursor.getFloat(column) returns result
      androidCursor.getFloat(column) shouldEqual result
    }

  }

  "getLong" should {

    "return the value returned by cursor" in new AndroidCursorScope {
      val column = 1
      val result = Random.nextLong()
      cursor.getLong(column) returns result
      androidCursor.getLong(column) shouldEqual result
    }

  }

  "getShort" should {

    "return the value returned by cursor" in new AndroidCursorScope {
      val column = 1
      val result = Random.nextInt().toShort
      cursor.getShort(column) returns result
      androidCursor.getShort(column) shouldEqual result
    }

  }

  "getInt" should {

    "return the value returned by cursor" in new AndroidCursorScope {
      val column = 1
      val result = Random.nextInt()
      cursor.getInt(column) returns result
      androidCursor.getInt(column) shouldEqual result
    }

  }

  "getBlob" should {

    "return the value returned by cursor" in new AndroidCursorScope {
      val column = 1
      val result = Random.nextString(10).getBytes
      cursor.getBlob(column) returns result
      androidCursor.getBlob(column) shouldEqual result
    }

  }

  "getString" should {

    "return the value returned by cursor" in new AndroidCursorScope {
      val column = 1
      val result = Random.nextString(10)
      cursor.getString(column) returns result
      androidCursor.getString(column) shouldEqual result
    }

  }

  "getType" should {

    "return the value returned by cursor" in new AndroidCursorScope {
      val column = 1
      val result = Random.nextInt()
      cursor.getType(column) returns result
      androidCursor.getType(column) shouldEqual result
    }

  }

}
