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

package com.fortysevendeg.mvessel.resultset

import com.fortysevendeg.mvessel.TestLogWrapper
import com.fortysevendeg.mvessel.api.CursorProxy
import com.fortysevendeg.mvessel.api.impl.CursorSeq
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.specification.Scope

import scala.util.Random

trait ResultSetMetaDataSpecification
  extends Specification
  with Mockito {

  trait WithCursorMocked
    extends Scope {

    val cursor = mock[CursorProxy]

    val resultSetMetaData = new ResultSetMetaData(cursor, new TestLogWrapper)

  }

  trait WithEmptyCursor
    extends Scope {

    val columnNames = Array("column1", "column2", "column3")

    val cursor = new CursorSeq(columnNames, Seq.empty)

    val resultSetMetaData = new ResultSetMetaData(cursor, new TestLogWrapper)

  }

  trait WithCursorData
    extends Scope {

    val rows = 1 to 10 map { _ =>
      Seq[Any](
        Random.nextString(10),
        Random.nextInt(10),
        Random.nextFloat())
    }

    val columnNames = Array("column1", "column2", "column3")

    val cursor = new CursorSeq(columnNames, rows)

    val resultSetMetaData = new ResultSetMetaData(cursor, new TestLogWrapper)

  }

}
