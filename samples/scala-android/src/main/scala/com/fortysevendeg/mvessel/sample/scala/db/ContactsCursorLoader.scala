package com.fortysevendeg.mvessel.sample.scala.db

import android.content.Context
import android.content.CursorLoader
import android.database.Cursor
import com.fortysevendeg.mvessel.Database
import com.fortysevendeg.mvessel.api.impl.AndroidCursor

class ContactsCursorLoader(
  context: Context,
  database: Database[AndroidCursor]) extends CursorLoader(context)  {

  override def loadInBackground: Cursor =
    database.rawQuery(
      sql = String.format(
        "SELECT %s, %s, %s FROM %s",
        ContactsOpenHelper.C_ID,
        ContactsOpenHelper.C_NAME,
        ContactsOpenHelper.C_AGE,
        ContactsOpenHelper.TABLE))
}