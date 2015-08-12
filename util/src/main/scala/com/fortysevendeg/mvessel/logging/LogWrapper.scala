package com.fortysevendeg.mvessel.logging

import Printer._

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

  def logOnError[T](f: => T, msg: String = "") =
    Try(f) match {
      case Failure(ex) => e(msg, Some(ex))
      case _ =>
    }

}

class AndroidLogWrapper(
  level: Int = android.util.Log.INFO,
  tag: String = "scala-sqlite-droid"
  ) extends LogWrapper {

  override def d(message: String) =
    if (level <= android.util.Log.DEBUG) android.util.Log.d(tag, message)

  override def v(message: String) =
    if (level <= android.util.Log.VERBOSE) android.util.Log.v(tag, message)

  override def i(message: String) =
    if (level <= android.util.Log.INFO) android.util.Log.i(tag, message)

  override def w(message: String) =
    if (level <= android.util.Log.WARN) android.util.Log.w(tag, message)

  override def e(message: String, maybeThrowable: Option[Throwable] = None) =
    if (level <= android.util.Log.ERROR)
      maybeThrowable match {
        case Some(t) => android.util.Log.e(tag, message, t)
        case _ => android.util.Log.e(tag, message)
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