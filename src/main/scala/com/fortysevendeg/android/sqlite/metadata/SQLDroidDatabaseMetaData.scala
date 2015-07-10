package com.fortysevendeg.android.sqlite.metadata

import java.sql._

import android.database.{Cursor, MatrixCursor, MergeCursor}
import com.fortysevendeg.android.sqlite.metadata.SQLDroidDatabaseMetaData._
import com.fortysevendeg.android.sqlite.resultset.SQLDroidResultSet
import com.fortysevendeg.android.sqlite.util.CursorUtils._
import com.fortysevendeg.android.sqlite.{SQLDroidDriver, WrapperNotSupported, _}
import org.sqldroid.SQLDroidConnection

import scala.util.{Failure, Success, Try}

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
    ): ResultSet = {
    /*    
     get the list of tables matching the pattern (getTables)
     create a Matrix Cursor for each of the tables
     create a merge cursor from all the Matrix Cursors
     and return the columname and type from:
      "PRAGMA table_info(tablename)"
     which returns data like this:
        sqlite> PRAGMA lastyear.table_info(gross_sales); 
        cid|name|type|notnull|dflt_value|pk 
        0|year|INTEGER|0|'2006'|0 
        1|month|TEXT|0||0 
        2|monthlygross|REAL|0||0 
        3|sortcol|INTEGER|0||0 
        sqlite>
    
     and then make the cursor have these columns
        TABLE_CAT String => table catalog (may be null)
        TABLE_SCHEM String => table schema (may be null)
        TABLE_NAME String => table name
        COLUMN_NAME String => column name
        DATA_TYPE int => SQL type from java.sql.Types
        TYPE_NAME String => Data source dependent type name, for a UDT the type name is fully qualified
        COLUMN_SIZE int => column size.
        BUFFER_LENGTH is not used.
        DECIMAL_DIGITS int => the number of fractional digits. Null is returned for data types where DECIMAL_DIGITS is not applicable.
        NUM_PREC_RADIX int => Radix (typically either 10 or 2)
        NULLABLE int => is NULL allowed.
        columnNoNulls - might not allow NULL values
        columnNullable - definitely allows NULL values
        columnNullableUnknown - nullability unknown
        REMARKS String => comment describing column (may be null)
        COLUMN_DEF String => default value for the column, which should be interpreted as a string when the value is enclosed in single quotes (may be null)
        SQL_DATA_TYPE int => unused
        SQL_DATETIME_SUB int => unused
        CHAR_OCTET_LENGTH int => for char types the maximum number of bytes in the column
        ORDINAL_POSITION int => index of column in table (starting at 1)
        IS_NULLABLE String => ISO rules are used to determine the nullability for a column.
        YES --- if the parameter can include NULLs
        NO --- if the parameter cannot include NULLs
        empty string --- if the nullability for the parameter is unknown
        SCOPE_CATLOG String => catalog of table that is the scope of a reference attribute (null if DATA_TYPE isn't REF)
        SCOPE_SCHEMA String => schema of table that is the scope of a reference attribute (null if the DATA_TYPE isn't REF)
        SCOPE_TABLE String => table name that this the scope of a reference attribure (null if the DATA_TYPE isn't REF)
        SOURCE_DATA_TYPE short => source type of a distinct type or user-generated Ref type, SQL type from java.sql.Types (null if DATA_TYPE isn't DISTINCT or user-generated REF)
        IS_AUTOINCREMENT String => Indicates whether this column is auto incremented
        YES --- if the column is auto incremented
        NO --- if the column is not auto incremented
        empty string --- if it cannot be determined whether the column is auto incremented parameter is unknown
     */
    val columnNames = scala.Array(
      "TABLE_CAT", "TABLE_SCHEM", "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE",
      "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX",
      "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB",
      "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA",
      "SCOPE_TABLE", "SOURCE_DATA_TYPE", "IS_AUTOINCREMENT")
    val types = scala.Array(tableType, viewType)
    val resultSet = getTables(catalog, schemaPattern, tableNamePattern, types)
    val cursorList = resultSet.process { rs =>
      val tableName = rs.getString(3)
      val matrixCursor = new MatrixCursor(columnNames)
      val tableInfoCursor = connection.getDb.rawQuery(pragmaTable(rs.getString(3)))
      tableInfoCursor match {
        case cursor: Cursor =>
          cursor.process { c =>
            val a = c.getString(1)
            val fieldType = c.getString(2)
            val sqlType = fieldType.toUpperCase match {
              case "TEXT" => java.sql.Types.VARCHAR
              case "CHAR" => java.sql.Types.VARCHAR
              case "NUMERIC" => java.sql.Types.NUMERIC
              case "INT" => java.sql.Types.INTEGER
              case "REAL" => java.sql.Types.REAL
              case "BLOB" => java.sql.Types.BLOB
              case _ => java.sql.Types.NULL
            }
            val nullable = c.getInt(3) match {
              case 0 => 0
              case 1 => 1
              case _ => 2
            }

            val columnValues = scala.Array[AnyRef](
              javaNull, javaNull, tableName, a, new Integer(sqlType),
              fieldType, javaNull, javaNull, javaNull, new Integer(10),
              new Integer(nullable), javaNull, javaNull, javaNull, javaNull,
              new Integer(-1), new Integer(-1), "", javaNull, javaNull,
              javaNull, javaNull, "")

            matrixCursor.addRow(columnValues)
          }
      }
      matrixCursor
    }

    cursorList match {
      case Seq(cursor) => new SQLDroidResultSet(cursor)
      case seq if seq.size > 1 => new SQLDroidResultSet(new MergeCursor(seq.toArray))
      case _ => new SQLDroidResultSet(new MatrixCursor(columnNames, 0))
    }
  }

  override def getCrossReference(
    parentCatalog: String,
    parentSchema: String,
    parentTable: String,
    foreignCatalog: String,
    foreignSchema: String,
    foreignTable: String): ResultSet =
    (defined(parentCatalog, parentSchema, parentTable), defined(foreignCatalog, foreignSchema, foreignTable)) match {
    case (true, false) =>
      getExportedKeys(parentCatalog, parentSchema, parentTable)
    case (false, true) =>
      getImportedKeys(foreignCatalog, foreignSchema, foreignTable)
    case (true, true) =>
      connection.createStatement().executeQuery(crossReference(parentCatalog, parentSchema, parentTable, foreignCatalog, foreignSchema, foreignTable))
    case _ =>
      javaNull
  }

  private[this] def defined(options: String*): Boolean = options forall (Option(_).isDefined)

  override def getExportedKeys(
    catalog: String,
    schema: String,
    table: String
    ): ResultSet = {
    val pk = PrimaryKey(connection, table)

    val tableList = connection.createStatement.executeQuery(sqliteMasterName("table")).process(_.getString(1))

    val exportedKeySelect = tableList map { table =>
      Try(connection.createStatement.executeQuery(foreignKeys(table))) match {
        case Success(resultSet) => generateExportedKeySelect(resultSet, table, pk)
        case _ => Seq.empty
      }
    }

    val selectSeq = exportedKeySelect.flatten[String]
    val select = if (selectSeq.isEmpty) None else Some(selectSeq.mkString(" UNION ALL "))
    connection.createStatement.executeQuery(exportedKeys(Option(catalog), Option(schema), table, select, pk))
  }

  private[this] def generateExportedKeySelect(resultSet: ResultSet, table: String, primaryKey: PrimaryKey): Seq[String] =
    resultSet.process { rs =>
      val keySeq = rs.getInt(2) + 1
      val fcn = rs.getString(4)
      val pkColumn = (Option(rs.getString(5)), primaryKey.columns) match {
        case (Some(name), _) => name
        case (_, Seq(head, rest @ _ *)) => head
        case _ => "''"
      }
      val ur = ruleMap(rs.getString(6))
      val dr = ruleMap(rs.getString(7))
      Option(rs.getString(3)) match {
        case Some(t) if t.equalsIgnoreCase(table) => Some(exportedKeysSelect(keySeq, table, fcn, pkColumn, ur, dr, fkn(table)))
        case _ => None
      }
    }.flatten[String]

  private[this] def fkn(table: String): Option[String] = {
    val rsSql = connection.createStatement().executeQuery(sqliteMasterSql(table))
    if (rsSql.next())
      PrimaryKey.namedPattern.findFirstMatchIn(rsSql.getString(1)) map { m =>
        Some(s", '${m.group(1).toLowerCase}' AS fkn")
      } getOrElse Some(", '' as fkn")
    else None
  }

  override def getImportedKeys(
    catalog: String,
    schema: String,
    table: String
    ): ResultSet = {
    val selectSeq = Try(connection.createStatement.executeQuery(foreignKeys(table))) match {
      case Success(resultSet) => generateImportedKeySelect(resultSet, table)
      case Failure(_) => Seq.empty
    }
    connection.createStatement.executeQuery(
      importedKeys(
        catalog = Option(catalog),
        schema = Option(schema),
        table = table,
        importedKeysSelect = if (selectSeq.isEmpty) importedKeysSelectEmpty else selectSeq.mkString(" UNION ALL ")))
  }

  private[this] def generateImportedKeySelect(resultSet: ResultSet, table: String): Seq[String] =
    resultSet.process { rs =>
      val keySeq = rs.getInt(2) + 1
      val pkTable = rs.getString(3)
      val fkColumn = rs.getString(4)
      val pkColumn = Option(rs.getString(5)) orElse PrimaryKey(connection, table).columns.headOption
      val updateRule = rs.getString(6)
      val deleteRule = rs.getString(7)
      importedKeysSelect(keySeq, pkTable, fkColumn, pkColumn getOrElse "", updateRule, deleteRule)
    }

  override def getIndexInfo(
    catalog: String,
    schema: String,
    table: String,
    unique: Boolean,
    approximate: Boolean
    ): ResultSet = {
    val statement = connection.createStatement
    Try {
      statement.executeQuery(pragmaIndexList(table))
    } match {
      case Success(resultSet) =>
        val indexSeq: Seq[(String, Int)] = resultSet.process { rs =>
          (rs.getString(3), rs.getInt(2))
        }.reverse

        val indexInfoSeq = indexSeq flatMap { tuple =>
          val (indexName, index) = tuple
          statement.executeQuery(pragmaIndexInfo(indexName)).process { rs =>
            IndexInfo(indexName, index, rs.getInt(1) + 1, rs.getString(3))
          }
        }

        statement.executeQuery(indexInfo(table, indexInfoSeq))
      case Failure(e) =>
        statement.executeQuery(emptyIndexInfo(table))
    }
  }

  override def getPrimaryKeys(
    catalog: String,
    schema: String,
    table: String
    ): ResultSet = {
    val matrixCursor = new MatrixCursor(
      scala.Array("TABLE_CAT", "TABLE_SCHEM", "TABLE_NAME", "COLUMN_NAME", "KEY_SEQ", "PK_NAME"))
    Option(table) map (t => connection.getDb.rawQuery(pragmaTable(t))) match {
      case Some(c: Cursor) =>
        c.process { c =>
          if (c.getInt(5) > 0)
            matrixCursor.addRow(scala.Array[AnyRef](javaNull, javaNull, table, c.getString(1), javaNull))
        }
      case _ =>
    }
    new SQLDroidResultSet(matrixCursor)
  }

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
    ): ResultSet = {
    /*
      .tables command from here:
        http://www.sqlite.org/sqlite.html

      SELECT name FROM sqlite_master
      WHERE type IN ('table','view') AND name NOT LIKE 'sqlite_%'
      UNION ALL
      SELECT name FROM sqlite_temp_master
      WHERE type IN ('table','view')
      ORDER BY 1

      Documentation for getTables() mandates a certain format for the returned result set.
      To make the return here compatible with the standard, the following statement is
      executed.  Note that the only returned value of any consequence is still the table name
      but now it's the third column in the result set and all the other columns are present
      The type, which can be 'view', 'table' (maybe also 'index') is returned as the type.
      The sort will be wrong if multiple types are selected.  The solution would be to select
      one time with type = ('table' | 'view' ), etc. but I think these would have to be
      substituted by hand (that is, I don't think a ? option could be used - but I could be wrong about that.
     */
    val database = connection.getDb
    val tablePattern = Option(tableNamePattern) getOrElse "%"
    val safeTypes = Option(types) getOrElse scala.Array(tableType)
    safeTypes map { tableType =>
      database.rawQuery(
        cursorTableType(tableType),
        scala.Array(tablePattern, tablePattern.toUpperCase, tablePattern, tablePattern.toUpperCase))
    } match {
      case scala.Array(cursor) => new SQLDroidResultSet(cursor)
      case array if array.length > 1 => new SQLDroidResultSet(new MergeCursor(array))
      case _ => javaNull
    }
  }

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

  case class IndexInfo(name: String, index: Int, op: Int, cn: String)

  val tableType = "TABLE"

  val viewType = "VIEW"

  def ruleMap(rule: String) = rule match {
    case "NO ACTION" => DatabaseMetaData.importedKeyNoAction
    case "CASCADE" => DatabaseMetaData.importedKeyCascade
    case "RESTRICT" => DatabaseMetaData.importedKeyRestrict
    case "SET NULL" => DatabaseMetaData.importedKeySetNull
    case "SET DEFAULT" => DatabaseMetaData.importedKeySetDefault
  }

  def sqliteMasterSql(table: String) = s"SELECT sql FROM sqlite_master WHERE lower(name) = lower('$table')"

  def foreignKeys(table: String) = s"pragma foreign_key_list('$table')"

  def sqliteMasterName(`type`: String) = s"SELECT name FROM sqlite_master WHERE type = '${`type`}'"

  def importedKeys(
    catalog: Option[String],
    schema: Option[String],
    table: String,
    importedKeysSelect: String) =
    s"""
       | SELECT
       |  '${quoteNull(catalog)}' AS PKTABLE_CAT,
       |  '${quoteNull(schema)}' AS PKTABLE_SCHEM,
       |  ptn AS PKTABLE_NAME,
       |  pcn as PKCOLUMN_NAME,
       |  '${quoteNull(catalog)}' AS FKTABLE_CAT,
       |  '${quoteNull(schema)}' AS FKTABLE_SCHEM,
       |  '${table.escape}' AS FKTABLE_NAME,
       |  fcn AS FKCOLUMN_NAME,
       |  ks AS KEY_SEQ,
       |  ur AS UPDATE_RULE,
       |  dr AS DELETE_RULE,
       |  '' AS FK_NAME,
       |  '' AS PK_NAME,
       |  ${DatabaseMetaData.importedKeyInitiallyDeferred} AS DEFERRABILITY
       | FROM ($importedKeysSelect)
     """.stripMargin

  def importedKeysSelect(keySeq: Int, table: String, fkColumn: String, pkColumn: String, updateRule: String, deleteRule: String) =
    s"""
       | SELECT
       |  $keySeq AS ks,
       |  '${table.escape}' AS ptn,
       |  '${fkColumn.escape}' AS fcn,
       |  '${pkColumn.escape}' AS pcn,
       |  CASE '${updateRule.escape}'
       |   WHEN 'NO ACTION' THEN ${DatabaseMetaData.importedKeyNoAction}
       |   WHEN 'CASCADE' THEN ${DatabaseMetaData.importedKeyCascade}
       |   WHEN 'RESTRICT' THEN ${DatabaseMetaData.importedKeyRestrict}
       |   WHEN 'SET NULL' THEN ${DatabaseMetaData.importedKeySetNull}
       |   WHEN 'SET DEFAULT' THEN ${DatabaseMetaData.importedKeySetDefault}
       |  end AS ur,
       |  CASE '${deleteRule.escape}'
       |   WHEN 'NO ACTION' THEN ${DatabaseMetaData.importedKeyNoAction}
       |   WHEN 'CASCADE' THEN ${DatabaseMetaData.importedKeyCascade}
       |   WHEN 'RESTRICT' THEN ${DatabaseMetaData.importedKeyRestrict}
       |   WHEN 'SET NULL' THEN ${DatabaseMetaData.importedKeySetNull}
       |   WHEN 'SET DEFAULT' THEN ${DatabaseMetaData.importedKeySetDefault}
       |  end AS dr
     """.stripMargin

  def importedKeysSelectEmpty =
    s"""
       | SELECT
       |  -1 AS ks,
       |  '' AS ptn,
       |  '' AS fcn,
       |  '' AS pcn,
       |  ${DatabaseMetaData.importedKeyNoAction} AS ur,
       |  ${DatabaseMetaData.importedKeyNoAction} AS dr
       | LIMIT 0
     """.stripMargin

  def exportedKeysSelect(keySeq: Int, table: String, fkColumn: String, pkColumn: String, ur: Int, dr: Int, fkn: Option[String]) =
    s"""
       | SELECT
       |  $keySeq AS ks,
       |  lower('$table') AS fkt,
       |  lower('$fkColumn') AS fkColumn,
       |  $pkColumn AS pcn,
       |  $ur AS ur,
       |  $dr AS dr
       |  ${fkn getOrElse ""}
     """.stripMargin

  def exportedKeys(
    catalog: Option[String],
    schema: Option[String],
    table: String,
    exportedKeysSelect: Option[String],
    primaryKey: PrimaryKey) =
    s"""
       | SELECT
       |  ${quoteNull(catalog)} AS PKTABLE_CAT,
       |  ${quoteNull(schema)} AS PKTABLE_SCHEM,
       |  '$table' AS PKTABLE_NAME,
       |  ${safeName(exportedKeysSelect.isDefined, "pcn", "''")} AS PKCOLUMN_NAME,
       |  ${quoteNull(catalog)} AS FKTABLE_CAT,
       |  ${quoteNull(schema)} AS FKTABLE_SCHEM,
       |  ${safeName(exportedKeysSelect.isDefined, "fkt")} AS FKTABLE_NAME,
       |  ${safeName(exportedKeysSelect.isDefined, "fcn")} AS FKCOLUMN_NAME,
       |  ${safeName(exportedKeysSelect.isDefined, "ks", "-1")} AS KEY_SEQ,
       |  ${safeName(exportedKeysSelect.isDefined, "ur", "3")} AS UPDATE_RULE,
       |  ${safeName(exportedKeysSelect.isDefined, "dr", "3")} AS DELETE_RULE,
       |  ${safeName(exportedKeysSelect.isDefined, "fkn")} AS FK_NAME,
       |  ${primaryKey.name getOrElse "''"} AS PK_NAME,
       |  ${DatabaseMetaData.importedKeyInitiallyDeferred} AS DEFERRABILITY
       |  ${exportedKeysSelect map (e => s" FROM ($e) ORDER BY fkt") getOrElse "LIMIT 0"}""".stripMargin

  private[this] def safeName(b: Boolean, v: String, d: String = "''") = if (b) v else d

  private[this] def quoteNull(s: Option[String]) = s map (s => s"'${s.escape}'") getOrElse "null"

  def emptyIndexInfo(table: String) =
    indexInfoBody(table, "SELECT null AS un, null AS n, null AS op, null AS cn")

  def indexInfo(
    table: String,
    indexInfoSeq: Seq[IndexInfo]) =
    indexInfoBody(table, selectInfoSeq(indexInfoSeq))

  private[this] def indexInfoBody(
    table: String,
    select: String) =
    s"""
       | SELECT
       |  null AS TABLE_CAT,
       |  null AS TABLE_SCHEM,
       |  '$table' AS TABLE_NAME,
       |  un AS NON_UNIQUE,
       |  null AS INDEX_QUALIFIER,
       |  n AS INDEX_NAME,
       |  ${DatabaseMetaData.tableIndexOther} AS TYPE,
       |  op AS ORDINAL_POSITION,
       |  cn AS COLUMN_NAME,
       |  null AS ASC_OR_DESC,
       |  0 AS CARDINALITY,
       |  0 AS PAGES,
       |  null AS FILTER_CONDITION
       |  FROM ($select)
       |  LIMIT 0;""".stripMargin

  private[this] def selectInfoSeq(indexInfoSeq: Seq[IndexInfo]): String =
    (indexInfoSeq map { i =>
      s"SELECT ${1 - i.index} AS un, '${i.name}' AS n, ${i.op} AS op, ${i.cn} AS cn"
    }).mkString(" UNION ALL ")

  def cursorTableType(tableType: String): String =
    s"""
       | SELECT
       |  null AS TABLE_CAT,
       |  null AS TABLE_SCHEM,
       |  tbl_name AS TABLE_NAME,
       |  '$tableType' AS TABLE_TYPE,
       |  'No Comment' AS REMARKS,
       |  null AS TYPE_CAT,
       |  null AS TYPE_SCHEM,
       |  null AS TYPE_NAME,
       |  null AS SELF_REFERENCING_COL_NAME,
       |  null AS REF_GENERATION
       | FROM sqlite_master
       | WHERE tbl_name LIKE ?
       | AND name NOT LIKE 'sqlite_%'
       | AND name NOT LIKE 'android_metadata'
       | AND upper(type) = ?" + "
       | UNION ALL
       | SELECT
       |  null AS TABLE_CAT,
       |  null AS TABLE_SCHEM,
       |  tbl_name AS TABLE_NAME,
       |  '$tableType' AS TABLE_TYPE,
       |  'No Comment' AS REMARKS,
       |  null AS TYPE_CAT,
       |  null AS TYPE_SCHEM,
       |  null AS TYPE_NAME,
       |  null AS SELF_REFERENCING_COL_NAME,
       |  null AS REF_GENERATION
       | FROM sqlite_temp_master
       | WHERE tbl_name LIKE ?
       | AND name NOT LIKE 'android_metadata'
       | AND upper(type) = ?
       | ORDER BY 3""".stripMargin

  def pragmaTable(tableName: String) = s"pragma table_info('$tableName')"

  def pragmaIndexList(tableName: String) = s"pragma index_list('$tableName')"

  def pragmaIndexInfo(tableName: String) = s"pragma index_info('$tableName')"

  def crossReference(
    parentCatalog: String,
    parentSchema: String,
    parentTable: String,
    foreignCatalog: String,
    foreignSchema: String,
    foreignTable: String) =
    s"""
       |SELECT
       | '$parentCatalog' AS PKTABLE_CAT,
       | '$parentSchema' AS PKTABLE_SCHEM,
       | '$parentTable' AS PKTABLE_NAME,
       | '' AS PKCOLUMN_NAME,
       | '$foreignCatalog' AS FKTABLE_CAT,
       | '$foreignSchema' AS FKTABLE_SCHEM,
       | '$foreignTable' AS FKTABLE_NAME,
       | '' AS FKCOLUMN_NAME,
       | -1 AS KEY_SEQ,
       | 3 AS UPDATE_RULE,
       | 3 AS DELETE_RULE,
       | '' AS FK_NAME,
       | '' AS PK_NAME,
       | ${DatabaseMetaData.importedKeyInitiallyDeferred} AS DEFERRABILITY
       | LIMIT 0""".stripMargin

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
      | SELECT
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