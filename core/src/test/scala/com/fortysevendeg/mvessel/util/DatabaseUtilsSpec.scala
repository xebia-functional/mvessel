package com.fortysevendeg.mvessel.util

import java.sql.{Statement, Connection, SQLException}

import com.fortysevendeg.mvessel.util.DatabaseUtils.{WithStatement, WrapSQLException}
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.specification.Scope

trait DatabaseUtilsSpecification
  extends Specification
  with Mockito {

  trait WrapSQLExceptionScope
    extends Scope {

    val intValue = 2
    val function: (Int) => Int = _ * 2

    val message = "Message"

    val exception = new RuntimeException("Error")
    val functionThrowable: (Int) => Int = _ => throw exception
  }

  trait WithStatementScope
    extends Scope {

    val connection = mock[Connection]

    val statement = mock[Statement]

    connection.createStatement() returns statement

    val function: (Statement) => Int = _ => 10

    val exception = new RuntimeException("Error")
    val functionThrowable: (Statement) => Int = _ => throw exception

  }

}

class DatabaseUtilsSpec
  extends DatabaseUtilsSpecification {

  "WrapSQLException" should {

    "return the result of the function if the option is defined" in
      new WrapSQLExceptionScope {
        WrapSQLException(Some(intValue), "")(function) shouldEqual function(intValue)
      }

    "throws a SQLException with the message if the option is not defined" in
      new WrapSQLExceptionScope {
        WrapSQLException(None, message)(_ => ()) must throwA[SQLException](message)
      }

    "throw a SQLException with a cause equal to the exception thrown by the function" in
      new WrapSQLExceptionScope {
        WrapSQLException(Some(intValue), message)(functionThrowable) must throwA.like {
          case e: SQLException => e.getCause shouldEqual exception
        }
      }

  }

  "WithStatement" should {

    "return the result of the function and close the generated statement" in
      new WithStatementScope {
        WithStatement(function)(connection) shouldEqual function(statement)
        there was one(statement).close()
      }

    "throws the exception produced by the function closing the generated statement before" in
      new WithStatementScope {
        WithStatement(functionThrowable)(connection) must throwA(exception)
        there was one(statement).close()
      }

  }

}
