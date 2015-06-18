package com.fortysevendeg.android.sqlite

trait LogWrapper {

    val level: Int

    def d(message: String)

    def e(message: String)

    def e(message: String, t: Throwable)

    def i(message: String)

    def v(message: String)

}

trait AndroidLogWrapper extends LogWrapper {

    val level = android.util.Log.WARN

    val tag = "SQLDroid"

    override def d(message: String) =
        if (level <= android.util.Log.DEBUG) android.util.Log.d(tag, message)

    override def e(message: String) =
        if (level <= android.util.Log.ERROR) android.util.Log.e(tag, message)

    override def e(message: String, t: Throwable) =
        if (level <= android.util.Log.ERROR) android.util.Log.e(tag, message, t)

    override def i(message: String) =
        if (level <= android.util.Log.INFO) android.util.Log.i(tag, message)

    override def v(message: String) =
        if (level <= android.util.Log.VERBOSE) android.util.Log.v(tag, message)

}
