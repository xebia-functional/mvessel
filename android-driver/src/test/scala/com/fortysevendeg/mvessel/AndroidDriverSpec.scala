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

import com.fortysevendeg.mvessel.api.impl.AndroidCursor
import com.fortysevendeg.mvessel.util.ConnectionValues
import org.specs2.matcher.Scope
import org.specs2.mutable.Specification

trait DriverSpecification
  extends Specification {

  val name = "database.db"

  val minorVersion = 0

  val majorVersion = 1

  val validUrl = s"jdbc:sqlite:$name"

  val invalidPrefixUrl = s"jdbc:oracle:$name"

  val invalidUrl = s"urlNotValid"

  val properties = new Properties()

  trait DriverScope extends Scope

  trait WithConnectionValues extends DriverScope {

    val timeout = 1

    val retry = 5

    val connectionValues = ConnectionValues(name, Map("timeout" -> timeout.toString, "retry" -> retry.toString))

    val driver = new AndroidDriver {

      override def parseConnectionString(connectionString: String): Option[ConnectionValues] = Some(connectionValues)
    }

  }

  trait WithoutConnectionValues extends DriverScope {

    val driver = new AndroidDriver {

      override def parseConnectionString(connectionString: String): Option[ConnectionValues] = None
    }

  }

}

class DriverSpec
  extends DriverSpecification {

  "connect" should {

    "create a Connection with the params obtained by the ConnectionStringParser" in
      new WithConnectionValues {
        driver.connect(validUrl, properties) must beLike {
          case c: Connection[AndroidCursor] =>
            c.databaseName shouldEqual name
            c.timeout shouldEqual timeout
            c.retryInterval shouldEqual retry
        }
      }

    "throws a SQLException when the URL can't be parsed" in
      new WithoutConnectionValues {
        driver.connect(validUrl, properties) must throwA[SQLException]
      }
  }

}
