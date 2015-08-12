package com.fortysevendeg.mvessel

import java.sql.{Driver => SQLDriver, _}
import java.util.Properties
import java.util.logging.Logger

import com.fortysevendeg.mvessel.Connection._
import com.fortysevendeg.mvessel.Driver._
import com.fortysevendeg.mvessel.util.ConnectionStringParser
import com.fortysevendeg.mvessel.util.DatabaseUtils.WrapSQLException

import scala.util.{Failure, Try}

class Driver extends SQLDriver with ConnectionStringParser {

  override def acceptsURL(url: String): Boolean =
    Option(url) exists (_.startsWith(sqlitePrefix))

  override def jdbcCompliant(): Boolean = false

  override def getPropertyInfo(url: String, info: Properties): scala.Array[DriverPropertyInfo] = scala.Array.empty

  override def getMinorVersion: Int = 0

  override def getMajorVersion: Int = 1

  override def getParentLogger: Logger =
    throw new SQLFeatureNotSupportedException

  override def connect(connectionUrl: String, properties: Properties): Connection =
    WrapSQLException(parseConnectionString(connectionUrl), s"Can't parse $connectionUrl") { values =>
      new Connection(
        databaseName = values.name,
        timeout = readTimeOut(values.params) getOrElse defaultTimeout,
        retryInterval = readRetry(values.params) getOrElse defaultRetryInterval,
        flags = readFlags(properties))
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

object Driver {

  def registerDriver() =
    Try(java.sql.DriverManager.registerDriver(new Driver)) match {
      case Failure(e) => throw new SQLException(e)
      case _ =>
    }

  val driverName = BuildInfo.name

  val driverVersion = BuildInfo.version

  val databaseFlags = "DatabaseFlags"

  val additionalDatabaseFlags = "AdditionalDatabaseFlags"

  val sqlitePrefix = "jdbc:sqlite:"

  val timeoutParam = "timeout"

  val retryParam = "retry"

}
