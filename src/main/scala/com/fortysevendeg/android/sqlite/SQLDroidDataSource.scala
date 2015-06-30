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
  log: LogWrapper = new AndroidLogWrapper)
  extends DataSource
  with WrapperNotSupported {

  private[this] val url: String = "jdbc:sqldroid:" + dbPath

  lazy val connection: Connection = driver.connect(url, properties)

  override def getConnection: Connection = connection

  override def getConnection(username: String, password: String): Connection = getConnection

  override def getLogWriter: PrintWriter =
    Try {
      new PrintWriter("droid.log")
    } match {
      case Success(pw) => pw
      case Failure(e) =>
        log.e("Can't create log writer", Some(e))
        javaNull
    }

  override def setLogWriter(out: PrintWriter) =
    Try {
      DriverManager.setLogWriter(out)
    } match {
      case Failure(e) => log.e("Error setting log writer", Some(e))
      case _ =>
    }

  override def getParentLogger: Logger = log.notImplemented(javaNull)

  override def getLoginTimeout: Int = log.notImplemented(0)

  override def setLoginTimeout(seconds: Int) = log.notImplemented(Unit)
}
