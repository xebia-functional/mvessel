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

import java.sql.{SQLException, ResultSet}

import com.fortysevendeg.mvessel.util.ResultSetProcessorOps._
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.specification.Scope

import scala.util.Random

trait StructureControlOpsSpecification
  extends Specification
  with Mockito {

  val total = 10

  val allValues: Seq[Array[AnyRef]] = (1 to total map { i =>
    Array(i.asInstanceOf[AnyRef], s"name $i", new java.lang.Integer(Random.nextInt(99)))
  }).toSeq

  trait StructureControlOps
    extends Scope

  trait WithResultSets
    extends StructureControlOps {

    val resultSet = mock[ResultSet]
    resultSet.next() returns (true, Seq.fill(total - 1)(true) :+ false :_*)
    resultSet.getString(1) returns (allValues.head(1).toString, allValues.slice(1, total) map (_(1).toString) :_*)

    val oneRowResultSet = mock[ResultSet]
    oneRowResultSet.next() returns (true, false)
    oneRowResultSet.getString(1) returns allValues.head(1).toString

    val emptyResultSet = mock[ResultSet]
    emptyResultSet.next() returns false

  }

}

class StructureControlOpsSpec
  extends StructureControlOpsSpecification {

  "process for ResultSet" should {

    "return a sequence with all elements when until param is not specified" in new WithResultSets {
      resultSet.process { c =>
        c.getString(1)
      } shouldEqual allValues.map(_(1))
    }

    "return a sequence with one element for a one element cursor when until param is not specified" in new WithResultSets {
      oneRowResultSet.process { c =>
        c.getString(1)
      } shouldEqual Seq(allValues.head(1))
    }

    "return an empty sequence for a cursor without rows" in new WithResultSets {
      emptyResultSet.process { c =>
        c.getString(1)
      } shouldEqual Seq.empty
    }

    "return a sequence with 1 elements when pass 1 as until param" in new WithResultSets {
      resultSet.process (c => c.getString(1), Some(1)) shouldEqual Seq(allValues.head(1))
    }

    "return an empty sequence when pass 0 as until param" in new WithResultSets {
      resultSet.process (c => c.getString(1), Some(0)) shouldEqual Seq.empty
    }

    "throws the same exception as the process body and call close" in new WithResultSets {
      val exception = new SQLException("Error")
      resultSet.process { c =>
        throw exception
      } must throwA(exception)
      there was one(resultSet).close()
    }

    "throws the same exception as the process body and don't call close if is already closed" in new WithResultSets {
      val exception = new SQLException("Error")
      resultSet.isClosed returns true
      resultSet.process { c =>
        throw exception
      } must throwA(exception)
      there was no(resultSet).close()
    }

  }

  "processOne for ResultSet" should {

    "return a sequence with one element" in new WithResultSets {
      resultSet.processOne { c =>
        c.getString(1)
      } shouldEqual allValues.headOption.map(_(1))
    }

    "return a sequence with one element for a one element cursor" in new WithResultSets {
      oneRowResultSet.processOne { c =>
        c.getString(1)
      } shouldEqual allValues.headOption.map(_(1))
    }

    "return an empty sequence for a cursor without rows" in new WithResultSets {
      emptyResultSet.processOne { c =>
        c.getString(1)
      } shouldEqual None
    }

  }


}
