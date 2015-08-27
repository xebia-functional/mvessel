package com.fortysevendeg.mvessel

import java.sql.{Driver => SQLDriver, _}
import java.util.Properties
import java.util.logging.Logger

import com.fortysevendeg.mvessel.BaseDriver._
import com.fortysevendeg.mvessel.util.ConnectionStringParser

trait BaseDriver
  extends SQLDriver
  with ConnectionStringParser {

  override def acceptsURL(url: String): Boolean =
    Option(url) exists (_.startsWith(sqlitePrefix))

  override def jdbcCompliant(): Boolean = false

  override def getPropertyInfo(url: String, info: Properties): scala.Array[DriverPropertyInfo] = scala.Array.empty

  override def getMinorVersion: Int = 0

  override def getMajorVersion: Int = 1

  override def getParentLogger: Logger =
    throw new SQLFeatureNotSupportedException

  def connect(url: String, properties: Properties): Connection
}

object BaseDriver {

  val sqlitePrefix = "jdbc:sqlite:"

  val timeoutParam = "timeout"

  val retryParam = "retry"

}
