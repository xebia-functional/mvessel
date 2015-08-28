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
