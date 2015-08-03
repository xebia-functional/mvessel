package com.fortysevendeg.mvessel.resultset

import android.database.{Cursor, MatrixCursor}
import com.fortysevendeg.mvessel.TestLogWrapper
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.specification.Scope

import scala.util.Random

trait ResultSetMetaDataSpecification
  extends Specification
  with Mockito {

  trait WithCursorMocked
    extends Scope {

    val cursor = mock[Cursor]

    val resultSetMetaData = new ResultSetMetaData(cursor, new TestLogWrapper)

  }

  trait WithMatrixCursor
    extends Scope {

    val columnNames = Array("column1", "column2", "column3")

    val cursor = new MatrixCursor(columnNames)

    val resultSetMetaData = new ResultSetMetaData(cursor, new TestLogWrapper)

  }

  trait WithData {

    self: WithMatrixCursor =>

    1 to 10 foreach { _ =>
      cursor.addRow(Array[AnyRef](
        Random.nextString(10),
        new java.lang.Integer(Random.nextInt(10)),
        new java.lang.Float(Random.nextFloat())))
    }

  }

}
