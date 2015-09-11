package com.fortysevendeg.mvessel.metadata

import java.sql.{Connection => SQLConnection, DatabaseMetaData => SQLDatabaseMetaData, ResultSet}

import com.fortysevendeg.mvessel.api.{CursorProxy, DatabaseProxyFactory, DatabaseProxy}
import com.fortysevendeg.mvessel.{TestLogWrapper, Connection, Database}
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.specification.Scope

import scala.util.Random

trait DatabaseMetaDataSpecification
  extends Specification
  with Mockito {

  trait WithMockedSQLConnection
    extends Scope {

    val connection = mock[Connection[CursorProxy]]

    val databaseMetaData = new DatabaseMetaData(connection, new TestLogWrapper)

  }

  trait WithMockedConnection
    extends Scope {

    val dbVersion = Random.nextInt(10) + 1

    val driverName = "driver-name"

    val driverVersion = "driver-version"

    val database = mock[Database[CursorProxy]]

    val databaseProxy = mock[DatabaseProxy[CursorProxy]]

    val databaseProxyFactory = mock[DatabaseProxyFactory[CursorProxy]]

    databaseProxyFactory.openDatabase(any, any) returns databaseProxy

    databaseProxy.getVersion returns dbVersion

    databaseProxy.getDriverName returns driverName

    databaseProxy.getDriverVersion returns driverVersion

    database.database returns databaseProxy

    val connection = new Connection[CursorProxy](
      databaseWrapperFactory = databaseProxyFactory,
      databaseName = "",
      logWrapper = new TestLogWrapper)

    val databaseMetaData = new DatabaseMetaData(connection, new TestLogWrapper)

  }

}

class DatabaseMetaDataUnitSpec
  extends DatabaseMetaDataSpecification {

  "getConnection" should {
    "return the connection passed from constructor" in new WithMockedSQLConnection {
      databaseMetaData.getConnection shouldEqual connection
    }
  }

  "getDatabaseMinorVersion" should {
    "return 0" in
      new WithMockedSQLConnection {
        databaseMetaData.getDatabaseMinorVersion shouldEqual 0
      }
  }

  "getDatabaseMajorVersion" should {
    "return the value returned by SQLiteDatabase if the connection is a Connection" in
      new WithMockedConnection {
        databaseMetaData.getDatabaseMajorVersion shouldEqual dbVersion
      }
  }

  "getCatalogSeparator" should {
    "return the dot" in
      new WithMockedSQLConnection {
        databaseMetaData.getCatalogSeparator shouldEqual "."
      }
  }

  "getCatalogTerm" should {
    "return the \"catalog\" string" in
      new WithMockedSQLConnection {
        databaseMetaData.getCatalogTerm shouldEqual "catalog"
      }
  }

  "getDatabaseProductName" should {
    "return a non empty string" in
      new WithMockedSQLConnection {
        databaseMetaData.getDatabaseProductName.isEmpty must beFalse
      }
  }

  "getDatabaseProductVersion" should {
    "return an empty string" in
      new WithMockedSQLConnection {
        databaseMetaData.getDatabaseProductVersion.isEmpty must beTrue
      }
  }

  "getDefaultTransactionIsolation" should {
    "return Connection.TRANSACTION_SERIALIZABLE" in
      new WithMockedSQLConnection {
        databaseMetaData.getDefaultTransactionIsolation shouldEqual SQLConnection.TRANSACTION_SERIALIZABLE
      }
  }

  "isReadOnly" should {

    "return true if the connection is read only" in
      new WithMockedSQLConnection {
        connection.isReadOnly returns true
        databaseMetaData.isReadOnly must beTrue
      }

    "return false if the connection is not read only" in
      new WithMockedSQLConnection {
        connection.isReadOnly returns false
        databaseMetaData.isReadOnly must beFalse
      }
  }

  "getDriverMajorVersion" should {
    "return " in new WithMockedSQLConnection {
      databaseMetaData.getDriverMajorVersion shouldEqual 1
    }
  }

  "getDriverMinorVersion" should {
    "return " in new WithMockedSQLConnection {
      databaseMetaData.getDriverMinorVersion shouldEqual 1
    }
  }

  "getDriverName" should {
    "return " in new WithMockedConnection {
      databaseMetaData.getDriverName shouldEqual driverName
    }
  }

  "getDriverVersion" should {
    "return " in new WithMockedConnection {
      databaseMetaData.getDriverVersion shouldEqual driverVersion
    }
  }

  "getExtraNameCharacters" should {
    "return an empty string" in new WithMockedSQLConnection {
      databaseMetaData.getExtraNameCharacters.isEmpty must beTrue
    }
  }

  "getIdentifierQuoteString" should {
    "return a space character" in new WithMockedSQLConnection {
      databaseMetaData.getIdentifierQuoteString shouldEqual " "
    }
  }

  "getJDBCMajorVersion" should {
    "return 2" in new WithMockedSQLConnection {
      databaseMetaData.getJDBCMajorVersion shouldEqual 2
    }
  }

  "getJDBCMinorVersion" should {
    "return 1" in new WithMockedSQLConnection {
      databaseMetaData.getJDBCMinorVersion shouldEqual 1
    }
  }

  "getMaxBinaryLiteralLength" should {
    "return 0" in new WithMockedSQLConnection {
      databaseMetaData.getMaxBinaryLiteralLength shouldEqual 0
    }
  }

  "getMaxCatalogNameLength" should {
    "return 0" in new WithMockedSQLConnection {
      databaseMetaData.getMaxCatalogNameLength shouldEqual 0
    }
  }

  "getMaxCharLiteralLength" should {
    "return 0" in new WithMockedSQLConnection {
      databaseMetaData.getMaxCharLiteralLength shouldEqual 0
    }
  }

  "getMaxColumnNameLength" should {
    "return 0" in new WithMockedSQLConnection {
      databaseMetaData.getMaxColumnNameLength shouldEqual 0
    }
  }

  "getMaxColumnsInGroupBy" should {
    "return 0" in new WithMockedSQLConnection {
      databaseMetaData.getMaxColumnsInGroupBy shouldEqual 0
    }
  }

  "getMaxColumnsInIndex" should {
    "return 0" in new WithMockedSQLConnection {
      databaseMetaData.getMaxColumnsInIndex shouldEqual 0
    }
  }

  "getMaxColumnsInOrderBy" should {
    "return 0" in new WithMockedSQLConnection {
      databaseMetaData.getMaxColumnsInOrderBy shouldEqual 0
    }
  }

  "getMaxColumnsInSelect" should {
    "return 0" in new WithMockedSQLConnection {
      databaseMetaData.getMaxColumnsInSelect shouldEqual 0
    }
  }

  "getMaxColumnsInTable" should {
    "return 0" in new WithMockedSQLConnection {
      databaseMetaData.getMaxColumnsInTable shouldEqual 0
    }
  }

  "getMaxConnections" should {
    "return 0" in new WithMockedSQLConnection {
      databaseMetaData.getMaxConnections shouldEqual 0
    }
  }

  "getMaxCursorNameLength" should {
    "return 0" in new WithMockedSQLConnection {
      databaseMetaData.getMaxCursorNameLength shouldEqual 0
    }
  }

  "getMaxIndexLength" should {
    "return 0" in new WithMockedSQLConnection {
      databaseMetaData.getMaxIndexLength shouldEqual 0
    }
  }

  "getMaxProcedureNameLength" should {
    "return 0" in new WithMockedSQLConnection {
      databaseMetaData.getMaxProcedureNameLength shouldEqual 0
    }
  }

  "getMaxRowSize" should {
    "return 0" in new WithMockedSQLConnection {
      databaseMetaData.getMaxRowSize shouldEqual 0
    }
  }

  "getMaxSchemaNameLength" should {
    "return 0" in new WithMockedSQLConnection {
      databaseMetaData.getMaxSchemaNameLength shouldEqual 0
    }
  }

  "getMaxStatementLength" should {
    "return 0" in new WithMockedSQLConnection {
      databaseMetaData.getMaxStatementLength shouldEqual 0
    }
  }

  "getMaxStatements" should {
    "return 0" in new WithMockedSQLConnection {
      databaseMetaData.getMaxStatements shouldEqual 0
    }
  }

  "getMaxTableNameLength" should {
    "return 0" in new WithMockedSQLConnection {
      databaseMetaData.getMaxTableNameLength shouldEqual 0
    }
  }

  "getMaxTablesInSelect" should {
    "return 0" in new WithMockedSQLConnection {
      databaseMetaData.getMaxTablesInSelect shouldEqual 0
    }
  }

  "getMaxUserNameLength" should {
    "return 0" in new WithMockedSQLConnection {
      databaseMetaData.getMaxUserNameLength shouldEqual 0
    }
  }

  "getNumericFunctions" should {
    "return 0" in new WithMockedSQLConnection {
      databaseMetaData.getNumericFunctions shouldEqual ""
    }
  }

  "getResultSetHoldability" should {
    "return ResultSet.CLOSE_CURSORS_AT_COMMIT" in new WithMockedSQLConnection {
      databaseMetaData.getResultSetHoldability shouldEqual ResultSet.CLOSE_CURSORS_AT_COMMIT
    }
  }

  "getSchemaTerm" should {
    "return \"schema\"" in new WithMockedSQLConnection {
      databaseMetaData.getSchemaTerm shouldEqual "schema"
    }
  }

  "getSearchStringEscape" should {
    "return empty string" in new WithMockedSQLConnection {
      databaseMetaData.getSearchStringEscape.isEmpty must beTrue
    }
  }

  "getSQLKeywords" should {
    "return empty string" in new WithMockedSQLConnection {
      databaseMetaData.getSQLKeywords.isEmpty must beTrue
    }
  }

  "getSQLStateType" should {
    "return DatabaseMetaData.sqlStateSQL99" in new WithMockedSQLConnection {
      databaseMetaData.getSQLStateType shouldEqual SQLDatabaseMetaData.sqlStateSQL99
    }
  }

  "getStringFunctions" should {
    "return empty string" in new WithMockedSQLConnection {
      databaseMetaData.getStringFunctions.isEmpty must beTrue
    }
  }

  "getSystemFunctions" should {
    "return empty string" in new WithMockedSQLConnection {
      databaseMetaData.getSystemFunctions.isEmpty must beTrue
    }
  }

  "getTimeDateFunctions" should {
    "return empty string" in new WithMockedSQLConnection {
      databaseMetaData.getTimeDateFunctions.isEmpty must beTrue
    }
  }

  "getUserName" should {
    "return empty string" in new WithMockedSQLConnection {
      databaseMetaData.getUserName.isEmpty must beTrue
    }
  }

  "getClientInfoProperties" should {
    "throw a UnsupportedOperationException" in new WithMockedSQLConnection {
      databaseMetaData.getClientInfoProperties must throwA[UnsupportedOperationException]
    }

  }

  "getFunctionColumns" should {
    "throw a UnsupportedOperationException" in new WithMockedSQLConnection {
      databaseMetaData.getFunctionColumns("", "", "", "") must throwA[UnsupportedOperationException]
    }

  }

  "getFunctions" should {
    "throw a UnsupportedOperationException" in new WithMockedSQLConnection {
      databaseMetaData.getFunctions("", "", "") must throwA[UnsupportedOperationException]
    }

  }

  "getProcedureTerm" should {
    "throw a UnsupportedOperationException" in new WithMockedSQLConnection {
      databaseMetaData.getProcedureTerm must throwA[UnsupportedOperationException]
    }

  }

  "getPseudoColumns" should {
    "throw a UnsupportedOperationException" in new WithMockedSQLConnection {
      databaseMetaData.getPseudoColumns("", "", "", "") must throwA[UnsupportedOperationException]
    }

  }

  "getRowIdLifetime" should {
    "throw a UnsupportedOperationException" in new WithMockedSQLConnection {
      databaseMetaData.getRowIdLifetime must throwA[UnsupportedOperationException]
    }

  }

  "getSchemas" should {
    "throw a UnsupportedOperationException" in new WithMockedSQLConnection {
      databaseMetaData.getSchemas("", "") must throwA[UnsupportedOperationException]
    }

  }

  "getSchemas" should {
    "throw a UnsupportedOperationException" in new WithMockedSQLConnection {
      databaseMetaData.getSchemas must throwA[UnsupportedOperationException]
    }

  }

  "getURL" should {
    "throw a UnsupportedOperationException" in new WithMockedSQLConnection {
      databaseMetaData.getURL must throwA[UnsupportedOperationException]
    }

  }

}
