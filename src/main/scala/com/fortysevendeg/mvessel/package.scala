package com.fortysevendeg

package object mvessel {

  val javaNull = null

  implicit class IndexOps(columnIndex: Int) {
    def index: Int = columnIndex - 1
  }

  implicit class SQLStringOps(string: String) {
    def escape: String = string.replace("'", "''")
  }

}
