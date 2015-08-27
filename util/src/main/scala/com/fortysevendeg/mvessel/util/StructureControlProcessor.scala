package com.fortysevendeg.mvessel.util

import scala.annotation.tailrec
import scala.util.{Failure, Success, Try}

trait StructureControlProcessor[T] {
  def move(t: T): Boolean
  def close(t: T): Unit
  def isClosed(t: T): Boolean
}

object StructureControlProcessor {

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
