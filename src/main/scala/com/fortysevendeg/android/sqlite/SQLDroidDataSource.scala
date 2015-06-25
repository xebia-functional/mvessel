package com.fortysevendeg.android.sqlite

import java.io.PrintWriter
import java.sql.{Connection, DriverManager}
import java.util.Properties
import java.util.logging.Logger
import javax.sql.DataSource

import scala.util.{Failure, Success, Try}

class SQLDroidDataSource(
  driver: SQLDroidDriver,
  properties: Properties = new Properties,
  dbPath: String,
  log: LogWrapper = new AndroidLogWrapper) extends DataSource {

  private[this] val url: String = "jdbc:sqldroid:" + dbPath

  lazy val connection: Connection = driver.connect(url, properties)

  def getConnection: Connection = connection

  def getConnection(username: String, password: String): Connection = getConnection

  def getLogWriter: PrintWriter =
    Try {
      new PrintWriter("droid.log")
    } match {
      case Success(pw) => pw
      case Failure(e) =>
        e.printStackTrace()
        javaNull
    }

  def setLogWriter(out: PrintWriter) =
    Try {
      DriverManager.setLogWriter(out)
    } match {
      case Failure(e) => e.printStackTrace()
      case _ =>
    }

  def getParentLogger: Logger = log.notImplemented(javaNull)

  def getLoginTimeout: Int = log.notImplemented(0)

  def setLoginTimeout(seconds: Int) = log.notImplemented(Unit)

  def isWrapperFor(iface: Class[_]): Boolean =
    throw new UnsupportedOperationException

  def unwrap[T](iface: Class[T]): T =
    throw new UnsupportedOperationException
}
