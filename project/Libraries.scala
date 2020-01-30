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

import sbt._
import V._

object Libraries {

  lazy val specs2Core = "org.specs2" %% "specs2-core" % specs2

  lazy val specs2Mock = "org.specs2" %% "specs2-mock" % specs2

  lazy val sqliteJdbc = "org.xerial" % "sqlite-jdbc" % xerialJdbc

//  lazy val android = "com.google.android" % "android" % androidJar

}