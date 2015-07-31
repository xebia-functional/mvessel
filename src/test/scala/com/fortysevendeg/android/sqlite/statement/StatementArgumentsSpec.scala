package com.fortysevendeg.android.sqlite.statement

import java.sql.SQLException

import com.fortysevendeg.android.sqlite._
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.specification.Scope

trait StatementArgumentsSpecification
  extends Specification
  with Mockito {

  trait StatementArgumentsScope
    extends Scope {

  }

  trait WithStatementArgument
    extends StatementArgumentsScope {

    val arguments = new StatementArguments {}

  }

  trait WithPreparedStatementArgument
    extends StatementArgumentsScope {

    val arguments = new PreparedStatementArguments

  }

  class CustomType

}

class StatementArgumentsSpec
  extends StatementArgumentsSpecification {

  "StatementArgument" should {

    "return an empty array when call toArray" in
      new WithStatementArgument {
        arguments.toArray must beEmpty
      }

    "return an empty array when call toStringArray" in
      new WithStatementArgument {
        arguments.toStringArray must beEmpty
      }

  }

  "PreparedStatementArgument" should {

    "iterate one time when call map" in
      new WithPreparedStatementArgument {
        arguments.map(_ => "") shouldEqual Seq("")
      }

    "iterate two times when call map and after add a new entry" in
      new WithPreparedStatementArgument {
        arguments.addNewEntry()
        arguments.map(_ => "") shouldEqual Seq("", "")
      }

    "iterate one time when call map and after add a new entry and clear arguments" in
      new WithPreparedStatementArgument {
        arguments.addNewEntry()
        arguments.clearArguments()
        arguments.map(_ => "") shouldEqual Seq("")
      }

    "return an array with one null element when call toArray" in
      new WithPreparedStatementArgument {
        arguments.toArray shouldEqual Array(javaNull)
      }

    "return an array with a number of elements equal to the position used in setObjectArgument when call toArray" in
      new WithPreparedStatementArgument {
        arguments.setObjectArgument(3, javaNull)
        arguments.toArray shouldEqual Array(javaNull, javaNull, javaNull)
      }

    "return an array with one null element when call toStringArray" in
      new WithPreparedStatementArgument {
        arguments.toStringArray shouldEqual Array(javaNull)
      }

    "return an array with a number of elements equal to the position used in setObjectArgument when call toStringArray" in
      new WithPreparedStatementArgument {
        arguments.setObjectArgument(3, javaNull)
        arguments.toStringArray shouldEqual Array(javaNull, javaNull, javaNull)
      }

    "return an array with a number of elements equal to the position used in setArgument when call toStringArray" in
      new WithPreparedStatementArgument {
        arguments.setArgument(3, "")
        arguments.toStringArray shouldEqual Array(javaNull, javaNull, "")
      }

    "throws a SQLException when call setObjectArgument with a position less than 1" in
      new WithPreparedStatementArgument {
        arguments.setObjectArgument(0, javaNull) must throwA[SQLException]
      }

    "add the byte array when call to setObjectArgument with a byte array" in
      new WithPreparedStatementArgument {
        val argument = Array(1.toByte)
        arguments.setObjectArgument(1, argument)
        arguments.toArray shouldEqual Array(argument)
      }

    "add the byte array when call to setArgument with a byte array" in
      new WithPreparedStatementArgument {
        val argument = Array(1.toByte)
        arguments.setArgument(1, argument)
        arguments.toArray shouldEqual Array(argument)
      }

    "add the Byte when call to setObjectArgument with a Byte" in
      new WithPreparedStatementArgument {
        val argument = 1.toByte
        arguments.setObjectArgument(1, argument)
        arguments.toArray shouldEqual Array(argument)
      }

    "add the Byte when call to setArgument with a Byte" in
      new WithPreparedStatementArgument {
        val argument = 1.toByte
        arguments.setArgument(1, argument)
        arguments.toArray shouldEqual Array(argument)
      }

    "add a positive number when call to setObjectArgument with a true Boolean" in
      new WithPreparedStatementArgument {
        val argument = true
        arguments.setObjectArgument(1, argument)
        arguments.toArray shouldEqual Array(1)
      }

    "add a zero when call to setObjectArgument with a false Boolean" in
      new WithPreparedStatementArgument {
        val argument = false
        arguments.setObjectArgument(1, argument)
        arguments.toArray shouldEqual Array(0)
      }

    "add a positive number when call to setArgument with a true Boolean" in
      new WithPreparedStatementArgument {
        val argument = true
        arguments.setArgument(1, argument)
        arguments.toArray shouldEqual Array(1)
      }

    "add a zero when call to setArgument with a false Boolean" in
      new WithPreparedStatementArgument {
        val argument = false
        arguments.setArgument(1, argument)
        arguments.toArray shouldEqual Array(0)
      }

    "add the Double when call to setObjectArgument with a Double" in
      new WithPreparedStatementArgument {
        val argument = 1.toDouble
        arguments.setObjectArgument(1, argument)
        arguments.toArray shouldEqual Array(argument)
      }

    "add the Double when call to setArgument with a Double" in
      new WithPreparedStatementArgument {
        val argument = 1.toDouble
        arguments.setArgument(1, argument)
        arguments.toArray shouldEqual Array(argument)
      }

    "add the Long when call to setObjectArgument with a Long" in
      new WithPreparedStatementArgument {
        val argument = 1.toLong
        arguments.setObjectArgument(1, argument)
        arguments.toArray shouldEqual Array(argument)
      }

    "add the Long when call to setArgument with a Long" in
      new WithPreparedStatementArgument {
        val argument = 1.toLong
        arguments.setArgument(1, argument)
        arguments.toArray shouldEqual Array(argument)
      }

    "add the Short when call to setObjectArgument with a Short" in
      new WithPreparedStatementArgument {
        val argument = 1.toShort
        arguments.setObjectArgument(1, argument)
        arguments.toArray shouldEqual Array(argument)
      }

    "add the Short when call to setArgument with a Short" in
      new WithPreparedStatementArgument {
        val argument = 1.toShort
        arguments.setArgument(1, argument)
        arguments.toArray shouldEqual Array(argument)
      }

    "add the Float when call to setObjectArgument with a Float" in
      new WithPreparedStatementArgument {
        val argument = 1.toFloat
        arguments.setObjectArgument(1, argument)
        arguments.toArray shouldEqual Array(argument)
      }

    "add the Float when call to setArgument with a Float" in
      new WithPreparedStatementArgument {
        val argument = 1.toFloat
        arguments.setArgument(1, argument)
        arguments.toArray shouldEqual Array(argument)
      }

    "add the String when call to setObjectArgument with a String" in
      new WithPreparedStatementArgument {
        val argument = "string"
        arguments.setObjectArgument(1, argument)
        arguments.toArray shouldEqual Array(argument)
      }

    "add the String when call to setArgument with a String" in
      new WithPreparedStatementArgument {
        val argument = "string"
        arguments.setArgument(1, argument)
        arguments.toArray shouldEqual Array(argument)
      }

    "add the time in millis when call to setObjectArgument with a java.sql.Time" in
      new WithPreparedStatementArgument {
        val millis = System.currentTimeMillis()
        arguments.setObjectArgument(1, new java.sql.Time(millis))
        arguments.toArray shouldEqual Array(millis)
      }

    "add the time in millis when call to setArgument with a java.sql.Time" in
      new WithPreparedStatementArgument {
        val millis = System.currentTimeMillis()
        arguments.setArgument(1, new java.sql.Time(millis))
        arguments.toArray shouldEqual Array(millis)
      }

    "add the time in millis when call to setObjectArgument with a java.sql.Date" in
      new WithPreparedStatementArgument {
        val millis = System.currentTimeMillis()
        arguments.setObjectArgument(1, new java.sql.Date(millis))
        arguments.toArray shouldEqual Array(millis)
      }

    "add the time in millis when call to setArgument with a java.sql.Date" in
      new WithPreparedStatementArgument {
        val millis = System.currentTimeMillis()
        arguments.setArgument(1, new java.sql.Date(millis))
        arguments.toArray shouldEqual Array(millis)
      }

    "add the time in millis when call to setObjectArgument with a java.sql.Timestamp" in
      new WithPreparedStatementArgument {
        val millis = System.currentTimeMillis()
        arguments.setObjectArgument(1, new java.sql.Timestamp(millis))
        arguments.toArray shouldEqual Array(millis)
      }

    "add the time in millis when call to setArgument with a java.sql.Timestamp" in
      new WithPreparedStatementArgument {
        val millis = System.currentTimeMillis()
        arguments.setArgument(1, new java.sql.Timestamp(millis))
        arguments.toArray shouldEqual Array(millis)
      }

    "add a null when call to setObjectArgument with a null" in
      new WithPreparedStatementArgument {
        arguments.setObjectArgument(1, javaNull)
        arguments.toArray shouldEqual Array(javaNull)
      }

    "throws a SQLException when call to setObjectArgument with a custom type" in
      new WithPreparedStatementArgument {
        arguments.setObjectArgument(1, new CustomType) must throwA[SQLException]
      }

  }

}
