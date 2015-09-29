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
