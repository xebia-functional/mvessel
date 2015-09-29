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

package com.fortysevendeg.mvessel

import java.sql.{Driver => SQLDriver, _}
import java.util.Properties
import java.util.logging.Logger

import com.fortysevendeg.mvessel.BaseDriver._
import com.fortysevendeg.mvessel.api.CursorProxy
import com.fortysevendeg.mvessel.util.ConnectionStringParser

trait BaseDriver[T <: CursorProxy]
  extends SQLDriver
  with ConnectionStringParser {

  override def acceptsURL(url: String): Boolean =
    Option(url) exists (_.startsWith(sqlitePrefix))

  override def jdbcCompliant(): Boolean = false

  override def getPropertyInfo(url: String, info: Properties): scala.Array[DriverPropertyInfo] = scala.Array.empty

  override def getMinorVersion: Int = 0

  override def getMajorVersion: Int = 1

  override def getParentLogger: Logger =
    throw new SQLFeatureNotSupportedException

  def connect(url: String, properties: Properties): Connection[T]
}

object BaseDriver {

  val sqlitePrefix = "jdbc:sqlite:"

  val timeoutParam = "timeout"

  val retryParam = "retry"

}
