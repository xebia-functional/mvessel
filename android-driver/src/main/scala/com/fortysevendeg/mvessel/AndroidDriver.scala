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

import java.sql.SQLException
import java.util.Properties

import com.fortysevendeg.mvessel.api.impl.{AndroidCursor, AndroidDatabaseFactory}
import com.fortysevendeg.mvessel.logging.AndroidLogWrapper
import com.fortysevendeg.mvessel.util.DatabaseUtils.WrapSQLException

import scala.util.{Failure, Try}
import com.fortysevendeg.mvessel.Connection._

class AndroidDriver extends BaseDriver[AndroidCursor] {

  override def connect(connectionUrl: String, properties: Properties): Connection[AndroidCursor] =
    WrapSQLException(parseConnectionString(connectionUrl), s"Can't parse $connectionUrl") { values =>
      new Connection(
        databaseWrapperFactory = new AndroidDatabaseFactory,
        databaseName = values.name,
        timeout = readTimeOut(values.params) getOrElse defaultTimeout,
        retryInterval = readRetry(values.params) getOrElse defaultRetryInterval,
        flags = readFlags(properties),
        logWrapper = new AndroidLogWrapper)
    }

  protected def readTimeOut(params: Map[String, String]): Option[Long] =
    readLongParam(BaseDriver.timeoutParam, params)

  protected def readRetry(params: Map[String, String]): Option[Int] =
    readLongParam(BaseDriver.retryParam, params) map (_.toInt)

  protected def readLongParam(name: String, params: Map[String, String]): Option[Long] =
    params.get(name) flatMap (value => Try(value.toLong).toOption)

  val flags =
    android.database.sqlite.SQLiteDatabase.CREATE_IF_NECESSARY |
      android.database.sqlite.SQLiteDatabase.OPEN_READWRITE |
      android.database.sqlite.SQLiteDatabase.NO_LOCALIZED_COLLATORS

  protected def readFlags(properties: Properties): Int =
    Option(properties) match {
      case Some(p) =>
        completeFlags(
          p.getProperty(AndroidDriver.databaseFlags),
          p.getProperty(AndroidDriver.additionalDatabaseFlags))
      case _ => flags
    }

  protected def completeFlags(databaseFlags: String, androidFlags: String): Int =
    (Option(databaseFlags), Option(androidFlags)) match {
      case (Some(f), _) => parseInt(f, flags)
      case (None, Some(f)) => flags | parseInt(f, 0)
      case _ => flags
    }

  protected def parseInt(value: String, defaultValue: Int) =
    Try(value.toInt).toOption getOrElse defaultValue

}

object AndroidDriver {

  def register() =
    Try(java.sql.DriverManager.registerDriver(new AndroidDriver)) match {
      case Failure(e) => throw new SQLException(e)
      case _ =>
    }

  val driverName = AndroidDriver.getClass.getName

  val driverVersion = BuildInfo.version

  val databaseFlags = "DatabaseFlags"

  val additionalDatabaseFlags = "AdditionalDatabaseFlags"

}