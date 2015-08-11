package com.fortysevendeg.mvessel.util

import java.sql.ResultSet

import android.database.Cursor
import StructureControlProcessor._

object StructureControlOps {

  implicit class CursorOps(cursor: Cursor) {

    def process[T](process: Cursor => T, until: Option[Int] = None) = processStructureControl(cursor)(process, until)

    def processOne[T](process: Cursor => T) = processStructureControl(cursor)(process, Some(1)).headOption

  }

  implicit class ResultSetOps(resultSet: ResultSet) {

    def process[T](process: ResultSet => T, until: Option[Int] = None) = processStructureControl(resultSet)(process, until)

    def processOne[T](process: ResultSet => T) = processStructureControl(resultSet)(process, Some(1)).headOption

  }

}
