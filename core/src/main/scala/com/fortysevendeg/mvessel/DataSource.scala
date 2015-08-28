package com.fortysevendeg.mvessel

import java.io.PrintWriter
import java.sql.{Connection => SQLConnection, DriverManager}
import java.util.Properties
import java.util.logging.Logger
import javax.sql.{DataSource => SQLDataSource}

import com.fortysevendeg.mvessel.logging.LogWrapper

import scala.util.{Failure, Success, Try}

class DataSource(
  driver: BaseDriver,
  properties: Properties = new Properties,
  dbPath: String,
  log: LogWrapper)
  extends SQLDataSource
  with WrapperNotSupported {

  private[this] val url: String = "jdbc:sqlite:" + dbPath

  lazy val connection: Connection = driver.connect(url, properties)

  override def getConnection: SQLConnection = connection

  override def getConnection(username: String, password: String): SQLConnection = getConnection

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
