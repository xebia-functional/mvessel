package com.fortysevendeg.android.sqlite

import java.sql._
import java.util.Properties
import java.util.concurrent.Executor

import android.database.sqlite.SQLiteDatabase
import com.fortysevendeg.android.sqlite.logging.LogWrapper
import com.fortysevendeg.android.sqlite.statement.SQLDroidPreparedStatement
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.specification.Scope

import scala.util.Random

trait SQLDroidConnectionSpecification
  extends Specification
  with Mockito {

  val databaseName = "database"

  val resultSetType = Random.nextInt(10)

  val resultSetConcurrency = Random.nextInt(10)

  val resultSetHoldability = Random.nextInt(10)

  trait SQLDroidConnectionScope
    extends Scope {

    val logWrapper = mock[TestLogWrapper]

    val database = mock[SQLDroidDatabase]

    val sqliteDatabase = mock[SQLiteDatabase]

    database.database returns sqliteDatabase

    val sqlDroid: SQLDroidConnection

  }

  trait WithSomeConnection extends SQLDroidConnectionScope {

    val sqlDroid: SQLDroidConnection = new SQLDroidConnection(databaseName = databaseName, logWrapper = new TestLogWrapper) {

      override protected def createDatabase(): Option[SQLDroidDatabase] = Some(database)

    }

  }

  trait WithSomeConnectionAutoCommit extends SQLDroidConnectionScope {

    val sqlDroid: SQLDroidConnection = new SQLDroidConnection(databaseName = databaseName, logWrapper = new TestLogWrapper) {

      override protected def createDatabase(): Option[SQLDroidDatabase] = Some(database)

      override protected def defaultAutoCommit(): Boolean = true
    }

  }

  trait WithNoneConnection extends SQLDroidConnectionScope {

    val sqlDroid: SQLDroidConnection = new SQLDroidConnection(databaseName = databaseName, logWrapper = new TestLogWrapper) {

      override protected def createDatabase(): Option[SQLDroidDatabase] = None

    }

  }

  trait WithMockLogger extends SQLDroidConnectionScope {

    val mockLog = mock[LogWrapper]

    val sqlDroid: SQLDroidConnection = new SQLDroidConnection(databaseName = databaseName, logWrapper = mockLog)

  }

}

class SQLDroidConnectionSpec
  extends SQLDroidConnectionSpecification {

  "close" should {

    "call close method on database when its open" in new WithSomeConnection {
      sqlDroid.close()
      there was one(database).close()
    }

    "call nothing when database its closed" in new WithNoneConnection {
      sqlDroid.close()
      there was no(database).close()
    }

  }

  "commit" should {

    "call endTransaction and beginTransaction when the state it's ok" in new WithSomeConnection {
      sqlDroid.commit()
      there was one(database).endTransaction()
      there was one(database).beginTransaction()
    }

    "throw a SQLException if the autoCommit is active" in new WithSomeConnectionAutoCommit {
      sqlDroid.commit() must throwA[SQLException]
    }

    "throw a SQLException when the database is closed" in new WithNoneConnection {
      sqlDroid.commit() must throwA[SQLException]
    }

  }

  "createStatement" should {

    "return a statement" in new WithSomeConnection {
      sqlDroid.createStatement() must beAnInstanceOf[Statement]
    }

  }

  "getAutoCommit" should {

    "return false when the autoCommit is disabled" in new WithSomeConnection {
      sqlDroid.getAutoCommit() must beFalse
    }

    "return true when the autoCommit is enabled" in new WithSomeConnectionAutoCommit {
      sqlDroid.getAutoCommit() must beTrue
    }

  }

  "setAutoCommit" should {

    "doing nothing when the autoCommit status is the same" in new WithSomeConnection {
      sqlDroid.setAutoCommit(false)
      there was no(database).beginTransaction()
      there was no(database).endTransaction()
    }

    "call to endTransaction and activate autoCommit when pass true and autoCommit is disabled" in
      new WithSomeConnection {
        sqlDroid.setAutoCommit(true)
        there was one(database).endTransaction()
        sqlDroid.getAutoCommit must beTrue
      }

    "call to beginTransaction and disable autoCommit when pass false and autoCommit is enabled" in
      new WithSomeConnectionAutoCommit {
        sqlDroid.setAutoCommit(false)
        there was one(database).beginTransaction()
        sqlDroid.getAutoCommit must beFalse
      }

  }

  "getMetaData" should {

    "return a DatabaseMetaData" in new WithSomeConnection {
      sqlDroid.getMetaData() must beAnInstanceOf[DatabaseMetaData]
    }

  }

  "isClosed" should {

    "return false when the database is open" in new WithSomeConnection {
      sqliteDatabase.isOpen returns true
      sqlDroid.isClosed must beFalse
    }

    "return true when the database is closed" in new WithSomeConnection {
      sqliteDatabase.isOpen returns false
      sqlDroid.isClosed must beTrue
    }

    "return false when the connection is close" in new WithNoneConnection {
      sqlDroid.isClosed must beTrue
    }

  }

  "nativeSQL" should {

    "return the same string" in new WithNoneConnection {
      val value = Random.nextString(10)
      sqlDroid.nativeSQL(value) shouldEqual value
    }

  }

  "prepareStatement" should {

    "creates a SQLDroidPreparedStatement with the passed sql" in new WithNoneConnection {
      val value = Random.nextString(10)
      sqlDroid.prepareStatement(value) must beLike[PreparedStatement] {
        case st: SQLDroidPreparedStatement =>
          st.sql shouldEqual value
          st.columnGenerated must beNone
      }
    }

    "creates a SQLDroidPreparedStatement with the passed sql when passing resultSet type and concurrency" in
      new WithNoneConnection {
        val value = Random.nextString(10)
        sqlDroid.prepareStatement(
          sql = value,
          resultSetType = resultSetType,
          resultSetConcurrency = resultSetConcurrency) must beLikeA[PreparedStatement] {
          case st: SQLDroidPreparedStatement =>
            st.sql shouldEqual value
            st.columnGenerated must beNone
        }
      }

    "creates a SQLDroidPreparedStatement with the passed sql when passing resultSet type, concurrency and holdability" in
      new WithNoneConnection {
        val value = Random.nextString(10)
        sqlDroid.prepareStatement(
          sql = value,
          resultSetType = resultSetType,
          resultSetConcurrency = resultSetConcurrency,
          resultSetHoldability = resultSetHoldability) must beLikeA[PreparedStatement] {
          case st: SQLDroidPreparedStatement =>
            st.sql shouldEqual value
            st.columnGenerated must beNone
        }
      }

    "creates a SQLDroidPreparedStatement with the passed sql when passing autoGeneratedKeys value" in
      new WithNoneConnection {
        val value = Random.nextString(10)
        sqlDroid.prepareStatement(value, 1) must beLikeA[PreparedStatement] {
          case st: SQLDroidPreparedStatement =>
            st.sql shouldEqual value
            st.columnGenerated must beNone
        }
      }

    "creates a SQLDroidPreparedStatement with the passed sql when passing a column int array" in
      new WithNoneConnection {
        val value = Random.nextString(10)
        sqlDroid.prepareStatement(value, scala.Array[Int]()) must
          beLikeA[PreparedStatement] {
            case st: SQLDroidPreparedStatement =>
              st.sql shouldEqual value
              st.columnGenerated must beNone
        }
      }

    "creates a SQLDroidPreparedStatement with the passed sql when the column array is empty" in
      new WithNoneConnection {
        val value = Random.nextString(10)
        sqlDroid.prepareStatement(value, scala.Array[String]()) must
          beLikeA[PreparedStatement] {
            case st: SQLDroidPreparedStatement =>
              st.sql shouldEqual value
              st.columnGenerated must beNone
        }
      }

    "creates a SQLDroidPreparedStatement with the passed sql and column name when the column array has only one element" in
      new WithNoneConnection {
        val value = Random.nextString(10)
        val column = Random.nextString(10)
        sqlDroid.prepareStatement(value, scala.Array[String](column)) must
          beLikeA[PreparedStatement] {
            case st: SQLDroidPreparedStatement =>
              st.sql shouldEqual value
              st.columnGenerated must beSome[String].which(_ == column)
        }
      }

    "creates a SQLDroidPreparedStatement with the passed sql when the column array is null" in
      new WithNoneConnection {
        val value = Random.nextString(10)
        sqlDroid.prepareStatement(value, javaNull.asInstanceOf[scala.Array[String]]) must
          beLikeA[PreparedStatement] {
            case st: SQLDroidPreparedStatement =>
              st.sql shouldEqual value
              st.columnGenerated must beNone
        }
      }

    "return null when the column array has more than one element" in
      new WithNoneConnection {
        val value = Random.nextString(10)
        Option(sqlDroid.prepareStatement(value, scala.Array[String]("1", "2"))) must beNone
      }

  }

  "rollback" should {

    "throw a SQLException when the autCommit is active" in new WithSomeConnectionAutoCommit {
      sqlDroid.rollback() must throwA[SQLException](sqlDroid.autoCommitErrorMessage)
    }

    "execute rollback sql and starts a new transaction" in new WithSomeConnection {
      sqlDroid.rollback()
      there was one(database).execSQL(sqlDroid.rollbackSql)
      there was one(database).endTransaction()
      there was one(database).beginTransaction()
    }

    "call to LogWrapper.notImplemented when passing a SavePoint object" in new WithMockLogger {
      sqlDroid.rollback(mock[Savepoint])
      there was one(mockLog).notImplemented(Unit)
    }

  }

  "clearWarnings" should {

    "call to LogWrapper.notImplemented" in new WithMockLogger {
      sqlDroid.clearWarnings()
      there was one(mockLog).notImplemented(Unit)
    }

  }

  "createStatement" should {

    "call to LogWrapper.notImplemented when call to two arguments method" in new WithMockLogger {
      sqlDroid.createStatement(resultSetType, resultSetConcurrency)
      there was one(mockLog).notImplemented(javaNull)
    }

    "call to LogWrapper.notImplemented when call to three arguments method" in new WithMockLogger {
      sqlDroid.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability)
      there was one(mockLog).notImplemented(javaNull)
    }

  }

  "getCatalog" should {

    "call to LogWrapper.notImplemented" in new WithMockLogger {
      sqlDroid.getCatalog()
      there was one(mockLog).notImplemented(javaNull)
    }

  }

  "getHoldability" should {

    "call to LogWrapper.notImplemented" in new WithMockLogger {
      sqlDroid.getHoldability()
      there was one(mockLog).notImplemented(0)
    }

  }

  "getTransactionIsolation" should {

    "call to LogWrapper.notImplemented" in new WithMockLogger {
      sqlDroid.getTransactionIsolation()
      there was one(mockLog).notImplemented(0)
    }

  }

  "getTypeMap" should {

    "call to LogWrapper.notImplemented" in new WithMockLogger {
      sqlDroid.getTypeMap()
      there was one(mockLog).notImplemented(javaNull)
    }

  }

  "getWarnings" should {

    "call to LogWrapper.notImplemented" in new WithMockLogger {
      sqlDroid.getWarnings()
      there was one(mockLog).notImplemented(javaNull)
    }

  }

  "isReadOnly" should {

    "call to LogWrapper.notImplemented" in new WithMockLogger {
      sqlDroid.isReadOnly()
      there was one(mockLog).notImplemented(false)
    }

  }

  "prepareCall" should {

    "call to LogWrapper.notImplemented when call to prepareCall with a SQL" in
      new WithMockLogger {
      sqlDroid.prepareCall(Random.nextString(10))
      there was one(mockLog).notImplemented(javaNull)
    }

    "call to LogWrapper.notImplemented when call to prepareCall with a SQL and resultSet type and concurrency" in
      new WithMockLogger {
      sqlDroid.prepareCall(Random.nextString(10), resultSetType, resultSetConcurrency)
      there was one(mockLog).notImplemented(javaNull)
    }

    "call to LogWrapper.notImplemented when call to prepareCall with a SQL and resultSet type, concurrency and holdability" in
      new WithMockLogger {
      sqlDroid.prepareCall(Random.nextString(10), resultSetType, resultSetConcurrency, resultSetHoldability)
      there was one(mockLog).notImplemented(javaNull)
    }

  }

  "releaseSavepoint" should {

    "call to LogWrapper.notImplemented" in new WithMockLogger {
      sqlDroid.releaseSavepoint(mock[Savepoint])
      there was one(mockLog).notImplemented(Unit)
    }

  }

  "setCatalog" should {

    "call to LogWrapper.notImplemented" in new WithMockLogger {
      sqlDroid.setCatalog(Random.nextString(10))
      there was one(mockLog).notImplemented(Unit)
    }

  }

  "setHoldability" should {

    "call to LogWrapper.notImplemented" in new WithMockLogger {
      sqlDroid.setHoldability(0)
      there was one(mockLog).notImplemented(Unit)
    }

  }

  "setReadOnly" should {

    "call to LogWrapper.notImplemented" in new WithMockLogger {
      sqlDroid.setReadOnly(false)
      there was one(mockLog).notImplemented(Unit)
    }

  }

  "setSavepoint" should {

    "call to LogWrapper.notImplemented" in new WithMockLogger {
      sqlDroid.setSavepoint()
      there was one(mockLog).notImplemented(javaNull)
    }

    "call to LogWrapper.notImplemented when passing a SavePoint name" in new WithMockLogger {
      sqlDroid.setSavepoint(Random.nextString(10))
      there was one(mockLog).notImplemented(javaNull)
    }

  }

  "setTransactionIsolation" should {

    "call to LogWrapper.notImplemented" in new WithMockLogger {
      sqlDroid.setTransactionIsolation(0)
      there was one(mockLog).notImplemented(Unit)
    }

  }

  "setTypeMap" should {

    "call to LogWrapper.notImplemented" in new WithMockLogger {
      sqlDroid.setTypeMap(javaNull)
      there was one(mockLog).notImplemented(Unit)
    }

  }

  "getNetworkTimeout" should {

    "call to LogWrapper.notImplemented" in new WithMockLogger {
      sqlDroid.getNetworkTimeout()
      there was one(mockLog).notImplemented(0)
    }

  }

  "createBlob" should {

    "call to LogWrapper.notImplemented" in new WithMockLogger {
      sqlDroid.createBlob()
      there was one(mockLog).notImplemented(javaNull)
    }

  }

  "createSQLXML" should {

    "call to LogWrapper.notImplemented" in new WithMockLogger {
      sqlDroid.createSQLXML()
      there was one(mockLog).notImplemented(javaNull)
    }

  }

  "createNClob" should {

    "call to LogWrapper.notImplemented" in new WithMockLogger {
      sqlDroid.createNClob()
      there was one(mockLog).notImplemented(javaNull)
    }

  }

  "getClientInfo" should {

    "call to LogWrapper.notImplemented" in new WithMockLogger {
      sqlDroid.getClientInfo()
      there was one(mockLog).notImplemented(javaNull)
    }

    "call to LogWrapper.notImplemented when passing a client info name" in new WithMockLogger {
      sqlDroid.getClientInfo(Random.nextString(10))
      there was one(mockLog).notImplemented(javaNull)
    }

  }

  "getSchema" should {

    "call to LogWrapper.notImplemented" in new WithMockLogger {
      sqlDroid.getSchema()
      there was one(mockLog).notImplemented(javaNull)
    }

  }

  "setNetworkTimeout" should {

    "call to LogWrapper.notImplemented" in new WithMockLogger {
      sqlDroid.setNetworkTimeout(mock[Executor], 0)
      there was one(mockLog).notImplemented(Unit)
    }

  }

  "setClientInfo" should {

    "call to LogWrapper.notImplemented when passing a name and a value" in new WithMockLogger {
      sqlDroid.setClientInfo(Random.nextString(10), Random.nextString(10))
      there was one(mockLog).notImplemented(Unit)
    }

    "call to LogWrapper.notImplemented when passing a Properties object" in new WithMockLogger {
      sqlDroid.setClientInfo(mock[Properties])
      there was one(mockLog).notImplemented(Unit)
    }

  }

  "createClob" should {

    "call to LogWrapper.notImplemented" in new WithMockLogger {
      sqlDroid.createClob()
      there was one(mockLog).notImplemented(javaNull)
    }

  }

  "createArrayOf" should {

    "call to LogWrapper.notImplemented" in new WithMockLogger {
      sqlDroid.createArrayOf(Random.nextString(10), scala.Array.empty)
      there was one(mockLog).notImplemented(javaNull)
    }

  }

  "abort" should {

    "call to LogWrapper.notImplemented" in new WithMockLogger {
      sqlDroid.abort(mock[Executor])
      there was one(mockLog).notImplemented(Unit)
    }

  }

  "isValid" should {

    "call to LogWrapper.notImplemented" in new WithMockLogger {
      sqlDroid.isValid(0)
      there was one(mockLog).notImplemented(false)
    }

  }

  "createStruct" should {

    "call to LogWrapper.notImplemented" in new WithMockLogger {
      sqlDroid.createStruct(Random.nextString(10), scala.Array.empty)
      there was one(mockLog).notImplemented(javaNull)
    }

  }

  "setSchema" should {

    "call to LogWrapper.notImplemented" in new WithMockLogger {
      sqlDroid.setSchema(Random.nextString(10))
      there was one(mockLog).notImplemented(Unit)
    }

  }

}
