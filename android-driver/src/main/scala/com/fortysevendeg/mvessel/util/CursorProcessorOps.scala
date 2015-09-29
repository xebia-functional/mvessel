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

import android.database.Cursor

import StructureControlProcessor._

import scala.util.Try

object CursorProcessorOps {

  implicit def `Cursor processor` = new StructureControlProcessor[Cursor] {

    def move(cursor: Cursor): Boolean = cursor.moveToNext()

    def close(cursor: Cursor): Unit = Try(cursor.close())

    def isClosed(cursor: Cursor): Boolean = cursor.isClosed
  }

  implicit class CursorOps(cursor: Cursor) {

    def process[T](process: Cursor => T, until: Option[Int] = None) = processStructureControl(cursor)(process, until)

    def processOne[T](process: Cursor => T) = processStructureControl(cursor)(process, Some(1)).headOption

  }

}
