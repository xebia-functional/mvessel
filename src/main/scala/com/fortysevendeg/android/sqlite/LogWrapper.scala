package com.fortysevendeg.android.sqlite

import Printer._

trait LogWrapper {

  def v(message: String)

  def d(message: String)

  def i(message: String)

  def w(message: String)

  def e(message: String)

  def e(message: String, t: Throwable)

  def notImplemented[T](result: T): T

}

class AndroidLogWrapper(
  level: Int = android.util.Log.INFO,
  tag: String = "scala-sqlite-droid") extends LogWrapper {

  override def d(message: String) =
    if (level <= android.util.Log.DEBUG) android.util.Log.d(tag, message)

  override def v(message: String) =
    if (level <= android.util.Log.VERBOSE) android.util.Log.v(tag, message)

  override def i(message: String) =
    if (level <= android.util.Log.INFO) android.util.Log.i(tag, message)

  override def w(message: String) =
    if (level <= android.util.Log.WARN) android.util.Log.w(tag, message)

  override def e(message: String) =
    if (level <= android.util.Log.ERROR) android.util.Log.e(tag, message)

  override def e(message: String, t: Throwable) =
    if (level <= android.util.Log.ERROR) android.util.Log.e(tag, message, t)

  override def notImplemented[T](result: T): T = {
    val message = "Method not implemented" :: (stackTraces map (s => s" at ${methodName(s)}(${fileName(s)})")).toList
    w(message.mkString("\n"))
    result
  }

}
object Printer {

  private[this] val basePackage = "com.fortysevendeg.android.sqlite"

  def stackTraces: Seq[StackTraceElement] = Thread.currentThread.getStackTrace

  def stackTrace: Option[StackTraceElement] =
    Thread.currentThread.getStackTrace find(_.getClassName.startsWith(basePackage))

  def methodName(stackTrace: StackTraceElement) =
    s"${stackTrace.getClassName}.${stackTrace.getMethodName}"

  def fileName(stackTrace: StackTraceElement) =
    s"${stackTrace.getFileName}:${stackTrace.getLineNumber}"
}