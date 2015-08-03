package com.fortysevendeg.mvessel.util

import java.lang.reflect.Method
import java.sql.{Connection, Statement, ResultSet, Types}

import android.database.Cursor

import scala.annotation.tailrec
import scala.util.{Failure, Success, Try}

object CursorUtils {

  trait CursorProcessor[T] {
    def move(t: T): Boolean
    def close(t: T): Unit
    def isClosed(t: T): Boolean
  }

  implicit def `Cursor processor` = new CursorProcessor[Cursor] {

    def move(cursor: Cursor): Boolean = cursor.moveToNext()

    def close(cursor: Cursor): Unit = Try(cursor.close())

    def isClosed(cursor: Cursor): Boolean = cursor.isClosed
  }

  implicit def `ResultSet processor` = new CursorProcessor[ResultSet] {

    def move(resultSet: ResultSet): Boolean = resultSet.next()

    def close(resultSet: ResultSet): Unit = Try(resultSet.close())

    def isClosed(resultSet: ResultSet): Boolean = resultSet.isClosed

  }

  private def processCursor[T, C](c: C)(process: C => T, until: Option[Int] = None)(implicit cursorProcessor: CursorProcessor[C]) = {
    def untilTrue(size: Int) = until map (_ > size) getOrElse true
    @tailrec
    def loop(seq: Seq[T]): Seq[T] = {
      if (untilTrue(seq.size) && cursorProcessor.move(c)) loop(seq :+ process(c))
      else {
        cursorProcessor.close(c)
        seq
      }
    }
    Try(loop(Seq.empty)) match {
      case Success(t) => t
      case Failure(e) =>
        if (!cursorProcessor.isClosed(c)) cursorProcessor.close(c)
        throw e
    }
  }

  implicit class IntSome(i: Int) {
    def some = Some(i)
  }

  implicit class CursorOps(cursor: Cursor) {

    private[this] val method: Option[Method] = Try(classOf[Cursor].getMethod("getType", classOf[Int])).toOption

    def getTypeSafe(index: Int): Int =
      method map (_.invoke(cursor, index.asInstanceOf[Integer]).asInstanceOf[Int]) getOrElse Types.OTHER

    def process[T](process: Cursor => T, until: Option[Int] = None) = processCursor(cursor)(process, until)

    def processOne[T](process: Cursor => T) = processCursor(cursor)(process, 1.some).headOption

  }

  implicit class ResultSetOps(resultSet: ResultSet) {

    def process[T](process: ResultSet => T, until: Option[Int] = None) = processCursor(resultSet)(process, until)

    def processOne[T](process: ResultSet => T) = processCursor(resultSet)(process, 1.some).headOption

  }

  object WithStatement {

    def apply[T](f: Statement => T)(implicit connection: Connection): T = {
      val statement = connection.createStatement()
      Try(f(statement)) match {
        case Success(t) =>
          closeStatement(statement)
          t
        case Failure(e) =>
          closeStatement(statement)
          throw e
      }
    }

    private[this] def closeStatement(statement: Statement) {
      Try(statement.close())
    }

  }

}
