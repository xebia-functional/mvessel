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

package com.fortysevendeg.mvessel.logging

import com.fortysevendeg.mvessel.logging.Printer._

import scala.util.{Failure, Try}

trait LogWrapper {

  def v(message: String)

  def d(message: String)

  def i(message: String)

  def w(message: String)

  def e(message: String, maybeThrowable: Option[Throwable] = None)

  def notImplemented[T](result: T): T = {
    val message = "Method not implemented" :: (stackTraces map (s => s" at ${methodName(s)}(${fileName(s)})")).toList
    w(message.mkString("\n"))
    result
  }

  def logOnError[T](msg: String = "", f: => T) =
    Try(f) match {
      case Failure(ex) => e(msg, Some(ex))
      case _ =>
    }

}

object Printer {

  private[this] val basePackage = "com.fortysevendeg.mvessel"

  def stackTraces: Seq[StackTraceElement] = Thread.currentThread.getStackTrace

  def stackTrace: Option[StackTraceElement] =
    Thread.currentThread.getStackTrace find (_.getClassName.startsWith(basePackage))

  def methodName(stackTrace: StackTraceElement) =
    s"${stackTrace.getClassName}.${stackTrace.getMethodName}"

  def fileName(stackTrace: StackTraceElement) =
    s"${stackTrace.getFileName}:${stackTrace.getLineNumber}"
}