package com.fortysevendeg.android.sqlite

import java.io.PrintWriter
import java.sql.{Connection, DriverManager}
import java.util.Properties
import java.util.logging.Logger
import javax.sql.DataSource

import org.sqldroid.SQLDroidDriver

import scala.util.{Failure, Success, Try}

class DroidDataSource(
  packageName: String,
  databaseName: String,
  description: String = "Android SQLite Data Source") extends DataSource {

    private[this] val url: String = "jdbc:sqldroid:" + "/data/data/" + packageName + "/" + databaseName + ".db"

    lazy val connection: Connection = new SQLDroidDriver().connect(url, new Properties)

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

    def getLoginTimeout: Int = 0

    def setLogWriter(out: PrintWriter) =
        Try {
            DriverManager.setLogWriter(out)
        } match {
            case Failure(e) => e.printStackTrace()
            case _ =>
        }

    def setLoginTimeout(seconds: Int) = {}

    def isWrapperFor(iface: Class[_]): Boolean = throw new UnsupportedOperationException("isWrapperfor")

    def unwrap[T](iface: Class[T]): T = throw new UnsupportedOperationException("unwrap")

    def getParentLogger: Logger = javaNull
}
