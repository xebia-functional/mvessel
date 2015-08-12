package com.fortysevendeg.mvessel.util

import java.sql.{SQLException, Connection, Statement}
import scala.util.{Failure, Success, Try}

object DatabaseUtils {

  object WrapSQLException {

    def apply[D, T](option: Option[D], message: String)(f: D => T) = option match {
      case Some(db) =>
        Try(f(db)) match {
          case Success(r) => r
          case Failure(e) => throw new SQLException(e)
        }
      case None =>
        throw new SQLException(message)
    }

  }

  object WithStatement {

    def apply[T](f: Statement => T)(implicit connection: Connection): T = {
      val statement = connection.createStatement()
      Try(f(statement)) match {
        case Success(t) =>
          closeStatement(statement)
          t
        case Failure(e) =>
          closeStatement(statement)
          throw e
      }
    }

    private[this] def closeStatement(statement: Statement) {
      Try(statement.close())
    }

  }

}
