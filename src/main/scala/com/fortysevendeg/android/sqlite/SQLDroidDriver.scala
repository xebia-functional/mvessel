package com.fortysevendeg.android.sqlite

import java.sql.{SQLFeatureNotSupportedException, DriverPropertyInfo, Connection, Driver}
import java.util.Properties
import java.util.logging.Logger
import SQLDroidDriver._
import org.sqldroid.SQLDroidConnection

import scala.util.{Failure, Success, Try}

class SQLDroidDriver extends Driver {

  override def acceptsURL(url: String): Boolean =
    url.startsWith(sqlDroidPrefix) || url.startsWith(sqlitePrefix)

  override def jdbcCompliant(): Boolean = false

  override def getPropertyInfo(url: String, info: Properties): Array[DriverPropertyInfo] = Array.empty

  override def getMinorVersion: Int = 0

  override def getMajorVersion: Int = 1

  override def getParentLogger: Logger =
    throw new SQLFeatureNotSupportedException

  override def connect(connectionUrl: String, properties: Properties): Connection =
    new SQLDroidConnection(connectionUrl, properties)
}

object SQLDroidDriver {

  Try {
    java.sql.DriverManager.registerDriver(new SQLDroidDriver)
  } match {
    case Success(_) =>
    case Failure(e) => e.printStackTrace()
  }
  
  val databaseFlags = "DatabaseFlags"
  
  val additionalDatabaseFlags = "AdditionalDatabaseFlags"

  val sqlDroidPrefix = "jdbc:sqldroid:"

  val sqlitePrefix = "jdbc:sqlite:"

  val timeoutParam = "timeout"

  val retryParam = "retry"
  
}
