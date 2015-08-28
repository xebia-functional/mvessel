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
