package com.fortysevendeg.android.sqlite

class TestLogWrapper extends LogWrapper {

  override def v(message: String): Unit = println(message)

  override def d(message: String): Unit = println(message)

  override def i(message: String): Unit = println(message)

  override def w(message: String): Unit = println(message)

  override def e(message: String): Unit = println(message)

  override def e(message: String, t: Throwable): Unit = println(message)

  override def notImplemented[T](result: T): T = result

}
