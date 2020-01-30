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

import java.io.PrintWriter
import java.sql.{Connection => SQLConnection, DriverManager}
import java.util.Properties
import java.util.logging.Logger
import javax.sql.{DataSource => SQLDataSource}

import com.fortysevendeg.mvessel.api.CursorProxy
import com.fortysevendeg.mvessel.logging.LogWrapper

import scala.util.{Failure, Success, Try}

class DataSource[T <: CursorProxy](
  driver: BaseDriver[T],
  properties: Properties = new Properties,
  dbPath: String,
  log: LogWrapper)
  extends SQLDataSource
  with WrapperNotSupported {

  protected final val url: String = BaseDriver.sqlitePrefix + dbPath

  override def getConnection: SQLConnection = driver.connect(url, properties)

  override def getConnection(username: String, password: String): SQLConnection = getConnection

  override def getLogWriter: PrintWriter =
    Try {
      new PrintWriter("droid.log")
    } match {
      case Success(pw) => pw
      case Failure(e) =>
        log.e("Can't create log writer", Some(e))
        javaNull
    }

  override def setLogWriter(out: PrintWriter) =
    Try {
      DriverManager.setLogWriter(out)
    } match {
      case Failure(e) => log.e("Error setting log writer", Some(e))
      case _ =>
    }

  override def getParentLogger: Logger = log.notImplemented(javaNull)

  override def getLoginTimeout: Int = log.notImplemented(0)

  override def setLoginTimeout(seconds: Int) = log.notImplemented(Unit)
}
