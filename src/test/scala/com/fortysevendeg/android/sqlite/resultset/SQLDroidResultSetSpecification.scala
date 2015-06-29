package com.fortysevendeg.android.sqlite.resultset

import java.io.{BufferedReader, Reader, InputStream}
import java.text.SimpleDateFormat
import java.util.Date

import android.database.{Cursor, MatrixCursor}
import com.fortysevendeg.android.sqlite._
import com.fortysevendeg.android.sqlite.util.DateUtils
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.specification.Scope

import scala.util.Random

trait SQLDroidResultSetSpecification
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

    val cursor = mock[Cursor]

    val sqlDroid = new SQLDroidResultSet(cursor, log)

  }

  trait WithMatrixCursor
    extends Scope {

    val columnNames = Array("column1", "column2", "column3", "column4", "column5")

    val columnString = 1

    val columnInteger = 2

    val columnFloat = 3

    val columnBytes = 4

    val columnNull = 5

    val nonExistentColumnName = "column0"

    val cursor = new MatrixCursor(columnNames)

    val sqlDroid = new SQLDroidResultSet(cursor, log)

  }

  trait WithData {

    self: WithMatrixCursor =>

    val numRows = 10

    val rows = 1 to numRows map { _ =>
      val row = Array[AnyRef](
        Random.nextString(10),
        new java.lang.Integer(Random.nextInt(10)),
        new java.lang.Float(Random.nextFloat()),
        Random.nextString(10).getBytes,
        javaNull)
      cursor.addRow(row)
      row
    }
  }

}
