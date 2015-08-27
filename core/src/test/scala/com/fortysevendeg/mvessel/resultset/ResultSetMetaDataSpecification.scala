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
