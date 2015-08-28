package com.fortysevendeg.mvessel

import java.sql.SQLException
import java.util.Properties

import com.fortysevendeg.mvessel.api.impl.AndroidDatabaseFactory
import com.fortysevendeg.mvessel.logging.AndroidLogWrapper
import com.fortysevendeg.mvessel.util.DatabaseUtils.WrapSQLException

import scala.util.{Failure, Try}
import com.fortysevendeg.mvessel.Connection._

class AndroidDriver extends BaseDriver {

  override def connect(connectionUrl: String, properties: Properties): Connection =
    WrapSQLException(parseConnectionString(connectionUrl), s"Can't parse $connectionUrl") { values =>
      new Connection(
        databaseWrapperFactory = new AndroidDatabaseFactory,
        databaseName = values.name,
        timeout = readTimeOut(values.params) getOrElse defaultTimeout,
        retryInterval = readRetry(values.params) getOrElse defaultRetryInterval,
        flags = readFlags(properties),
        logWrapper = new AndroidLogWrapper)
    }

  private[this] def readTimeOut(params: Map[String, String]): Option[Long] =
    readLongParam(BaseDriver.timeoutParam, params)

  private[this] def readRetry(params: Map[String, String]): Option[Int] =
    readLongParam(BaseDriver.retryParam, params) map (_.toInt)

  private[this] def readLongParam(name: String, params: Map[String, String]): Option[Long] =
    params.get(name) flatMap (value => Try(value.toLong).toOption)

  val flags =
    android.database.sqlite.SQLiteDatabase.CREATE_IF_NECESSARY |
      android.database.sqlite.SQLiteDatabase.OPEN_READWRITE |
      android.database.sqlite.SQLiteDatabase.NO_LOCALIZED_COLLATORS

  private[this] def readFlags(properties: Properties): Int =
    Option(properties) match {
      case Some(p) =>
        completeFlags(
          p.getProperty(AndroidDriver.databaseFlags),
          p.getProperty(AndroidDriver.additionalDatabaseFlags))
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

object AndroidDriver {

  def register() =
    Try(java.sql.DriverManager.registerDriver(new AndroidDriver)) match {
      case Failure(e) => throw new SQLException(e)
      case _ =>
    }

  val driverName = BuildInfo.name

  val driverVersion = BuildInfo.version

  val databaseFlags = "DatabaseFlags"

  val additionalDatabaseFlags = "AdditionalDatabaseFlags"

}