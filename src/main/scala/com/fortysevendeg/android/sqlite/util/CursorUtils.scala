package com.fortysevendeg.android.sqlite.util

import java.lang.reflect.Method
import java.sql.{Types, ResultSet}

import android.database.Cursor

import scala.annotation.tailrec
import scala.util.Try

object CursorUtils {

  trait CursorProcessor[T] {
    def move(t: T): Boolean
    def close(t: T): Unit
  }

  implicit def `Cursor processor` = new CursorProcessor[Cursor] {

    def move(cursor: Cursor): Boolean = cursor.moveToNext()

    def close(cursor: Cursor): Unit = cursor.close()
  }

  implicit def `ResultSet processor` = new CursorProcessor[ResultSet] {

    def move(resultSet: ResultSet): Boolean = resultSet.next()

    def close(resultSet: ResultSet): Unit = resultSet.close()

  }

  def processCursor[T, C](c: C)(process: C => T)(implicit cursorProcessor: CursorProcessor[C]) = {
    @tailrec
    def loop(seq: Seq[T], c: C): Seq[T] = {
      if (cursorProcessor.move(c)) loop(seq :+ process(c), c)
      else {
        cursorProcessor.close(c)
        seq
      }
    }
    loop(Seq.empty, c)
  }

  implicit class CursorOps(cursor: Cursor) {

    private[this] val method: Option[Method] = Try(classOf[Cursor].getMethod("getType", classOf[Int])).toOption

    def getTypeSafe(index: Int): Int =
      method map (_.invoke(cursor, index.asInstanceOf[Integer]).asInstanceOf[Int]) getOrElse Types.OTHER

    def process[T](process: Cursor => T) = processCursor(cursor)(process)

  }

  implicit class ResultSetOps(resultSet: ResultSet) {

    def process[T](process: ResultSet => T) = processCursor(resultSet)(process)

  }

}
