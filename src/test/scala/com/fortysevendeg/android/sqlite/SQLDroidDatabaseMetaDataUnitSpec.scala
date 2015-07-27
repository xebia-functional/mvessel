package com.fortysevendeg.android.sqlite

import java.sql.{DatabaseMetaData, ResultSet, Connection}

import android.database.sqlite.SQLiteDatabase
import com.fortysevendeg.android.sqlite.metadata.SQLDroidDatabaseMetaData
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.specification.Scope

import scala.util.Random

trait SQLDroidDatabaseMetaDataSpecification
  extends Specification
  with Mockito {

  trait WithMockedConnection
    extends Scope {

    val connection = mock[Connection]

    val sqlDroid = new SQLDroidDatabaseMetaData(connection)

  }

  trait WithMockedSQLDroidConnection
    extends Scope {

    val dbVersion = Random.nextInt(10) + 1

    val connection = mock[SQLDroidConnection]

    val sqlDroidDatabase = mock[SQLDroidDatabase]

    val sqlLiteDatabase = mock[SQLiteDatabase]

    sqlLiteDatabase.getVersion returns dbVersion

    sqlDroidDatabase.database returns sqlLiteDatabase

    val f = (db : SQLDroidDatabase) => db.database.getVersion

    connection.withOpenDatabase(f) returns dbVersion

    val sqlDroid = new SQLDroidDatabaseMetaData(connection)

  }

}

class SQLDroidDatabaseMetaDataUnitSpec
  extends SQLDroidDatabaseMetaDataSpecification {

  "getConnection" should {
    "return the connection passed from constructor" in new WithMockedConnection {
      sqlDroid.getConnection shouldEqual connection
    }
  }

  "getDatabaseMinorVersion" should {
    "return 0" in
      new WithMockedConnection {
        sqlDroid.getDatabaseMinorVersion shouldEqual 0
      }
  }

  "getDatabaseMajorVersion" should {
    "return the value returned by SQLiteDatabase if the connection is a SQLDroidConnection" in
      new WithMockedSQLDroidConnection {
        sqlDroid.getDatabaseMajorVersion shouldEqual dbVersion
      }
  }

  "getCatalogSeparator" should {
    "return the dot" in
      new WithMockedConnection {
        sqlDroid.getCatalogSeparator shouldEqual "."
      }
  }

  "getCatalogTerm" should {
    "return the \"catalog\" string" in
      new WithMockedConnection {
        sqlDroid.getCatalogTerm shouldEqual "catalog"
      }
  }

  "getDatabaseProductName" should {
    "return a non empty string" in
      new WithMockedConnection {
        sqlDroid.getDatabaseProductName.isEmpty must beFalse
      }
  }

  "getDatabaseProductVersion" should {
    "return an empty string" in
      new WithMockedConnection {
        sqlDroid.getDatabaseProductVersion.isEmpty must beTrue
      }
  }

  "getDefaultTransactionIsolation" should {
    "return Connection.TRANSACTION_SERIALIZABLE" in
      new WithMockedConnection {
        sqlDroid.getDefaultTransactionIsolation shouldEqual Connection.TRANSACTION_SERIALIZABLE
      }
  }

  "isReadOnly" should {

    "return true if the connection is read only" in
      new WithMockedConnection {
        connection.isReadOnly returns true
        sqlDroid.isReadOnly must beTrue
      }

    "return false if the connection is not read only" in
      new WithMockedConnection {
        connection.isReadOnly returns false
        sqlDroid.isReadOnly must beFalse
      }
  }

  "getDriverMajorVersion" should {
    "return " in new WithMockedConnection {
      sqlDroid.getDriverMajorVersion shouldEqual 1
    }
  }

  "getDriverMinorVersion" should {
    "return " in new WithMockedConnection {
      sqlDroid.getDriverMinorVersion shouldEqual 1
    }
  }

  "getDriverName" should {
    "return " in new WithMockedConnection {
      sqlDroid.getDriverName shouldEqual SQLDroidDriver.driverName
    }
  }

  "getDriverVersion" should {
    "return " in new WithMockedConnection {
      sqlDroid.getDriverVersion shouldEqual SQLDroidDriver.driverVersion
    }
  }

  "getExtraNameCharacters" should {
    "return an empty string" in new WithMockedConnection {
      sqlDroid.getExtraNameCharacters.isEmpty must beTrue
    }
  }

  "getIdentifierQuoteString" should {
    "return a space character" in new WithMockedConnection {
      sqlDroid.getIdentifierQuoteString shouldEqual " "
    }
  }

  "getJDBCMajorVersion" should {
    "return 2" in new WithMockedConnection {
      sqlDroid.getJDBCMajorVersion shouldEqual 2
    }
  }

  "getJDBCMinorVersion" should {
    "return 1" in new WithMockedConnection {
      sqlDroid.getJDBCMinorVersion shouldEqual 1
    }
  }

  "getMaxBinaryLiteralLength" should {
    "return 0" in new WithMockedConnection {
      sqlDroid.getMaxBinaryLiteralLength shouldEqual 0
    }
  }

  "getMaxCatalogNameLength" should {
    "return 0" in new WithMockedConnection {
      sqlDroid.getMaxCatalogNameLength shouldEqual 0
    }
  }

  "getMaxCharLiteralLength" should {
    "return 0" in new WithMockedConnection {
      sqlDroid.getMaxCharLiteralLength shouldEqual 0
    }
  }

  "getMaxColumnNameLength" should {
    "return 0" in new WithMockedConnection {
      sqlDroid.getMaxColumnNameLength shouldEqual 0
    }
  }

  "getMaxColumnsInGroupBy" should {
    "return 0" in new WithMockedConnection {
      sqlDroid.getMaxColumnsInGroupBy shouldEqual 0
    }
  }

  "getMaxColumnsInIndex" should {
    "return 0" in new WithMockedConnection {
      sqlDroid.getMaxColumnsInIndex shouldEqual 0
    }
  }

  "getMaxColumnsInOrderBy" should {
    "return 0" in new WithMockedConnection {
      sqlDroid.getMaxColumnsInOrderBy shouldEqual 0
    }
  }

  "getMaxColumnsInSelect" should {
    "return 0" in new WithMockedConnection {
      sqlDroid.getMaxColumnsInSelect shouldEqual 0
    }
  }

  "getMaxColumnsInTable" should {
    "return 0" in new WithMockedConnection {
      sqlDroid.getMaxColumnsInTable shouldEqual 0
    }
  }

  "getMaxConnections" should {
    "return 0" in new WithMockedConnection {
      sqlDroid.getMaxConnections shouldEqual 0
    }
  }

  "getMaxCursorNameLength" should {
    "return 0" in new WithMockedConnection {
      sqlDroid.getMaxCursorNameLength shouldEqual 0
    }
  }

  "getMaxIndexLength" should {
    "return 0" in new WithMockedConnection {
      sqlDroid.getMaxIndexLength shouldEqual 0
    }
  }

  "getMaxProcedureNameLength" should {
    "return 0" in new WithMockedConnection {
      sqlDroid.getMaxProcedureNameLength shouldEqual 0
    }
  }

  "getMaxRowSize" should {
    "return 0" in new WithMockedConnection {
      sqlDroid.getMaxRowSize shouldEqual 0
    }
  }

  "getMaxSchemaNameLength" should {
    "return 0" in new WithMockedConnection {
      sqlDroid.getMaxSchemaNameLength shouldEqual 0
    }
  }

  "getMaxStatementLength" should {
    "return 0" in new WithMockedConnection {
      sqlDroid.getMaxStatementLength shouldEqual 0
    }
  }

  "getMaxStatements" should {
    "return 0" in new WithMockedConnection {
      sqlDroid.getMaxStatements shouldEqual 0
    }
  }

  "getMaxTableNameLength" should {
    "return 0" in new WithMockedConnection {
      sqlDroid.getMaxTableNameLength shouldEqual 0
    }
  }

  "getMaxTablesInSelect" should {
    "return 0" in new WithMockedConnection {
      sqlDroid.getMaxTablesInSelect shouldEqual 0
    }
  }

  "getMaxUserNameLength" should {
    "return 0" in new WithMockedConnection {
      sqlDroid.getMaxUserNameLength shouldEqual 0
    }
  }

  "getNumericFunctions" should {
    "return 0" in new WithMockedConnection {
      sqlDroid.getNumericFunctions shouldEqual ""
    }
  }

  "getResultSetHoldability" should {
    "return ResultSet.CLOSE_CURSORS_AT_COMMIT" in new WithMockedConnection {
      sqlDroid.getResultSetHoldability shouldEqual ResultSet.CLOSE_CURSORS_AT_COMMIT
    }
  }

  "getSchemaTerm" should {
    "return \"schema\"" in new WithMockedConnection {
      sqlDroid.getSchemaTerm shouldEqual "schema"
    }
  }

  "getSearchStringEscape" should {
    "return empty string" in new WithMockedConnection {
      sqlDroid.getSearchStringEscape.isEmpty must beTrue
    }
  }

  "getSQLKeywords" should {
    "return empty string" in new WithMockedConnection {
      sqlDroid.getSQLKeywords.isEmpty must beTrue
    }
  }

  "getSQLStateType" should {
    "return DatabaseMetaData.sqlStateSQL99" in new WithMockedConnection {
      sqlDroid.getSQLStateType shouldEqual DatabaseMetaData.sqlStateSQL99
    }
  }

  "getStringFunctions" should {
    "return empty string" in new WithMockedConnection {
      sqlDroid.getStringFunctions.isEmpty must beTrue
    }
  }

  "getSystemFunctions" should {
    "return empty string" in new WithMockedConnection {
      sqlDroid.getSystemFunctions.isEmpty must beTrue
    }
  }

  "getTimeDateFunctions" should {
    "return empty string" in new WithMockedConnection {
      sqlDroid.getTimeDateFunctions.isEmpty must beTrue
    }
  }

  "getUserName" should {
    "return empty string" in new WithMockedConnection {
      sqlDroid.getUserName.isEmpty must beTrue
    }
  }

  "getClientInfoProperties" should {
    "throw a UnsupportedOperationException" in new WithMockedConnection {
      sqlDroid.getClientInfoProperties must throwA[UnsupportedOperationException]
    }

  }

  "getFunctionColumns" should {
    "throw a UnsupportedOperationException" in new WithMockedConnection {
      sqlDroid.getFunctionColumns("", "", "", "") must throwA[UnsupportedOperationException]
    }

  }

  "getFunctions" should {
    "throw a UnsupportedOperationException" in new WithMockedConnection {
      sqlDroid.getFunctions("", "", "") must throwA[UnsupportedOperationException]
    }

  }

  "getProcedureTerm" should {
    "throw a UnsupportedOperationException" in new WithMockedConnection {
      sqlDroid.getProcedureTerm must throwA[UnsupportedOperationException]
    }

  }

  "getPseudoColumns" should {
    "throw a UnsupportedOperationException" in new WithMockedConnection {
      sqlDroid.getPseudoColumns("", "", "", "") must throwA[UnsupportedOperationException]
    }

  }

  "getRowIdLifetime" should {
    "throw a UnsupportedOperationException" in new WithMockedConnection {
      sqlDroid.getRowIdLifetime must throwA[UnsupportedOperationException]
    }

  }

  "getSchemas" should {
    "throw a UnsupportedOperationException" in new WithMockedConnection {
      sqlDroid.getSchemas("", "") must throwA[UnsupportedOperationException]
    }

  }

  "getSchemas" should {
    "throw a UnsupportedOperationException" in new WithMockedConnection {
      sqlDroid.getSchemas must throwA[UnsupportedOperationException]
    }

  }

  "getURL" should {
    "throw a UnsupportedOperationException" in new WithMockedConnection {
      sqlDroid.getURL must throwA[UnsupportedOperationException]
    }

  }

}
