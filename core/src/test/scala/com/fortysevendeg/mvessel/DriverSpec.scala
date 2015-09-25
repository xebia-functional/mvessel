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

import java.sql.SQLFeatureNotSupportedException
import java.util.Properties

import com.fortysevendeg.mvessel.api.CursorProxy
import com.fortysevendeg.mvessel.util.ConnectionValues
import org.specs2.matcher.Scope
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification

trait DriverSpecification
  extends Specification
  with Mockito {

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

    val driver = new BaseDriver[CursorProxy] {

      override def parseConnectionString(connectionString: String): Option[ConnectionValues] = Some(connectionValues)

      override def connect(url: String, properties: Properties): Connection[CursorProxy] =
        mock[Connection[CursorProxy]]
    }

  }

  trait WithoutConnectionValues extends DriverScope {

    val driver = new BaseDriver[CursorProxy] {

      override def parseConnectionString(connectionString: String): Option[ConnectionValues] = None

      override def connect(url: String, properties: Properties): Connection[CursorProxy] =
        mock[Connection[CursorProxy]]
    }

  }

}

class DriverSpec
  extends DriverSpecification {

  "acceptsURL" should {

    "return true for a valid connection URL" in new WithConnectionValues {
      driver.acceptsURL(validUrl) must beTrue
    }

    "return false for a valid connection URL with other DB engine prefix" in new WithConnectionValues {
      driver.acceptsURL(invalidPrefixUrl) must beFalse
    }

    "return false for a invalid connection URL" in new WithConnectionValues {
      driver.acceptsURL(invalidUrl) must beFalse
    }

  }

  "jdbcCompliant" should {

    "return false" in new WithConnectionValues {
      driver.jdbcCompliant must beFalse
    }

  }

  "getPropertyInfo" should {

    "return an empty array" in new WithConnectionValues {
      driver.getPropertyInfo(validUrl, properties) must beEmpty
    }

  }

  "getMinorVersion" should {

    "return minorVersion" in new WithConnectionValues {
      driver.getMinorVersion shouldEqual minorVersion
    }

  }

  "getMajorVersion" should {

    "return majorVersion" in new WithConnectionValues {
      driver.getMajorVersion shouldEqual majorVersion
    }

  }

  "getParentLogger" should {

    "throw a SQLFeatureNotSupportedException" in new WithConnectionValues {
      driver.getParentLogger must throwA[SQLFeatureNotSupportedException]
    }

  }

}
