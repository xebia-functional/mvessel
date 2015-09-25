/*
 * The MIT License (MIT)
 *
 * Copyright (C) 2012 47 Degrees, LLC http://47deg.com hello@47deg.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 */

package com.fortysevendeg.mvessel.statement

import java.sql.SQLException

import com.fortysevendeg.mvessel._
import com.fortysevendeg.mvessel.api.{CursorProxy, DatabaseProxyFactory, DatabaseProxy}
import com.fortysevendeg.mvessel.logging.LogWrapper
import com.fortysevendeg.mvessel.resultset.ResultSet
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.specification.Scope

trait StatementSpecification
  extends Specification
  with Mockito {

  val selectSql = "SELECT * FROM table;"

  val insertSql = "INSERT INTO table(id, name) VALUES(1, 'name');"

  trait StatementScope extends Scope {

    val databaseName = "databaseName"

    val logWrapper = new TestLogWrapper

    val databaseProxy = mock[DatabaseProxy[CursorProxy]]

    val databaseProxyFactory = mock[DatabaseProxyFactory[CursorProxy]]

    databaseProxyFactory.openDatabase(any, any) returns databaseProxy

    databaseProxy.isOpen returns true

    val connection: Connection[CursorProxy] = new Connection(
      databaseWrapperFactory = databaseProxyFactory,
      databaseName = databaseName,
      logWrapper = logWrapper)

  }

  trait WithColumnGenerated extends StatementScope {

    val columnGenerated = "ColumnGeneratedName"

    val statement = new Statement(
      sqlConnection = connection,
      columnGenerated = Some(columnGenerated),
      logWrapper = new TestLogWrapper())
  }

  trait WithoutConnection extends StatementScope {

    val columnGenerated = None

    override val connection = javaNull

    val statement = new Statement(
      sqlConnection = javaNull,
      columnGenerated = None,
      logWrapper = new TestLogWrapper())
  }

  trait WithoutColumnGenerated extends StatementScope {

    val statement = new Statement[CursorProxy](
      sqlConnection = connection,
      columnGenerated = None,
      logWrapper = new TestLogWrapper())
  }

  trait WithMockLogger extends StatementScope {

    val mockLog = mock[LogWrapper]

    val statement = new Statement(
      sqlConnection = connection,
      columnGenerated = None,
      logWrapper = mockLog)
  }

}

class StatementSpec
  extends StatementSpecification {

  "addBatch" should {

    "add the string to the SQL batch" in new WithoutColumnGenerated {
      statement.addBatch(selectSql)
      statement.getBatchList shouldEqual Seq(selectSql)
    }

  }

  "clearBatch" should {

    "clear the SQL batch" in new WithoutColumnGenerated {
      statement.addBatch(selectSql)
      statement.clearBatch()
      statement.getBatchList shouldEqual Seq.empty
    }
  }

  "close and isClose" should {

    "return true when call isClose after a call to close" in new WithoutColumnGenerated {
      statement.close()
      statement.isClosed must beTrue
    }

    "return false when call isClose if no call to close has been made" in new WithoutColumnGenerated {
      statement.isClosed must beFalse
    }
  }

  "execute and getUpdateCount" should {

    "return true with a select query and -1" in new WithoutColumnGenerated {
      val cursor = mock[CursorProxy]
      databaseProxy.rawQuery(any, any) returns cursor
      statement.execute(selectSql) must beTrue
      statement.getUpdateCount shouldEqual -1
    }

    "return false with a select query and the result of changedRowCount in database" in new WithoutColumnGenerated {
      val changedRowCount = 10
      databaseProxy.changedRowCount returns Some(changedRowCount)
      statement.execute(insertSql) must beFalse
      statement.getUpdateCount shouldEqual changedRowCount
    }

    "throws a SQLException when the connection is close" in new WithoutConnection {
      statement.execute(selectSql) must throwA[SQLException](statement.connectionClosedErrorMessage)
    }

    "call LogWrapper.notImplemented when passing the autGeneratedKeys argument" in new WithMockLogger {
      statement.execute(selectSql, 0)
      there was one(mockLog).notImplemented(false)
    }

    "call LogWrapper.notImplemented when passing the columnIndexes argument" in new WithMockLogger {
      statement.execute(selectSql, scala.Array(0))
      there was one(mockLog).notImplemented(false)
    }

    "call LogWrapper.notImplemented when passing the columnNames argument" in new WithMockLogger {
      statement.execute(selectSql, scala.Array(""))
      there was one(mockLog).notImplemented(false)
    }
  }

  "executeBatch and getUpdateCount" should {

    "return an array with no elements when the batch is empty and -1" in new WithoutColumnGenerated {
      statement.executeBatch() shouldEqual scala.Array()
      statement.getUpdateCount shouldEqual -1
    }

    "return an array with the results of changedRowCount and the sum when the bacth has elements" in new WithoutColumnGenerated {
      val batch = Seq.fill(3)(insertSql)
      val changedRowCounts = Seq(Some(20), Some(30), Some(40))

      databaseProxy.changedRowCount returns(
        changedRowCounts.head,
        changedRowCounts.slice(1, changedRowCounts.size): _*)

      batch foreach statement.addBatch

      statement.executeBatch() shouldEqual changedRowCounts.flatten.toArray
      statement.getUpdateCount shouldEqual changedRowCounts.foldLeft(0)((a, b) => a + b.get)
    }

    "throws a SQLException when the connection is close" in new WithoutConnection {
      statement.executeBatch() must throwA[SQLException](statement.connectionClosedErrorMessage)
    }
  }

  "executeQuery and getResultSet" should {

    "return in both methods the same ResultSet with the Cursor returned by the database" in new WithoutColumnGenerated {
      val cursor = mock[CursorProxy]
      databaseProxy.rawQuery(any, any) returns cursor
      val rs = statement.executeQuery(selectSql)
      rs must beLike {
        case st: ResultSet[_] => st.cursor shouldEqual cursor
      }
      statement.getResultSet shouldEqual rs
    }

    "throws a SQLException when the connection is close" in new WithoutConnection {
      statement.executeQuery(selectSql) must throwA[SQLException](statement.connectionClosedErrorMessage)
    }
  }

  "executeUpdate and getUpdateCount" should {

    "return in both methods the result of changedRowCount in database" in new WithoutColumnGenerated {
      val changedRowCount = 10
      databaseProxy.changedRowCount returns Some(changedRowCount)
      statement.executeUpdate(insertSql) shouldEqual changedRowCount
      statement.getUpdateCount shouldEqual changedRowCount
    }

    "throws a SQLException when the connection is close" in new WithoutConnection {
      statement.executeUpdate(selectSql) must throwA[SQLException](statement.connectionClosedErrorMessage)
    }

    "call LogWrapper.notImplemented when passing the autGeneratedKeys argument" in new WithMockLogger {
      statement.executeUpdate(selectSql, 0)
      there was one(mockLog).notImplemented(0)
    }

    "call LogWrapper.notImplemented when passing the columnIndexes argument" in new WithMockLogger {
      statement.executeUpdate(selectSql, scala.Array(0))
      there was one(mockLog).notImplemented(0)
    }

    "call LogWrapper.notImplemented when passing the columnNames argument" in new WithMockLogger {
      statement.executeUpdate(selectSql, scala.Array(""))
      there was one(mockLog).notImplemented(0)
    }
  }

  "getConnection" should {

    "return the connection when is open" in new WithoutColumnGenerated {
      statement.getConnection shouldEqual connection
    }

    "throws a SQLException when the connection is close" in new WithoutConnection {
      statement.getConnection must throwA[SQLException](statement.connectionClosedErrorMessage)
    }
  }

  "getGeneratedKeys and getResultSet" should {

    "return the same ResultSet with the Cursor returned by the database that receive the column generated name" in
      new WithColumnGenerated {
        val cursor = mock[CursorProxy]
        databaseProxy.rawQuery(contain(columnGenerated), any) returns cursor
        val rs = statement.getGeneratedKeys
        rs must beLike {
          case st: ResultSet[_] => st.cursor shouldEqual cursor
        }
        statement.getResultSet shouldEqual rs
      }

    "throws a SQLException when the connection is close" in new WithoutConnection {
      statement.getGeneratedKeys must throwA[SQLException](statement.connectionClosedErrorMessage)
    }
  }

  "getMaxRows and setMaxRows" should {

    "works as expected when the number is positive and the connection is open" in new WithoutColumnGenerated {
      1 to 10 foreach { max =>
        statement.setMaxRows(max)
        statement.getMaxRows shouldEqual max
      }
    }

    "works as expected when the number is zero and the connection is open" in new WithoutColumnGenerated {
      val max = 0
      statement.setMaxRows(max)
      statement.getMaxRows shouldEqual max
    }

    "throws a SQLException when the number is negative" in new WithoutColumnGenerated {
      statement.setMaxRows(-1) must throwA[SQLException](statement.maxNegativeErrorMessage)
    }

    "throws a SQLException when the connection is closed" in new WithoutConnection {
      statement.setMaxRows(-1) must throwA[SQLException](statement.statementClosedErrorMessage)
    }

  }

  "getMoreResults" should {

    "return false when the call is made without arguments" in new WithoutColumnGenerated {
      statement.getMoreResults must beFalse
    }

    "return false when pass a Int argument" in new WithoutColumnGenerated {
      statement.getMoreResults(10) must beFalse
    }
  }

  "cancel" should {

    "call LogWrapper.notImplemented" in new WithMockLogger {
      statement.cancel()
      there was one(mockLog).notImplemented(Unit)
    }
  }

  "clearWarnings" should {

    "call LogWrapper.notImplemented" in new WithMockLogger {
      statement.clearWarnings()
      there was one(mockLog).notImplemented(Unit)
    }
  }

  "getFetchDirection" should {

    "call LogWrapper.notImplemented" in new WithMockLogger {
      statement.getFetchDirection()
      there was one(mockLog).notImplemented(0)
    }
  }

  "setFetchDirection" should {

    "call LogWrapper.notImplemented" in new WithMockLogger {
      statement.setFetchDirection(10)
      there was one(mockLog).notImplemented(Unit)
    }
  }

  "getFetchSize" should {

    "call LogWrapper.notImplemented" in new WithMockLogger {
      statement.getFetchSize()
      there was one(mockLog).notImplemented(0)
    }
  }

  "setFetchSize" should {

    "call LogWrapper.notImplemented" in new WithMockLogger {
      statement.setFetchSize(10)
      there was one(mockLog).notImplemented(Unit)
    }
  }

  "getMaxFieldSize" should {

    "call LogWrapper.notImplemented" in new WithMockLogger {
      statement.getMaxFieldSize()
      there was one(mockLog).notImplemented(0)
    }
  }

  "setMaxFieldSize" should {

    "call LogWrapper.notImplemented" in new WithMockLogger {
      statement.setMaxFieldSize(10)
      there was one(mockLog).notImplemented(Unit)
    }
  }

  "getQueryTimeout" should {

    "call LogWrapper.notImplemented" in new WithMockLogger {
      statement.getQueryTimeout()
      there was one(mockLog).notImplemented(0)
    }
  }

  "setQueryTimeout" should {

    "call LogWrapper.notImplemented" in new WithMockLogger {
      statement.setQueryTimeout(10)
      there was one(mockLog).notImplemented(Unit)
    }
  }

  "getResultSetConcurrency" should {

    "call LogWrapper.notImplemented" in new WithMockLogger {
      statement.getResultSetConcurrency()
      there was one(mockLog).notImplemented(0)
    }
  }

  "getResultSetHoldability" should {

    "call LogWrapper.notImplemented" in new WithMockLogger {
      statement.getResultSetHoldability()
      there was one(mockLog).notImplemented(0)
    }
  }

  "getResultSetType" should {

    "call LogWrapper.notImplemented" in new WithMockLogger {
      statement.getResultSetType()
      there was one(mockLog).notImplemented(0)
    }
  }

  "getWarnings" should {

    "call LogWrapper.notImplemented" in new WithMockLogger {
      statement.getWarnings()
      there was one(mockLog).notImplemented(javaNull)
    }
  }

  "setCursorName" should {

    "call LogWrapper.notImplemented" in new WithMockLogger {
      statement.setCursorName("cursorName")
      there was one(mockLog).notImplemented(Unit)
    }
  }

  "setEscapeProcessing" should {

    "call LogWrapper.notImplemented" in new WithMockLogger {
      statement.setEscapeProcessing(false)
      there was one(mockLog).notImplemented(Unit)
    }
  }

  "setPoolable" should {

    "call LogWrapper.notImplemented" in new WithMockLogger {
      statement.setPoolable(false)
      there was one(mockLog).notImplemented(Unit)
    }
  }

  "isPoolable" should {

    "call LogWrapper.notImplemented" in new WithMockLogger {
      statement.isPoolable()
      there was one(mockLog).notImplemented(false)
    }
  }

  "isCloseOnCompletion" should {

    "call LogWrapper.notImplemented" in new WithMockLogger {
      statement.isCloseOnCompletion()
      there was one(mockLog).notImplemented(false)
    }
  }

  "closeOnCompletion" should {

    "call LogWrapper.notImplemented" in new WithMockLogger {
      statement.closeOnCompletion()
      there was one(mockLog).notImplemented(Unit)
    }
  }

}
