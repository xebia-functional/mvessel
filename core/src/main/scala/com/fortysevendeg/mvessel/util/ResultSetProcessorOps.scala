package com.fortysevendeg.mvessel.util

import java.sql.ResultSet

import com.fortysevendeg.mvessel.util.StructureControlProcessor._

import scala.util.Try

object ResultSetProcessorOps {

  implicit def `ResultSet processor` = new StructureControlProcessor[ResultSet] {

    def move(resultSet: ResultSet): Boolean = resultSet.next()

    def close(resultSet: ResultSet): Unit = Try(resultSet.close())

    def isClosed(resultSet: ResultSet): Boolean = resultSet.isClosed

  }

  implicit class ResultSetOps(resultSet: ResultSet) {

    def process[T](process: ResultSet => T, until: Option[Int] = None) = processStructureControl(resultSet)(process, until)

    def processOne[T](process: ResultSet => T) = processStructureControl(resultSet)(process, Some(1)).headOption

  }

}
