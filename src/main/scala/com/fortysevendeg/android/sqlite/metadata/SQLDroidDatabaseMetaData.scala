package com.fortysevendeg.android.sqlite.metadata

import java.sql._

import com.fortysevendeg.android.sqlite.resultset.SQLDroidResultSet
import com.fortysevendeg.android.sqlite.{SQLDroidDriver, WrapperNotSupported}
import org.sqldroid.SQLDroidConnection
import SQLDroidDatabaseMetaData._

class SQLDroidDatabaseMetaData(connection: SQLDroidConnection)
  extends DatabaseMetaData
  with SQLiteDatabaseMetaDataSupport
  with WrapperNotSupported {

  lazy val attributesStatement = connection.prepareStatement(attributes)

  lazy val bestRowIdStatement = connection.prepareStatement(bestRowId)

  lazy val catalogsStatement = connection.prepareStatement(catalogs)

  lazy val columnPrivilegesStatement = connection.prepareStatement(columnPrivileges)

  lazy val procedureColumnsStatement = connection.prepareStatement(procedureColumns)

  lazy val proceduresStatement = connection.prepareStatement(procedures)

  lazy val superTablesStatement = connection.prepareStatement(superTables)

  lazy val superTypesStatement = connection.prepareStatement(superTypes)
  
  lazy val tablePrivilegesStatement = connection.prepareStatement(tablePrivileges)
  
  lazy val tableTypesStatement = connection.prepareStatement(tableTypes)

  lazy val udtsStatement = connection.prepareStatement(udts)

  lazy val versionColumnsStatement = connection.prepareStatement(versionColumns)

  override def getConnection: Connection = connection

  override def getAttributes(
    catalog: String,
    schemaPattern: String,
    typeNamePattern: String,
    attributeNamePattern: String
    ): ResultSet = attributesStatement.executeQuery()

  override def getBestRowIdentifier(
    catalog: String,
    schema: String,
    table: String,
    scope: Int,
    nullable: Boolean
    ): ResultSet = bestRowIdStatement.executeQuery()

  override def getCatalogs: ResultSet = catalogsStatement.executeQuery()

  override def getColumnPrivileges(
    catalog: String,
    schema: String,
    table: String,
    columnNamePattern: String
    ): ResultSet = columnPrivilegesStatement.executeQuery()

  override def getColumns(
    catalog: String,
    schemaPattern: String,
    tableNamePattern: String,
    columnNamePattern: String
    ): ResultSet = ???

  override def getCrossReference(
    parentCatalog: String,
    parentSchema: String,
    parentTable: String,
    foreignCatalog: String,
    foreignSchema: String,
    foreignTable: String): ResultSet = ???

  override def getExportedKeys(
    catalog: String,
    schema: String,
    table: String
    ): ResultSet = {
    val pk = PrimaryKey(connection, table)
  }

  override def getImportedKeys(
    catalog: String,
    schema: String,
    table: String
    ): ResultSet = ???

  override def getIndexInfo(
    catalog: String,
    schema: String,
    table: String,
    unique: Boolean,
    approximate: Boolean
    ): ResultSet = ???

  override def getPrimaryKeys(
    catalog: String,
    schema: String,
    table: String
    ): ResultSet = ???

  override def getProcedureColumns(
    catalog: String,
    schemaPattern: String,
    procedureNamePattern: String,
    columnNamePattern: String
    ): ResultSet = procedureColumnsStatement.executeQuery()

  override def getProcedures(
    catalog: String,
    schemaPattern: String,
    procedureNamePattern: String
    ): ResultSet = proceduresStatement.executeQuery()

  override def getSuperTables(
    catalog: String,
    schemaPattern: String,
    tableNamePattern: String
    ): ResultSet = superTablesStatement.executeQuery()

  override def getSuperTypes(
    catalog: String,
    schemaPattern: String,
    typeNamePattern: String
    ): ResultSet = superTypesStatement.executeQuery()

  override def getTablePrivileges(
    catalog: String,
    schemaPattern: String,
    tableNamePattern: String
    ): ResultSet = tablePrivilegesStatement.executeQuery()

  override def getTables(
    catalog: String,
    schemaPattern: String,
    tableNamePattern: String,
    types: scala.Array[String]
    ): ResultSet = ???

  override def getTableTypes: ResultSet = tableTypesStatement.executeQuery()

  override def getTypeInfo: ResultSet = new SQLDroidResultSet(connection.getDb.rawQuery(typeInfo, new scala.Array[String](0)))

  override def getUDTs(
    catalog: String,
    schemaPattern: String,
    typeNamePattern: String,
    types: scala.Array[Int]
    ): ResultSet = {
    udtsStatement.clearParameters()
    udtsStatement.executeQuery()
  }

  override def getVersionColumns(
    catalog: String,
    schema: String,
    table: String
    ): ResultSet = versionColumnsStatement.executeQuery()

  override def getCatalogSeparator: String = "."

  override def getCatalogTerm: String = "catalog"

  override def getDatabaseMajorVersion: Int = connection.getDb.database.getVersion

  override def getDatabaseMinorVersion: Int = 0

  override def getDatabaseProductName: String = "SQLite for Android"

  override def getDatabaseProductVersion: String = ""

  override def getDefaultTransactionIsolation: Int = Connection.TRANSACTION_SERIALIZABLE

  override def getDriverMajorVersion: Int = 1

  override def getDriverMinorVersion: Int = 1

  override def getDriverName: String = SQLDroidDriver.driverName

  override def getDriverVersion: String = SQLDroidDriver.driverVersion

  override def getExtraNameCharacters: String = ""

  override def getIdentifierQuoteString: String = " "

  override def getJDBCMajorVersion: Int = 2

  override def getJDBCMinorVersion: Int = 1

  override def getMaxBinaryLiteralLength: Int = 0

  override def getMaxCatalogNameLength: Int = 0

  override def getMaxCharLiteralLength: Int = 0

  override def getMaxColumnNameLength: Int = 0

  override def getMaxColumnsInGroupBy: Int = 0

  override def getMaxColumnsInIndex: Int = 0

  override def getMaxColumnsInOrderBy: Int = 0

  override def getMaxColumnsInSelect: Int = 0

  override def getMaxColumnsInTable: Int = 0

  override def getMaxConnections: Int = 0

  override def getMaxCursorNameLength: Int = 0

  override def getMaxIndexLength: Int = 0

  override def getMaxProcedureNameLength: Int = 0

  override def getMaxRowSize: Int = 0

  override def getMaxSchemaNameLength: Int = 0

  override def getMaxStatementLength: Int = 0

  override def getMaxStatements: Int = 0

  override def getMaxTableNameLength: Int = 0

  override def getMaxTablesInSelect: Int = 0

  override def getMaxUserNameLength: Int = 0

  override def getNumericFunctions: String = ""

  override def getResultSetHoldability: Int = ResultSet.CLOSE_CURSORS_AT_COMMIT

  override def getSchemaTerm: String = "schema"

  override def getSearchStringEscape: String = ""

  override def getSQLKeywords: String = ""

  override def getSQLStateType: Int = DatabaseMetaData.sqlStateSQL99

  override def getStringFunctions: String = ""

  override def getSystemFunctions: String = ""

  override def getTimeDateFunctions: String = ""

  override def getUserName: String = ""

  override def isReadOnly: Boolean = connection.isReadOnly

  override def getClientInfoProperties: ResultSet = throw new UnsupportedOperationException

  override def getFunctionColumns(
    catalog: String,
    schemaPattern: String,
    functionNamePattern: String,
    columnNamePattern: String
    ): ResultSet = throw new UnsupportedOperationException

  override def getFunctions(
    catalog: String,
    schemaPattern: String,
    functionNamePattern: String
    ): ResultSet = throw new UnsupportedOperationException

  override def getProcedureTerm: String = throw new UnsupportedOperationException

  override def getPseudoColumns(
    catalog: String,
    schemaPattern: String,
    tableNamePattern: String,
    columnNamePattern: String): ResultSet = throw new UnsupportedOperationException

  override def getRowIdLifetime: RowIdLifetime = throw new UnsupportedOperationException

  override def getSchemas(
    catalog: String,
    schemaPattern: String
    ): ResultSet = throw new UnsupportedOperationException

  override def getSchemas: ResultSet = throw new UnsupportedOperationException

  override def getURL: String = throw new UnsupportedOperationException
}

object SQLDroidDatabaseMetaData {
  
  lazy val attributes =
    """SELECT 
       | null AS TYPE_CAT, 
       | null AS TYPE_SCHEM, 
       | null AS TYPE_NAME, 
       | null AS ATTR_NAME, 
       | null AS DATA_TYPE, 
       | null AS ATTR_TYPE_NAME, 
       | null AS ATTR_SIZE, 
       | null AS DECIMAL_DIGITS, 
       | null AS NUM_PREC_RADIX, 
       | null AS NULLABLE, 
       | null AS REMARKS, 
       | null AS ATTR_DEF, 
       | null AS SQL_DATA_TYPE, 
       | null AS SQL_DATETIME_SUB, 
       | null AS CHAR_OCTET_LENGTH, 
       | null AS ORDINAL_POSITION, 
       | null AS IS_NULLABLE, 
       | null AS SCOPE_CATALOG, 
       | null AS SCOPE_SCHEMA, 
       | null AS SCOPE_TABLE, 
       | null AS SOURCE_DATA_TYPE 
       | LIMIT 0;""".stripMargin
  
  lazy val bestRowId =
    """SELECT
       | null AS SCOPE, 
       | null AS COLUMN_NAME, 
       | null AS DATA_TYPE, 
       | null AS TYPE_NAME, 
       | null AS COLUMN_SIZE, 
       | null AS BUFFER_LENGTH, 
       | null AS DECIMAL_DIGITS, 
       | null AS PSEUDO_COLUMN 
       | LIMIT 0;""".stripMargin
  
  lazy val catalogs = "SELECT null AS TABLE_CAT LIMIT 0;"
  
  lazy val columnPrivileges =
    """SELECT
      | null AS TABLE_CAT, 
      | null AS TABLE_SCHEM, 
      | null AS TABLE_NAME, 
      | null AS COLUMN_NAME, 
      | null AS GRANTOR, 
      | null AS GRANTEE, 
      | null AS PRIVILEGE, 
      | null AS IS_GRANTABLE 
      | LIMIT 0;""".stripMargin

  lazy val procedureColumns =
    """SELECT
      | null AS PROCEDURE_CAT,
      | null AS PROCEDURE_SCHEM,
      | null AS PROCEDURE_NAME,
      | null AS COLUMN_NAME,
      | null AS COLUMN_TYPE,
      | null AS DATA_TYPE,
      | null AS TYPE_NAME,
      | null AS PRECISION,
      | null AS LENGTH,
      | null AS SCALE,
      | null AS RADIX,
      | null AS NULLABLE,
      | null AS REMARKS
      | LIMIT 0;""".stripMargin
  
  lazy val procedures =
    """SELECT
      | null AS PROCEDURE_CAT,
      | null AS PROCEDURE_SCHEM,
      | null AS PROCEDURE_NAME,
      | null AS UNDEF1,
      | null AS UNDEF2,
      | null AS UNDEF3,
      | null AS REMARKS,
      | null AS PROCEDURE_TYPE
      | LIMIT 0;""".stripMargin
  
  lazy val superTables =
    """SELECT
      | null AS TYPE_CAT,
      | null AS TYPE_SCHEM,
      | null AS TYPE_NAME,
      | null AS SUPERTYPE_CAT,
      | null AS SUPERTYPE_SCHEM,
      | null AS SUPERTYPE_NAME
      | LIMIT 0;""".stripMargin
  
  lazy val superTypes =
    """SELECT
      | null AS TYPE_CAT,
      | null AS TYPE_SCHEM,
      | null AS TYPE_NAME,
      | null AS SUPERTYPE_CAT,
      | null AS SUPERTYPE_SCHEM,
      | null AS SUPERTYPE_NAME LIMIT 0;""".stripMargin
  
  lazy val tablePrivileges =
    """SELECT
      | null AS TABLE_CAT, 
      | null AS TABLE_SCHEM, 
      | null AS TABLE_NAME, 
      | null AS GRANTOR, 
      | null GRANTEE, 
      | null AS PRIVILEGE, 
      | null AS IS_GRANTABLE 
      | LIMIT 0;""".stripMargin
  
  lazy val tableTypes = "SELECT 'TABLE' AS TABLE_TYPE UNION SELECT 'VIEW' AS TABLE_TYPE;"

  lazy val typeInfo =
    s"""SELECT 
      | tn AS TYPE_NAME, 
      | dt AS DATA_TYPE, 0 AS PRECISION, 
      | null AS LITERAL_PREFIX, 
      | null AS LITERAL_SUFFIX, 
      | null AS CREATE_PARAMS, 
      | ${DatabaseMetaData.typeNullable} AS NULLABLE, 
      | 1 AS CASE_SENSITIVE, ${DatabaseMetaData.typeSearchable} AS SEARCHABLE, 
      | 0 AS UNSIGNED_ATTRIBUTE, 
      | 0 AS FIXED_PREC_SCALE, 
      | 0 AS AUTO_INCREMENT, 
      | null AS LOCAL_TYPE_NAME, 
      | 0 AS MINIMUM_SCALE, 
      | 0 AS MAXIMUM_SCALE, 
      | 0 AS SQL_DATA_TYPE, 
      | 0 AS SQL_DATETIME_SUB, 
      | 10 AS NUM_PREC_RADIX from (
      |  SELECT
      |   'BLOB' AS tn,
      |   ${Types.BLOB} AS dt
      |  UNION
      |  SELECT
      |   'NULL' AS tn,
      |   ${Types.NULL} AS dt
      |  UNION
      |  SELECT
      |   'REAL' AS tn,
      |   ${Types.REAL} AS dt
      |  UNION
      |  SELECT
      |   'TEXT' AS tn,
      |   ${Types.VARCHAR} AS dt
      |  UNION
      |  SELECT
      |   'INTEGER' AS tn,
      |   ${Types.INTEGER} AS dt
      | ) ORDER BY TYPE_NAME""".stripMargin
  
  lazy val udts =
    """SELECT
      | null AS TYPE_CAT, 
      | null AS TYPE_SCHEM, 
      | null AS TYPE_NAME, 
      | null AS CLASS_NAME, 
      | null AS DATA_TYPE, 
      | null AS REMARKS, 
      | null AS BASE_TYPE 
      | LIMIT 0;""".stripMargin
  
  lazy val versionColumns =
    """SELECT
      | null AS SCOPE, 
      | null AS COLUMN_NAME, 
      | null AS DATA_TYPE, 
      | null AS TYPE_NAME, 
      | null AS COLUMN_SIZE, 
      | null AS BUFFER_LENGTH, 
      | null AS DECIMAL_DIGITS, 
      | null AS PSEUDO_COLUMN 
      | LIMIT 0;""".stripMargin
}