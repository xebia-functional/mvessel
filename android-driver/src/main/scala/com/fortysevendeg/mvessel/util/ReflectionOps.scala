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

package com.fortysevendeg.mvessel.util

import java.lang.reflect.Method
import java.sql.Types

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

import scala.util.{Success, Try}

object ReflectionOps {

  private[this] val cursorGetType: Option[Method] = Try(classOf[Cursor].getMethod("getType", classOf[Int])).toOption

  /**
   * The count of changed rows on Android it's a call to a package-private method
   */
  private[this] val databaseLastChangeCount: Option[Method] =
    Try(classOf[SQLiteDatabase].getDeclaredMethod("lastChangeCount")) match {
      case Success(m) =>
        m.setAccessible(true)
        Some(m)
      case _ => None
    }

  /**
   * Added in API level 11
   */
  val lockedExceptionClass: Option[Class[_]] =
    Try(Class.forName("android.database.sqlite.SQLiteDatabaseLockedException")).toOption

  def getTypeSafe(cursor: Cursor, index: Int): Int =
    cursorGetType map (_.invoke(cursor, index.asInstanceOf[Integer]).asInstanceOf[Int]) getOrElse Types.OTHER

  def lastChangeCount(database: SQLiteDatabase): Option[Int] =
    databaseLastChangeCount map (_.invoke(database).asInstanceOf[Int])
}
