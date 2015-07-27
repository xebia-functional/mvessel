package com.fortysevendeg.android.sqlite.statement

import java.sql.{ResultSet, SQLException}

import android.database.Cursor
import com.fortysevendeg.android.sqlite.logging.LogWrapper
import com.fortysevendeg.android.sqlite.resultset.SQLDroidResultSet
import com.fortysevendeg.android.sqlite.{TestLogWrapper, SQLDroidConnection}
import com.fortysevendeg.android.sqlite._
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.specification.Scope

trait SQLDroidStatementSpecification
  extends Specification
  with Mockito {

  val selectSql = "SELECT * FROM table;"

  val insertSql = "INSERT INTO table(id, name) VALUES(1, 'name');"

  trait SQLDroidStatementScope extends Scope {

    val database = mock[SQLDroidDatabase]

    val connection: SQLDroidConnection = new SQLDroidConnection(databaseName = "databaseName", logWrapper = new TestLogWrapper) {
      override protected def createDatabase(): Option[SQLDroidDatabase] = Some(database)
    }
  }

  trait WithColumnGenerated extends SQLDroidStatementScope {

    val columnGenerated = "ColumnGeneratedName"

    val sqlDroid = new SQLDroidStatement(
      sqlDroidConnection = connection,
      columnGenerated = Some(columnGenerated),
      logWrapper = new TestLogWrapper())
  }

  trait WithoutConnection extends SQLDroidStatementScope {

    val columnGenerated = None

    override val connection = javaNull

    val sqlDroid = new SQLDroidStatement(
      sqlDroidConnection = javaNull,
      columnGenerated = None,
      logWrapper = new TestLogWrapper())
  }

  trait WithoutColumnGenerated extends SQLDroidStatementScope {

    val sqlDroid = new SQLDroidStatement(
      sqlDroidConnection = connection,
      columnGenerated = None,
      logWrapper = new TestLogWrapper())
  }

  trait WithMockLogger extends SQLDroidStatementScope {

    val mockLog = mock[LogWrapper]

    val sqlDroid = new SQLDroidStatement(
      sqlDroidConnection = connection,
      columnGenerated = None,
      logWrapper = mockLog)
  }

}

class SQLDroidStatementSpec
  extends SQLDroidStatementSpecification {

  "addBatch" should {

    "add the string to the SQL batch" in new WithoutColumnGenerated {
      sqlDroid.addBatch(selectSql)
      sqlDroid.getBatchList shouldEqual Seq(selectSql)
    }

  }

  "clearBatch" should {

    "clear the SQL batch" in new WithoutColumnGenerated {
      sqlDroid.addBatch(selectSql)
      sqlDroid.clearBatch()
      sqlDroid.getBatchList shouldEqual Seq()
    }
  }

  "close and isClose" should {

    "return true when call isClose after a call to close" in new WithoutColumnGenerated {
      sqlDroid.close()
      sqlDroid.isClosed must beTrue
    }

    "return false when call isClose if no call to close has been made" in new WithoutColumnGenerated {
      sqlDroid.isClosed must beFalse
    }
  }

  "execute and getUpdateCount" should {

    "return true with a select query and -1" in new WithoutColumnGenerated {
      val cursor = mock[Cursor]
      database.rawQuery(any) returns cursor
      sqlDroid.execute(selectSql) must beTrue
      sqlDroid.getUpdateCount shouldEqual -1
    }

    "return false with a select query and the result of changedRowCount in database" in new WithoutColumnGenerated {
      val changedRowCount = 10
      database.changedRowCount() returns changedRowCount
      sqlDroid.execute(insertSql) must beFalse
      sqlDroid.getUpdateCount shouldEqual changedRowCount
    }

    "throws a SQLException when the connection is close" in new WithoutConnection {
      sqlDroid.execute(selectSql) must throwA[SQLException](sqlDroid.connectionClosedErrorMessage)
    }

    "call LogWrapper.notImplemented when passing the autGeneratedKeys argument" in new WithMockLogger {
      sqlDroid.execute(selectSql, 0)
      there was one(mockLog).notImplemented(false)
    }

    "call LogWrapper.notImplemented when passing the columnIndexes argument" in new WithMockLogger {
      sqlDroid.execute(selectSql, scala.Array(0))
      there was one(mockLog).notImplemented(false)
    }

    "call LogWrapper.notImplemented when passing the columnNames argument" in new WithMockLogger {
      sqlDroid.execute(selectSql, scala.Array(""))
      there was one(mockLog).notImplemented(false)
    }
  }

  "executeBatch and getUpdateCount" should {

    "return an array with no elements when the batch is empty and -1" in new WithoutColumnGenerated {
      sqlDroid.executeBatch() shouldEqual scala.Array()
      sqlDroid.getUpdateCount shouldEqual -1
    }

    "return an array with the results of changedRowCount and the sum when the bacth has elements" in new WithoutColumnGenerated {
      val batch = Seq(selectSql, selectSql, selectSql)
      val changedRowCounts = Seq(20, 30, 40)

      database.changedRowCount() returns(
        changedRowCounts.head,
        changedRowCounts.slice(1, changedRowCounts.size): _*)

      batch foreach sqlDroid.addBatch

      sqlDroid.executeBatch() shouldEqual changedRowCounts.toArray
      sqlDroid.getUpdateCount shouldEqual changedRowCounts.sum
    }

    "throws a SQLException when the connection is close" in new WithoutConnection {
      sqlDroid.executeBatch() must throwA[SQLException](sqlDroid.connectionClosedErrorMessage)
    }
  }

  "executeQuery and getResultSet" should {

    "return in both methods the same SQLDroidResultSet with the Cursor returned by the database" in new WithoutColumnGenerated {
      val cursor = mock[Cursor]
      database.rawQuery(any) returns cursor
      val rs = sqlDroid.executeQuery(selectSql)
      rs must beLike {
        case st: SQLDroidResultSet => st.cursor shouldEqual cursor
      }
      sqlDroid.getResultSet shouldEqual rs
    }

    "throws a SQLException when the connection is close" in new WithoutConnection {
      sqlDroid.executeQuery(selectSql) must throwA[SQLException](sqlDroid.connectionClosedErrorMessage)
    }
  }

  "executeUpdate and getUpdateCount" should {

    "return in both methods the result of changedRowCount in database" in new WithoutColumnGenerated {
      val changedRowCount = 10
      database.changedRowCount() returns changedRowCount
      sqlDroid.executeUpdate(insertSql) shouldEqual changedRowCount
      sqlDroid.getUpdateCount shouldEqual changedRowCount
    }

    "throws a SQLException when the connection is close" in new WithoutConnection {
      sqlDroid.executeUpdate(selectSql) must throwA[SQLException](sqlDroid.connectionClosedErrorMessage)
    }

    "call LogWrapper.notImplemented when passing the autGeneratedKeys argument" in new WithMockLogger {
      sqlDroid.executeUpdate(selectSql, 0)
      there was one(mockLog).notImplemented(0)
    }

    "call LogWrapper.notImplemented when passing the columnIndexes argument" in new WithMockLogger {
      sqlDroid.executeUpdate(selectSql, scala.Array(0))
      there was one(mockLog).notImplemented(0)
    }

    "call LogWrapper.notImplemented when passing the columnNames argument" in new WithMockLogger {
      sqlDroid.executeUpdate(selectSql, scala.Array(""))
      there was one(mockLog).notImplemented(0)
    }
  }

  "getConnection" should {

    "return the connection when is open" in new WithoutColumnGenerated {
      sqlDroid.getConnection shouldEqual connection
    }

    "throws a SQLException when the connection is close" in new WithoutConnection {
      sqlDroid.getConnection must throwA[SQLException](sqlDroid.connectionClosedErrorMessage)
    }
  }

  "getGeneratedKeys and getResultSet" should {

    "return the same SQLDroidResultSet with the Cursor returned by the database that receive the column generated name" in
      new WithColumnGenerated {
        val cursor = mock[Cursor]
        database.rawQuery(contain(columnGenerated)) returns cursor
        val rs = sqlDroid.getGeneratedKeys
        rs must beLike {
          case st: SQLDroidResultSet => st.cursor shouldEqual cursor
        }
        sqlDroid.getResultSet shouldEqual rs
      }

    "throws a SQLException when the connection is close" in new WithoutConnection {
      sqlDroid.getGeneratedKeys must throwA[SQLException](sqlDroid.connectionClosedErrorMessage)
    }
  }

  "getMaxRows and setMaxRows" should {

    "works as expected when the number is positive and the connection is open" in new WithoutColumnGenerated {
      1 to 10 foreach { max =>
        sqlDroid.setMaxRows(max)
        sqlDroid.getMaxRows shouldEqual max
      }
    }

    "works as expected when the number is zero and the connection is open" in new WithoutColumnGenerated {
      val max = 0
      sqlDroid.setMaxRows(max)
      sqlDroid.getMaxRows shouldEqual max
    }

    "throws a SQLException when the number is negative" in new WithoutColumnGenerated {
      sqlDroid.setMaxRows(-1) must throwA[SQLException](sqlDroid.maxNegativeErrorMessage)
    }

    "throws a SQLException when the connection is closed" in new WithoutConnection {
      sqlDroid.setMaxRows(-1) must throwA[SQLException](sqlDroid.statementClosedErrorMessage)
    }

  }

  "getMoreResults" should {

    "return false when the call is made without arguments" in new WithoutColumnGenerated {
      sqlDroid.getMoreResults must beFalse
    }

    "return false when pass a Int argument" in new WithoutColumnGenerated {
      sqlDroid.getMoreResults(10) must beFalse
    }
  }

  "cancel" should {

    "call LogWrapper.notImplemented" in new WithMockLogger {
      sqlDroid.cancel()
      there was one(mockLog).notImplemented(Unit)
    }
  }

  "clearWarnings" should {

    "call LogWrapper.notImplemented" in new WithMockLogger {
      sqlDroid.clearWarnings()
      there was one(mockLog).notImplemented(Unit)
    }
  }

  "getFetchDirection" should {

    "call LogWrapper.notImplemented" in new WithMockLogger {
      sqlDroid.getFetchDirection()
      there was one(mockLog).notImplemented(0)
    }
  }

  "setFetchDirection" should {

    "call LogWrapper.notImplemented" in new WithMockLogger {
      sqlDroid.setFetchDirection(10)
      there was one(mockLog).notImplemented(Unit)
    }
  }

  "getFetchSize" should {

    "call LogWrapper.notImplemented" in new WithMockLogger {
      sqlDroid.getFetchSize()
      there was one(mockLog).notImplemented(0)
    }
  }

  "setFetchSize" should {

    "call LogWrapper.notImplemented" in new WithMockLogger {
      sqlDroid.setFetchSize(10)
      there was one(mockLog).notImplemented(Unit)
    }
  }

  "getMaxFieldSize" should {

    "call LogWrapper.notImplemented" in new WithMockLogger {
      sqlDroid.getMaxFieldSize()
      there was one(mockLog).notImplemented(0)
    }
  }

  "setMaxFieldSize" should {

    "call LogWrapper.notImplemented" in new WithMockLogger {
      sqlDroid.setMaxFieldSize(10)
      there was one(mockLog).notImplemented(Unit)
    }
  }

  "getQueryTimeout" should {

    "call LogWrapper.notImplemented" in new WithMockLogger {
      sqlDroid.getQueryTimeout()
      there was one(mockLog).notImplemented(0)
    }
  }

  "setQueryTimeout" should {

    "call LogWrapper.notImplemented" in new WithMockLogger {
      sqlDroid.setQueryTimeout(10)
      there was one(mockLog).notImplemented(Unit)
    }
  }

  "getResultSetConcurrency" should {

    "call LogWrapper.notImplemented" in new WithMockLogger {
      sqlDroid.getResultSetConcurrency()
      there was one(mockLog).notImplemented(0)
    }
  }

  "getResultSetHoldability" should {

    "call LogWrapper.notImplemented" in new WithMockLogger {
      sqlDroid.getResultSetHoldability()
      there was one(mockLog).notImplemented(0)
    }
  }

  "getResultSetType" should {

    "call LogWrapper.notImplemented" in new WithMockLogger {
      sqlDroid.getResultSetType()
      there was one(mockLog).notImplemented(0)
    }
  }

  "getWarnings" should {

    "call LogWrapper.notImplemented" in new WithMockLogger {
      sqlDroid.getWarnings()
      there was one(mockLog).notImplemented(javaNull)
    }
  }

  "setCursorName" should {

    "call LogWrapper.notImplemented" in new WithMockLogger {
      sqlDroid.setCursorName("cursorName")
      there was one(mockLog).notImplemented(Unit)
    }
  }

  "setEscapeProcessing" should {

    "call LogWrapper.notImplemented" in new WithMockLogger {
      sqlDroid.setEscapeProcessing(false)
      there was one(mockLog).notImplemented(Unit)
    }
  }

  "setPoolable" should {

    "call LogWrapper.notImplemented" in new WithMockLogger {
      sqlDroid.setPoolable(false)
      there was one(mockLog).notImplemented(Unit)
    }
  }

  "isPoolable" should {

    "call LogWrapper.notImplemented" in new WithMockLogger {
      sqlDroid.isPoolable()
      there was one(mockLog).notImplemented(false)
    }
  }

  "isCloseOnCompletion" should {

    "call LogWrapper.notImplemented" in new WithMockLogger {
      sqlDroid.isCloseOnCompletion()
      there was one(mockLog).notImplemented(false)
    }
  }

  "closeOnCompletion" should {

    "call LogWrapper.notImplemented" in new WithMockLogger {
      sqlDroid.closeOnCompletion()
      there was one(mockLog).notImplemented(Unit)
    }
  }

}
