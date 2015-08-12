package com.fortysevendeg.mvessel.util

import java.sql.ResultSet

import android.database.Cursor

import scala.annotation.tailrec
import scala.util.{Failure, Success, Try}

trait StructureControlProcessor[T] {
  def move(t: T): Boolean
  def close(t: T): Unit
  def isClosed(t: T): Boolean
}

object StructureControlProcessor {

  implicit def `Cursor processor` = new StructureControlProcessor[Cursor] {

    def move(cursor: Cursor): Boolean = cursor.moveToNext()

    def close(cursor: Cursor): Unit = Try(cursor.close())

    def isClosed(cursor: Cursor): Boolean = cursor.isClosed
  }

  implicit def `ResultSet processor` = new StructureControlProcessor[ResultSet] {

    def move(resultSet: ResultSet): Boolean = resultSet.next()

    def close(resultSet: ResultSet): Unit = Try(resultSet.close())

    def isClosed(resultSet: ResultSet): Boolean = resultSet.isClosed

  }

  def processStructureControl[T, C](c: C)(process: C => T, until: Option[Int] = None)(implicit processor: StructureControlProcessor[C]) = {

    def untilTrue(size: Int) = until map (_ > size) getOrElse true

    @tailrec
    def loop(seq: Seq[T]): Seq[T] = {
      if (untilTrue(seq.size) && processor.move(c)) loop(seq :+ process(c))
      else {
        processor.close(c)
        seq
      }
    }

    Try(loop(Seq.empty)) match {
      case Success(t) => t
      case Failure(e) =>
        if (!processor.isClosed(c)) processor.close(c)
        throw e
    }
  }

}
