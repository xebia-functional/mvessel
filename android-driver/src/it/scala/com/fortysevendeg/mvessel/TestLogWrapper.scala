package com.fortysevendeg.mvessel

import com.fortysevendeg.mvessel.logging.LogWrapper

class TestLogWrapper extends LogWrapper {

  override def v(message: String): Unit = {}

  override def d(message: String): Unit = {}

  override def i(message: String): Unit = {}

  override def w(message: String): Unit = {}

  override def e(message: String, maybeThrowable: Option[Throwable]): Unit = println(message)

}