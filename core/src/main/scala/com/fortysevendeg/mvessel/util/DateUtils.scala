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

import java.text.SimpleDateFormat
import java.util.Date

import com.fortysevendeg.mvessel.logging.LogWrapper

import scala.util.{Failure, Success, Try}

trait DateUtils {

  val log: LogWrapper

  private[this] val dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S")

  def parseDate(date: String): Option[java.util.Date] =
    Try(dateFormat.parse(date)) match {
      case Success(d) => Some(d)
      case Failure(e) =>
        log.e("Can't parse date", Some(e))
        None
    }

  def formatDate(date: Date): Option[String] =
    Try(dateFormat.format(date)) match {
      case Success(d) => Some(d)
      case Failure(e) =>
        log.e("Can't format date", Some(e))
        None
    }

}