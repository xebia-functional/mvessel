package com.fortysevendeg.mvessel

import java.sql._
import java.util.Properties
import java.util.concurrent.Executor

import com.fortysevendeg.mvessel.api.{DatabaseProxyFactory, DatabaseProxy}
import com.fortysevendeg.mvessel.logging.LogWrapper
import com.fortysevendeg.mvessel.statement.PreparedStatement
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.specification.Scope

import scala.util.Random

trait ConnectionSpecification
  extends Specification
  with Mockito {

  val databaseName = "database"

  val resultSetType = Random.nextInt(10)

  val resultSetConcurrency = Random.nextInt(10)

  val resultSetHoldability = Random.nextInt(10)

  trait ConnectionScope
    extends Scope {

    val logWrapper = mock[TestLogWrapper]

    val databaseProxy = mock[DatabaseProxy]

    val databaseProxyFactory = mock[DatabaseProxyFactory]

    databaseProxyFactory.openDatabase(any, any) returns databaseProxy

    val connection: Connection

  }

  trait WithSomeConnection extends ConnectionScope {

    val connection: Connection = new Connection(
      databaseWrapperFactory = databaseProxyFactory,
      databaseName = databaseName,
      logWrapper = new TestLogWrapper)

  }

  trait WithSomeConnectionAutoCommit extends ConnectionScope {

    val connection: Connection = new Connection(
      databaseWrapperFactory = databaseProxyFactory,
      databaseName = databaseName,
      logWrapper = new TestLogWrapper) {
      override protected def defaultAutoCommit(): Boolean = true
    }

  }

  trait WithMockLogger extends ConnectionScope {

    val mockLog = mock[LogWrapper]

    val connection: Connection = new Connection(
      databaseWrapperFactory = databaseProxyFactory,
      databaseName = databaseName,
      logWrapper = mockLog)

  }

}

class ConnectionSpec
  extends ConnectionSpecification {

  "close" should {

    "call close method on database when its open" in new WithSomeConnection {
      connection.close()
      there was one(databaseProxy).close()
    }

  }

  "commit" should {

    "call endTransaction and beginTransaction when the state it's ok" in new WithSomeConnection {
      connection.commit()
      there was one(databaseProxy).endTransaction()
      there was one(databaseProxy).beginTransaction()
    }

    "throw a SQLException if the autoCommit is active" in new WithSomeConnectionAutoCommit {
      connection.commit() must throwA[SQLException]
    }

    "throw a SQLException when the database is closed" in new WithSomeConnection {
      connection.close()
      connection.commit() must throwA[SQLException]
    }

  }

  "createStatement" should {

    "return a statement" in new WithSomeConnection {
      connection.createStatement() must beAnInstanceOf[Statement]
    }

  }

  "getAutoCommit" should {

    "return false when the autoCommit is disabled" in new WithSomeConnection {
      connection.getAutoCommit() must beFalse
    }

    "return true when the autoCommit is enabled" in new WithSomeConnectionAutoCommit {
      connection.getAutoCommit() must beTrue
    }

  }

  "setAutoCommit" should {

    "doing nothing when the autoCommit status is the same" in new WithSomeConnection {
      connection.setAutoCommit(false)
      there was no(databaseProxy).beginTransaction()
      there was no(databaseProxy).endTransaction()
    }

    "call to endTransaction and activate autoCommit when pass true and autoCommit is disabled" in
      new WithSomeConnection {
        connection.setAutoCommit(true)
        there was one(databaseProxy).endTransaction()
        connection.getAutoCommit must beTrue
      }

    "call to beginTransaction and disable autoCommit when pass false and autoCommit is enabled" in
      new WithSomeConnectionAutoCommit {
        connection.setAutoCommit(false)
        there was one(databaseProxy).beginTransaction()
        connection.getAutoCommit must beFalse
      }

  }

  "getMetaData" should {

    "return a DatabaseMetaData" in new WithSomeConnection {
      connection.getMetaData() must beAnInstanceOf[DatabaseMetaData]
    }

  }

  "isClosed" should {

    "return false when the database is open" in new WithSomeConnection {
      databaseProxy.isOpen returns true
      connection.isClosed must beFalse
    }

    "return true when the database is closed" in new WithSomeConnection {
      databaseProxy.isOpen returns false
      connection.isClosed must beTrue
    }

    "return false when the connection is close" in new WithSomeConnection {
      connection.close()
      connection.isClosed must beTrue
    }

  }

  "nativeSQL" should {

    "return the same string" in new WithSomeConnection {
      val value = Random.nextString(10)
      connection.nativeSQL(value) shouldEqual value
    }

  }

  "prepareStatement" should {

    "creates a PreparedStatement with the passed sql" in new WithSomeConnection {
      val value = Random.nextString(10)
      connection.prepareStatement(value) must beLike[PreparedStatement] {
        case st: PreparedStatement =>
          st.sql shouldEqual value
          st.columnGenerated must beNone
      }
    }

    "creates a PreparedStatement with the passed sql when passing resultSet type and concurrency" in
      new WithSomeConnection {
        val value = Random.nextString(10)
        connection.prepareStatement(
          sql = value,
          resultSetType = resultSetType,
          resultSetConcurrency = resultSetConcurrency) must beLikeA[PreparedStatement] {
          case st: PreparedStatement =>
            st.sql shouldEqual value
            st.columnGenerated must beNone
        }
      }

    "creates a PreparedStatement with the passed sql when passing resultSet type, concurrency and holdability" in
      new WithSomeConnection {
        val value = Random.nextString(10)
        connection.prepareStatement(
          sql = value,
          resultSetType = resultSetType,
          resultSetConcurrency = resultSetConcurrency,
          resultSetHoldability = resultSetHoldability) must beLikeA[PreparedStatement] {
          case st: PreparedStatement =>
            st.sql shouldEqual value
            st.columnGenerated must beNone
        }
      }

    "creates a PreparedStatement with the passed sql when passing autoGeneratedKeys value" in
      new WithSomeConnection {
        val value = Random.nextString(10)
        connection.prepareStatement(value, 1) must beLikeA[PreparedStatement] {
          case st: PreparedStatement =>
            st.sql shouldEqual value
            st.columnGenerated must beNone
        }
      }

    "creates a PreparedStatement with the passed sql when passing a column int array" in
      new WithSomeConnection {
        val value = Random.nextString(10)
        connection.prepareStatement(value, scala.Array[Int]()) must
          beLikeA[PreparedStatement] {
            case st: PreparedStatement =>
              st.sql shouldEqual value
              st.columnGenerated must beNone
        }
      }

    "creates a PreparedStatement with the passed sql when the column array is empty" in
      new WithSomeConnection {
        val value = Random.nextString(10)
        connection.prepareStatement(value, scala.Array[String]()) must
          beLikeA[PreparedStatement] {
            case st: PreparedStatement =>
              st.sql shouldEqual value
              st.columnGenerated must beNone
        }
      }

    "creates a PreparedStatement with the passed sql and column name when the column array has only one element" in
      new WithSomeConnection {
        val value = Random.nextString(10)
        val column = Random.nextString(10)
        connection.prepareStatement(value, scala.Array[String](column)) must
          beLikeA[PreparedStatement] {
            case st: PreparedStatement =>
              st.sql shouldEqual value
              st.columnGenerated must beSome[String].which(_ == column)
        }
      }

    "creates a PreparedStatement with the passed sql when the column array is null" in
      new WithSomeConnection {
        val value = Random.nextString(10)
        connection.prepareStatement(value, javaNull.asInstanceOf[scala.Array[String]]) must
          beLikeA[PreparedStatement] {
            case st: PreparedStatement =>
              st.sql shouldEqual value
              st.columnGenerated must beNone
        }
      }

    "return null when the column array has more than one element" in
      new WithSomeConnection {
        val value = Random.nextString(10)
        Option(connection.prepareStatement(value, scala.Array[String]("1", "2"))) must beNone
      }

  }

  "rollback" should {

    "throw a SQLException when the autCommit is active" in new WithSomeConnectionAutoCommit {
      connection.rollback() must throwA[SQLException](connection.autoCommitErrorMessage)
    }

    "execute rollback sql and starts a new transaction" in new WithSomeConnection {
      connection.rollback()
      there was one(databaseProxy).execSQL(connection.rollbackSql)
      there was one(databaseProxy).endTransaction()
      there was one(databaseProxy).beginTransaction()
    }

    "call to LogWrapper.notImplemented when passing a SavePoint object" in new WithMockLogger {
      connection.rollback(mock[Savepoint])
      there was one(mockLog).notImplemented(Unit)
    }

  }

  "clearWarnings" should {

    "call to LogWrapper.notImplemented" in new WithMockLogger {
      connection.clearWarnings()
      there was one(mockLog).notImplemented(Unit)
    }

  }

  "createStatement" should {

    "call to LogWrapper.notImplemented when call to two arguments method" in new WithMockLogger {
      connection.createStatement(resultSetType, resultSetConcurrency)
      there was one(mockLog).notImplemented(javaNull)
    }

    "call to LogWrapper.notImplemented when call to three arguments method" in new WithMockLogger {
      connection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability)
      there was one(mockLog).notImplemented(javaNull)
    }

  }

  "getCatalog" should {

    "call to LogWrapper.notImplemented" in new WithMockLogger {
      connection.getCatalog()
      there was one(mockLog).notImplemented(javaNull)
    }

  }

  "getHoldability" should {

    "call to LogWrapper.notImplemented" in new WithMockLogger {
      connection.getHoldability()
      there was one(mockLog).notImplemented(0)
    }

  }

  "getTransactionIsolation" should {

    "call to LogWrapper.notImplemented" in new WithMockLogger {
      connection.getTransactionIsolation()
      there was one(mockLog).notImplemented(0)
    }

  }

  "getTypeMap" should {

    "call to LogWrapper.notImplemented" in new WithMockLogger {
      connection.getTypeMap()
      there was one(mockLog).notImplemented(javaNull)
    }

  }

  "getWarnings" should {

    "call to LogWrapper.notImplemented" in new WithMockLogger {
      connection.getWarnings()
      there was one(mockLog).notImplemented(javaNull)
    }

  }

  "isReadOnly" should {

    "call to LogWrapper.notImplemented" in new WithMockLogger {
      connection.isReadOnly()
      there was one(mockLog).notImplemented(false)
    }

  }

  "prepareCall" should {

    "call to LogWrapper.notImplemented when call to prepareCall with a SQL" in
      new WithMockLogger {
      connection.prepareCall(Random.nextString(10))
      there was one(mockLog).notImplemented(javaNull)
    }

    "call to LogWrapper.notImplemented when call to prepareCall with a SQL and resultSet type and concurrency" in
      new WithMockLogger {
      connection.prepareCall(Random.nextString(10), resultSetType, resultSetConcurrency)
      there was one(mockLog).notImplemented(javaNull)
    }

    "call to LogWrapper.notImplemented when call to prepareCall with a SQL and resultSet type, concurrency and holdability" in
      new WithMockLogger {
      connection.prepareCall(Random.nextString(10), resultSetType, resultSetConcurrency, resultSetHoldability)
      there was one(mockLog).notImplemented(javaNull)
    }

  }

  "releaseSavepoint" should {

    "call to LogWrapper.notImplemented" in new WithMockLogger {
      connection.releaseSavepoint(mock[Savepoint])
      there was one(mockLog).notImplemented(Unit)
    }

  }

  "setCatalog" should {

    "call to LogWrapper.notImplemented" in new WithMockLogger {
      connection.setCatalog(Random.nextString(10))
      there was one(mockLog).notImplemented(Unit)
    }

  }

  "setHoldability" should {

    "call to LogWrapper.notImplemented" in new WithMockLogger {
      connection.setHoldability(0)
      there was one(mockLog).notImplemented(Unit)
    }

  }

  "setReadOnly" should {

    "call to LogWrapper.notImplemented" in new WithMockLogger {
      connection.setReadOnly(false)
      there was one(mockLog).notImplemented(Unit)
    }

  }

  "setSavepoint" should {

    "call to LogWrapper.notImplemented" in new WithMockLogger {
      connection.setSavepoint()
      there was one(mockLog).notImplemented(javaNull)
    }

    "call to LogWrapper.notImplemented when passing a SavePoint name" in new WithMockLogger {
      connection.setSavepoint(Random.nextString(10))
      there was one(mockLog).notImplemented(javaNull)
    }

  }

  "setTransactionIsolation" should {

    "call to LogWrapper.notImplemented" in new WithMockLogger {
      connection.setTransactionIsolation(0)
      there was one(mockLog).notImplemented(Unit)
    }

  }

  "setTypeMap" should {

    "call to LogWrapper.notImplemented" in new WithMockLogger {
      connection.setTypeMap(javaNull)
      there was one(mockLog).notImplemented(Unit)
    }

  }

  "getNetworkTimeout" should {

    "call to LogWrapper.notImplemented" in new WithMockLogger {
      connection.getNetworkTimeout()
      there was one(mockLog).notImplemented(0)
    }

  }

  "createBlob" should {

    "call to LogWrapper.notImplemented" in new WithMockLogger {
      connection.createBlob()
      there was one(mockLog).notImplemented(javaNull)
    }

  }

  "createSQLXML" should {

    "call to LogWrapper.notImplemented" in new WithMockLogger {
      connection.createSQLXML()
      there was one(mockLog).notImplemented(javaNull)
    }

  }

  "createNClob" should {

    "call to LogWrapper.notImplemented" in new WithMockLogger {
      connection.createNClob()
      there was one(mockLog).notImplemented(javaNull)
    }

  }

  "getClientInfo" should {

    "call to LogWrapper.notImplemented" in new WithMockLogger {
      connection.getClientInfo()
      there was one(mockLog).notImplemented(javaNull)
    }

    "call to LogWrapper.notImplemented when passing a client info name" in new WithMockLogger {
      connection.getClientInfo(Random.nextString(10))
      there was one(mockLog).notImplemented(javaNull)
    }

  }

  "getSchema" should {

    "call to LogWrapper.notImplemented" in new WithMockLogger {
      connection.getSchema()
      there was one(mockLog).notImplemented(javaNull)
    }

  }

  "setNetworkTimeout" should {

    "call to LogWrapper.notImplemented" in new WithMockLogger {
      connection.setNetworkTimeout(mock[Executor], 0)
      there was one(mockLog).notImplemented(Unit)
    }

  }

  "setClientInfo" should {

    "call to LogWrapper.notImplemented when passing a name and a value" in new WithMockLogger {
      connection.setClientInfo(Random.nextString(10), Random.nextString(10))
      there was one(mockLog).notImplemented(Unit)
    }

    "call to LogWrapper.notImplemented when passing a Properties object" in new WithMockLogger {
      connection.setClientInfo(mock[Properties])
      there was one(mockLog).notImplemented(Unit)
    }

  }

  "createClob" should {

    "call to LogWrapper.notImplemented" in new WithMockLogger {
      connection.createClob()
      there was one(mockLog).notImplemented(javaNull)
    }

  }

  "createArrayOf" should {

    "call to LogWrapper.notImplemented" in new WithMockLogger {
      connection.createArrayOf(Random.nextString(10), scala.Array.empty)
      there was one(mockLog).notImplemented(javaNull)
    }

  }

  "abort" should {

    "call to LogWrapper.notImplemented" in new WithMockLogger {
      connection.abort(mock[Executor])
      there was one(mockLog).notImplemented(Unit)
    }

  }

  "isValid" should {

    "call to LogWrapper.notImplemented" in new WithMockLogger {
      connection.isValid(0)
      there was one(mockLog).notImplemented(false)
    }

  }

  "createStruct" should {

    "call to LogWrapper.notImplemented" in new WithMockLogger {
      connection.createStruct(Random.nextString(10), scala.Array.empty)
      there was one(mockLog).notImplemented(javaNull)
    }

  }

  "setSchema" should {

    "call to LogWrapper.notImplemented" in new WithMockLogger {
      connection.setSchema(Random.nextString(10))
      there was one(mockLog).notImplemented(Unit)
    }

  }

}
