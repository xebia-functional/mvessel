package com.fortysevendeg.android.sqlite

import java.sql._
import java.util.Properties
import java.util.logging.Logger
import SQLDroidDriver._
import SQLDroidConnection._
import com.fortysevendeg.android.sqlite.util.ConnectionStringParser._

import scala.util.{Success, Failure, Try}

class SQLDroidDriver extends Driver {

  override def acceptsURL(url: String): Boolean =
    Option(url) exists (u => u.startsWith(sqlDroidPrefix) || u.startsWith(sqlitePrefix))

  override def jdbcCompliant(): Boolean = false

  override def getPropertyInfo(url: String, info: Properties): scala.Array[DriverPropertyInfo] = scala.Array.empty

  override def getMinorVersion: Int = 0

  override def getMajorVersion: Int = 1

  override def getParentLogger: Logger =
    throw new SQLFeatureNotSupportedException

  override def connect(connectionUrl: String, properties: Properties): Connection =
    parseConnectionString(connectionUrl) match {
      case Some(values) =>
        new SQLDroidConnection(
          databaseName = values.name,
          timeout = readTimeOut(values.params) getOrElse defaultTimeout,
          retryInterval = readRetry(values.params) getOrElse defaultRetryInterval,
          flags = readFlags(properties))
      case _ =>
        throw new SQLException(s"Can't parse $connectionUrl")
    }

  private[this] def readTimeOut(params: Map[String, String]): Option[Long] =
    readLongParam("timeout", params)

  private[this] def readRetry(params: Map[String, String]): Option[Int] =
    readLongParam("retry", params) map (_.toInt)

  private[this] def readLongParam(name: String, params: Map[String, String]): Option[Long] =
    params.get(name) flatMap (value => Try(value.toLong).toOption)

  val flags =
    android.database.sqlite.SQLiteDatabase.CREATE_IF_NECESSARY |
      android.database.sqlite.SQLiteDatabase.OPEN_READWRITE |
      android.database.sqlite.SQLiteDatabase.NO_LOCALIZED_COLLATORS

  private[this] def readFlags(properties: Properties): Int =
    Option(properties) match {
      case Some(p) => completeFlags(p.getProperty(databaseFlags), p.getProperty(additionalDatabaseFlags))
      case _ => flags
    }

  private[this] def completeFlags(databaseFlags: String, androidFlags: String): Int =
    (Option(databaseFlags), Option(androidFlags)) match {
      case (Some(f), _) => parseInt(f, flags)
      case (None, Some(f)) => flags | parseInt(f, 0)
      case _ => flags
    }

  private[this] def parseInt(value: String, defaultValue: Int) =
    Try(value.toInt).toOption getOrElse defaultValue
}

object SQLDroidDriver {

  Try {
    java.sql.DriverManager.registerDriver(new SQLDroidDriver)
  } match {
    case Failure(e) => e.printStackTrace()
    case _ =>
  }

  val driverName = BuildInfo.name

  val driverVersion = BuildInfo.version

  val databaseFlags = "DatabaseFlags"

  val additionalDatabaseFlags = "AdditionalDatabaseFlags"

  val sqlDroidPrefix = "jdbc:sqldroid:"

  val sqlitePrefix = "jdbc:sqlite:"

  val timeoutParam = "timeout"

  val retryParam = "retry"

}
