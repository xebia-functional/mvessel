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

import android.database.{MatrixCursor, Cursor}
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.specification.Scope
import CursorProcessorOps._

import scala.util.Random

trait CursorOpsSpecification
  extends Specification
  with Mockito {

  val total = 10

  val allValues: Seq[Array[AnyRef]] = (1 to total map { i =>
    Array(i.asInstanceOf[AnyRef], s"name $i", new java.lang.Integer(Random.nextInt(99)))
  }).toSeq

  trait CursorOps
    extends Scope

  trait WithCursors
    extends CursorOps {

    val columnNames = Array("id", "name", "age")

    val cursor = buildCursor(allValues.slice(0, total))

    val oneRowCursor = buildCursor(allValues.slice(0, 1))

    val emptyCursor = buildCursor(Seq.empty)

    def buildCursor(values: Seq[Array[AnyRef]]): Cursor = {
      val cursor = new MatrixCursor(columnNames)
      values foreach cursor.addRow
      cursor
    }

  }

}

class CursorOpsSpec
  extends CursorOpsSpecification {

  "process for Cursor" should {

    "return a sequence with all elements when until param is not specified" in new WithCursors {
      cursor.process { c =>
        c.getString(1)
      } shouldEqual allValues.map(_(1))
    }

    "return a sequence with one element for a one element cursor when until param is not specified" in new WithCursors {
      oneRowCursor.process { c =>
        c.getString(1)
      } shouldEqual Seq(allValues.head(1))
    }

    "return an empty sequence for a cursor without rows" in new WithCursors {
      emptyCursor.process { c =>
        c.getString(1)
      } shouldEqual Seq.empty
    }

    "return a sequence with 1 elements when pass 1 as until param" in new WithCursors {
      cursor.process (c => c.getString(1), Some(1)) shouldEqual Seq(allValues.head(1))
    }

    "return an empty sequence when pass 0 as until param" in new WithCursors {
      cursor.process (c => c.getString(1), Some(0)) shouldEqual Seq.empty
    }

  }

  "processOne for Cursor" should {

    "return a sequence with one element" in new WithCursors {
      cursor.processOne { c =>
        c.getString(1)
      } shouldEqual allValues.headOption.map(_(1))
    }

    "return a sequence with one element for a one element cursor" in new WithCursors {
      oneRowCursor.processOne { c =>
        c.getString(1)
      } shouldEqual allValues.headOption.map(_(1))
    }

    "return an empty sequence for a cursor without rows" in new WithCursors {
      emptyCursor.processOne { c =>
        c.getString(1)
      } shouldEqual None
    }

  }


}
