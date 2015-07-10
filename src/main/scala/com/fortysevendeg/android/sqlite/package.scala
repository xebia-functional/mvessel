package com.fortysevendeg.android

package object sqlite {

  val javaNull = null

  implicit class IndexOps(columnIndex: Int) {
    def index: Int = columnIndex - 1
  }

  implicit class SQLStringOps(string: String) {
    def escape: String = string.replace("'", "''")
  }

}
