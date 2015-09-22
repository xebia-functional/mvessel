package com.fortysevendeg.mvessel.sample.slick.db

import android.content.Context
import android.database.sqlite.{SQLiteDatabase, SQLiteOpenHelper}
import com.fortysevendeg.mvessel.AndroidDriver
import slick.driver.SQLiteDriver.api._
import SlickDatabaseHelper._

class SlickDatabaseHelper(context: Context)
  extends SQLiteOpenHelper(context, databaseName, null, databaseVersion)
  with SlickDatabaseConfig {

  val driverName = "com.fortysevendeg.mvessel.AndroidDriver"


  lazy val database = {
    val sqliteDatabase = getReadableDatabase
    AndroidDriver.register()
    Database.forURL(
      url = "jdbc:sqlite:" + sqliteDatabase.getPath,
      driver = driverName)
  }

  override def onCreate(sqliteDatabase: SQLiteDatabase): Unit = {
    valueTable.schema.create.statements foreach sqliteDatabase.execSQL
    sampleTable.schema.create.statements foreach sqliteDatabase.execSQL
  }

  override def onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int): Unit =
    throw new IllegalStateException("Can't make an upgrade")
}

object SlickDatabaseHelper {

  val databaseName = "slick.db"

  val databaseVersion = 1

}
