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

import java.util.Properties

import com.fortysevendeg.mvessel.api.CursorProxy
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.specification.Scope

import scala.util.Random

trait DataSourceSpecification
  extends Specification
  with Mockito {

  trait DataSourceScope
    extends Scope {

    val driver = mock[BaseDriver[CursorProxy]]

    val connection = mock[Connection[CursorProxy]]

    val properties = new Properties

    val dbPath = "/path/dbname.db"

    val datasource = new DataSource[CursorProxy](
      driver = driver,
      properties = properties,
      dbPath = dbPath,
      log = new TestLogWrapper)

  }

}

class DataSourceSpec
  extends DataSourceSpecification {

  "getConnection" should {

    "call to connect method of driver with right parameters" in
      new DataSourceScope {

        driver.connect(contain(dbPath), beTheSameAs(properties)).returns(connection)

        datasource.getConnection shouldEqual connection
      }

    "call to connect method of driver with right parameters when passing username and password" in
      new DataSourceScope {

        driver.connect(contain(dbPath), beTheSameAs(properties)).returns(connection)

        datasource.getConnection(Random.nextString(10), Random.nextString(10)) shouldEqual connection
      }

  }

  "getParentLogger" should {

    "returns a java null" in new DataSourceScope {
      datasource.getParentLogger shouldEqual javaNull
    }

  }

  "getLoginTimeout" should {

    "returns 0" in new DataSourceScope {
      datasource.getLoginTimeout shouldEqual 0
    }

  }

  "isWrapperFor" should {

    "throws an UnsupportedOperationException" in new DataSourceScope {
      datasource.isWrapperFor(classOf[DataSource[CursorProxy]]) must throwA[UnsupportedOperationException]
    }

  }

  "unwrap" should {

    "throws an UnsupportedOperationException" in new DataSourceScope {
      datasource.unwrap(classOf[DataSource[CursorProxy]]) must throwA[UnsupportedOperationException]
    }

  }

}
