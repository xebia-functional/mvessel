package com.fortysevendeg.android.sqlite.statement

import java.lang
import java.sql.SQLException
import com.fortysevendeg.android.sqlite._
import TypeTransformers._
import MapUtils._

import scala.collection.mutable

trait StatementArguments {

  def toStringArray: scala.Array[String] = scala.Array.empty

  def toArray: scala.Array[AnyRef] = scala.Array.empty

}

class PreparedStatementArguments extends StatementArguments {

  private[this] var arguments: NonEmptyList[mutable.Map[Int, AnyRef]] = NonEmptyList(mutable.Map.empty)

  // Prepared statements count from 1
  private[this] var maxIndex: Int = 1

  def map[U](f: scala.Array[AnyRef] => U): Seq[U] =
    arguments map (map => f(toArray(map, maxIndex - 1)))

  def addNewEntry(): Unit =
    arguments = NonEmptyList(head = mutable.Map.empty, tail = arguments.tail :+ arguments.head)

  def clearArguments(): Unit = {
    arguments = NonEmptyList(mutable.Map.empty)
    maxIndex = 1
  }

  override def toStringArray: Array[String] = toArray map (_.toString)

  override def toArray: Array[AnyRef] = arguments.head.toArray

  // In android only byte[], String, Long and Double are supported in bindArgs.

  def setObjectArgument(position: Int, arg: Any): Unit = nonEmptyArgument(position, arg) {
    case a: scala.Array[Byte] => setArgument(position, a)
    case a: Byte => setArgument(position, a)
    case a: Boolean => setArgument(position, a)
    case a: Double => setArgument(position, a)
    case a: Short => setArgument(position, a)
    case a: Float => setArgument(position, a)
    case a: String => setArgument(position, a)
    case a: java.sql.Date => setArgument(position, a)
    case a: java.sql.Time => setArgument(position, a)
    case a: java.sql.Timestamp => setArgument(position, a)
    case _ => throw new SQLException()
  }

  def removeArgument(position: Int): Unit = {}

  def setArgument(position: Int, arg: scala.Array[Byte]): Unit =
    nonEmptyArgument(position, arg)(addToHead(position, _))

  def setArgument(position: Int, arg: Double): Unit =
    nonEmptyArgument(position, arg) { d =>
      addToHead(position, java.lang.Double.valueOf(d))
    }

  def setArgument(position: Int, arg: Long): Unit =
    nonEmptyArgument(position, arg) { l =>
      addToHead(position, java.lang.Long.valueOf(l))
    }

  def setArgument(position: Int, arg: String): Unit =
    nonEmptyArgument(position, arg)(addToHead(position, _))

  def setArgument(position: Int, arg: Byte): Unit =
    setArgument(position, arg.toLong)

  def setArgument(position: Int, arg: Boolean): Unit =
    setArgument(position, arg.toLong)

  def setArgument(position: Int, arg: Short): Unit =
    setArgument(position, arg.toLong)

  def setArgument(position: Int, arg: Float): Unit =
    setArgument(position, arg.toDouble)

  def setArgument(position: Int, arg: java.sql.Date): Unit =
    setArgument(position, arg.toLong)

  def setArgument(position: Int, arg: java.sql.Time): Unit =
    setArgument(position, arg.toLong)

  def setArgument(position: Int, arg: java.sql.Timestamp): Unit =
    setArgument(position, arg.toLong)

  private[this] def addToHead(position: Int, arg: AnyRef) = {
    arguments.head += (position -> arg)
    if (position > maxIndex) maxIndex = position
  }

  private[this] def nonEmptyArgument[T](position: Int, arg: T)(f: T => Unit) = Option(arg) match {
    case Some(a) => f(a)
    case None => removeArgument(position)
  }

  private[this] def toArray(
    map: mutable.Map[Int, AnyRef],
    maxIndex: Int): scala.Array[AnyRef] =
    (0 to maxIndex map {
      map get _ match {
        case Some(v) => v
        case None => javaNull
      }
    }).toArray

}

case class NonEmptyList[+A](head: A, tail: Seq[A] = Seq.empty) {

  def map[U](f: (A) => U): Seq[U] = tail :+ head map f

}

object MapUtils {

  implicit class MapArray(map: mutable.Map[Int, AnyRef]) {

    def toArray(maxIndex: Int): scala.Array[AnyRef] =
      (0 to maxIndex map {
        map get _ match {
          case Some(v) => v
          case None => javaNull
        }
      }).toArray

  }

}

object TypeTransformers {

  implicit class BooleanTransformer(arg: Boolean) {
    def toLong: Long = if (arg) 1l else 0l
  }

  implicit class DateTransformer(arg: java.sql.Date) {
    def toLong: Long = arg.getTime
  }

  implicit class TimeTransformer(arg: java.sql.Time) {
    def toLong: Long = arg.getTime
  }

  implicit class TimestampTransformer(arg: java.sql.Timestamp) {
    def toLong: Long = arg.getTime
  }

}