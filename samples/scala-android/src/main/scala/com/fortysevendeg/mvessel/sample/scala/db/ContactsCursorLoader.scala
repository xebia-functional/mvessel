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