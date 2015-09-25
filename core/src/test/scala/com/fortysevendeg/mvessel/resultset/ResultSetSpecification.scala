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

import java.io.{BufferedReader, Reader, InputStream}
import java.util.Date

import com.fortysevendeg.mvessel._
import com.fortysevendeg.mvessel.api.CursorProxy
import com.fortysevendeg.mvessel.api.impl.CursorSeq
import com.fortysevendeg.mvessel.logging.LogWrapper
import com.fortysevendeg.mvessel.util.DateUtils
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.specification.Scope

import scala.util.Random

trait ResultSetSpecification
  extends Specification
  with Mockito
  with DateUtils {

  val log = new TestLogWrapper

  def formatDateOrThrow(date: Date): String =
    formatDate(date) getOrElse (throw new RuntimeException("Can't format date"))

  def inputStreamToString(is: InputStream): String =
    scala.io.Source.fromInputStream(is).mkString

  def readerToString(reader: Reader): String = {
    val buffer = new BufferedReader(reader)
    Stream.continually(buffer.readLine()).takeWhile(Option(_).isDefined).mkString
  }

  trait WithCursorMocked
    extends Scope {

    val cursor = mock[CursorProxy]

    val resultSet = new ResultSet(cursor, log)

  }

  trait WithEmptyCursor
    extends Scope {

    val columnNames = Array("column1", "column2", "column3", "column4", "column5")

    val columnString = 1

    val columnInteger = 2

    val columnFloat = 3

    val columnBytes = 4

    val columnNull = 5

    val nonExistentColumnName = "column0"

    val cursor = new CursorSeq(columnNames, Seq.empty)

    val resultSet = new ResultSet(cursor, log)

  }

  trait WithCursor
    extends Scope {

    val numRows = 10

    val rows = 1 to numRows map { _ =>
      Seq[Any](
        Random.nextString(10),
        Random.nextInt(10),
        Random.nextFloat(),
        Random.nextString(10).getBytes,
        javaNull)
    }

    val columnNames = Array("column1", "column2", "column3", "column4", "column5")

    val columnString = 1

    val columnInteger = 2

    val columnFloat = 3

    val columnBytes = 4

    val columnNull = 5

    val nonExistentColumnName = "column0"

    val cursor = new CursorSeq(columnNames, rows)

    val resultSet = new ResultSet(cursor, log)
  }

  trait WithCursorAndLogMocked
    extends Scope {

    val cursor = mock[CursorProxy]

    val logger = mock[LogWrapper]

    val resultSet = new ResultSet(cursor, logger)

  }

}
