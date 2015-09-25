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
