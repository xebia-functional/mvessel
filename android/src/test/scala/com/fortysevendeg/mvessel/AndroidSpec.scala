package com.fortysevendeg.mvessel

import org.specs2.mutable.Specification

class AndroidSpec
  extends Specification {

  "Android" should {

    "return true when retrieve android value" in {
      Android.android shouldEqual true
    }

  }

}
