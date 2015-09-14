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