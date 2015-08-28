package com.fortysevendeg.mvessel.logging

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

  override def e(message: String, maybeThrowable: Option[Throwable] = None) =
    if (level <= android.util.Log.ERROR)
      maybeThrowable match {
        case Some(t) => android.util.Log.e(tag, message, t)
        case _ => android.util.Log.e(tag, message)
      }

}
