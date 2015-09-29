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

import com.fortysevendeg.mvessel.TestLogWrapper
import com.fortysevendeg.mvessel.logging.LogWrapper
import org.specs2.mutable.Specification
import org.specs2.specification.Scope

trait DateUtilsSpecification
  extends Specification {

  val validFormat = "2015-10-10 12:50:00.123"

  val validFormatDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S").parse(validFormat)

  val invalidFormat = "invalidFormat"

  trait DateUtilsScope
    extends Scope {

    val dateUtils = new DateUtils {
      override val log: LogWrapper = new TestLogWrapper
    }

  }

}

class DateUtilsSpec
  extends DateUtilsSpecification {

  "parseDate" should {

    "return the date as Option for a right format" in new DateUtilsScope {
      dateUtils.parseDate(validFormat) must beSome.which {
        _ shouldEqual validFormatDate
      }
    }

    "return None for an invalid format" in new DateUtilsScope {
      dateUtils.parseDate(invalidFormat) must beNone
    }

  }

  "formatDate" should {

    "return the date formatter as Option" in new DateUtilsScope {
      dateUtils.formatDate(validFormatDate) must beSome.which {
        _ shouldEqual validFormat
      }
    }

  }

}
