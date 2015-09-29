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

import org.specs2.mutable.Specification
import org.specs2.specification.Scope

trait ConnectionStringParserSpecification
  extends Specification {

  val name = "/data/data/com.fortysevendeg.mvessel/databases/database.db"

  val memoryName = ":memory:"

  val oneParamMap = Map("param1" -> "value1")

  val twoParamMap = oneParamMap ++ Map("param2" -> "value2")

  val threeParamMap = twoParamMap ++ Map("param3" -> "value3")

  val urlWithoutParams = s"jdbc:sqlite:$name"

  val urlWithOneParam = s"$urlWithoutParams?param1=value1"

  val urlWithTwoParams = s"$urlWithoutParams?param1=value1&param2=value2"

  val urlWithThreeParams = s"$urlWithoutParams?param1=value1&param2=value2&param3=value3"

  val urlWithInvalidPrefix = s"jdbc:oracle:$name"

  val urlInMemory = s"jdbc:sqlite:$memoryName"

  trait ConnectionStringParserScope
    extends Scope {

    val parser = new ConnectionStringParser {}

  }

}

class ConnectionStringParserSpec
  extends ConnectionStringParserSpecification {

  "ConnectionStringParser" should {

    "return a name and an empty map params when passing a valid url without params" in
      new ConnectionStringParserScope {
        parser.parseConnectionString(urlWithoutParams) must beSome[ConnectionValues].which { c =>
          c shouldEqual ConnectionValues(name, Map.empty)
        }
      }

    "return a name and a map with one element when passing a valid url with one param" in
      new ConnectionStringParserScope {
        parser.parseConnectionString(urlWithOneParam) must beSome[ConnectionValues].which { c =>
          c shouldEqual ConnectionValues(name, oneParamMap)
        }
      }

    "return a name and a map with two elements when passing a valid url with two params" in
      new ConnectionStringParserScope {
        parser.parseConnectionString(urlWithTwoParams) must beSome[ConnectionValues].which { c =>
          c shouldEqual ConnectionValues(name, twoParamMap)
        }
      }

    "return a name and a map with three elements when passing a valid url with three params" in
      new ConnectionStringParserScope {
        parser.parseConnectionString(urlWithThreeParams) must beSome[ConnectionValues].which { c =>
          c shouldEqual ConnectionValues(name, threeParamMap)
        }
      }

    "return None when the url is doesn't have a valid prefix" in
      new ConnectionStringParserScope {
        parser.parseConnectionString(urlWithInvalidPrefix) must beNone
      }

    "return a memory name and an empty map params when passing a memory url" in
      new ConnectionStringParserScope {
        parser.parseConnectionString(urlInMemory) must beSome[ConnectionValues].which { c =>
          c shouldEqual ConnectionValues(memoryName, Map.empty)
        }
      }

  }

}
