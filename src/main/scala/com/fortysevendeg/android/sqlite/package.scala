package com.fortysevendeg.android

import java.lang.reflect.Method
import java.sql.Types

import android.database.Cursor

import scala.util.Try

package object sqlite {

  val javaNull = null

  implicit class CursorOps(cursor: Cursor) {

    private[this] val method: Option[Method] = Try(classOf[Cursor].getMethod("getType", classOf[Int])).toOption

    def getTypeSafe(index: Int): Int =
      method map (_.invoke(cursor, index.asInstanceOf[Integer]).asInstanceOf[Int]) getOrElse Types.OTHER

  }

  implicit class IndexOps(columnIndex: Int) {

    def index: Int = columnIndex - 1

  }

}
