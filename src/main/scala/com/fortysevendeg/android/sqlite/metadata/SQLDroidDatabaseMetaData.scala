package com.fortysevendeg.android.sqlite.metadata

import java.sql._

import android.database.{MatrixCursor, MergeCursor}
import com.fortysevendeg.android.sqlite.metadata.SQLDroidDatabaseMetaData._
import com.fortysevendeg.android.sqlite.resultset.SQLDroidResultSet
import com.fortysevendeg.android.sqlite.util.CursorUtils._
import com.fortysevendeg.android.sqlite.{SQLDroidDriver, WrapperNotSupported, _}

import scala.util.{Failure, Success, Try}

class SQLDroidDatabaseMetaData(connection: Connection)
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

  lazy val typeInfoStatement = connection.prepareStatement(typeInfo)

  lazy val udtsStatement = connection.prepareStatement(udts)

  lazy val versionColumnsStatement = connection.prepareStatement(versionColumns)

  implicit val c: Connection = connection

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
    val columnNames = scala.Array(
      "TABLE_CAT", "TABLE_SCHEM", "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE",
      "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX",
      "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB",
      "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA",
      "SCOPE_TABLE", "SOURCE_DATA_TYPE", "IS_AUTOINCREMENT")
    val types = scala.Array(tableType, viewType)

    val tableList = getTables(catalog, schemaPattern, tableNamePattern, types).process(_.getString("TABLE_NAME"))

    def fieldTypeToSqlType(fieldType: String): Int = {
      fieldType.toUpperCase match {
        case "TEXT" => java.sql.Types.VARCHAR
        case "CHAR" => java.sql.Types.VARCHAR
        case "NUMERIC" => java.sql.Types.NUMERIC
        case "INTEGER" => java.sql.Types.INTEGER
        case "REAL" => java.sql.Types.REAL
        case "BLOB" => java.sql.Types.BLOB
        case _ => java.sql.Types.NULL
      }
    }

    def nullableToInt(nullable: Int): Int = {
      nullable match {
        case 0 => 0
        case 1 => 1
        case _ => 2
      }
    }

    val cursorList = WithStatement { statement =>
      tableList map { tableName =>
        val matrixCursor = new MatrixCursor(columnNames)

        statement.executeQuery(pragmaTable(tableName)).process { rs =>
          val columnName = rs.getString("NAME")
          val fieldType = rs.getString("TYPE")
          val sqlType = fieldTypeToSqlType(fieldType)
          val nullable = nullableToInt(rs.getInt("NOTNULL"))

          val columnValues = scala.Array[AnyRef](
            javaNull, javaNull, tableName, columnName, new Integer(sqlType),
            fieldType, javaNull, javaNull, javaNull, new Integer(10),
            new Integer(nullable), javaNull, javaNull, javaNull, javaNull,
            new Integer(-1), new Integer(-1), "", javaNull, javaNull,
            javaNull, javaNull, "")

          matrixCursor.addRow(columnValues)
        }


        matrixCursor
      }
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
      statementResultSet(crossReference(parentCatalog, parentSchema, parentTable, foreignCatalog, foreignSchema, foreignTable))
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

    val exportedKeySelect = WithStatement { statement =>
      val tableList = statement.executeQuery(sqliteMasterName(tableType)).process(_.getString(1))

      tableList map { table =>
        Try(statement.executeQuery(foreignKeys(table))) match {
          case Success(resultSet) => generateExportedKeySelect(resultSet, table, pk)
          case _ => Seq.empty
        }
      }
    }

    val selectSeq = exportedKeySelect.flatten[String]
    val select = if (selectSeq.isEmpty) None else Some(selectSeq.mkString(unionAll))
    statementResultSet(
      exportedKeys(
        catalog = Option(catalog),
        schema = Option(schema),
        table = table,
        exportedKeysSelect = select,
        primaryKey = pk))
  }

  private[this] def generateExportedKeySelect(resultSet: ResultSet, table: String, primaryKey: PrimaryKey): Seq[String] = {
    val tablePos = resultSet.findColumn("TABLE")
    val seqPos = resultSet.findColumn("SEQ")
    val fkPos = resultSet.findColumn("FROM")
    val pkPos = resultSet.findColumn("TO")
    val updatePos = resultSet.findColumn("ON_UPDATE")
    val deletePos = resultSet.findColumn("ON_DELETE")
    resultSet.process { rs =>
      val keySeq = rs.getInt(seqPos) + 1
      val fkColumn = rs.getString(fkPos)
      val pkColumn = (Option(rs.getString(pkPos)), primaryKey.columns) match {
        case (Some(name), _) => name
        case (_, Seq(head, rest@_ *)) => head
        case _ => "''"
      }
      val ur = ruleMap(rs.getString(updatePos))
      val dr = ruleMap(rs.getString(deletePos))
      Option(rs.getString(tablePos)) match {
        case Some(t) if t.equalsIgnoreCase(table) => Some(exportedKeysSelect(keySeq, table, fkColumn, pkColumn, ur, dr, fkn(table)))
        case _ => None
      }
    }.flatten[String]
  }

  private[this] def fkn(table: String): Option[String] = {
    WithStatement { statement =>
      statement.executeQuery(sqliteMasterSql(table)).processOne(rs =>
        PrimaryKey.namedPattern.findFirstMatchIn(rs.getString(1)) map { m =>
          s", '${m.group(1).toLowerCase}' AS fkn"
        } getOrElse ", '' as fkn"
      )
    }
  }

  override def getImportedKeys(
    catalog: String,
    schema: String,
    table: String
    ): ResultSet = {

    val selectSeq = WithStatement { statement =>
      Try(statement.executeQuery(foreignKeys(table))) match {
        case Success(resultSet) => generateImportedKeySelect(resultSet, table)
        case Failure(_) => Seq.empty
      }
    }

    statementResultSet(
      importedKeys(
        catalog = Option(catalog),
        schema = Option(schema),
        table = table,
        importedKeysSelect = if (selectSeq.isEmpty) importedKeysSelectEmpty else selectSeq.mkString(unionAll)))
  }

  private[this] def generateImportedKeySelect(resultSet: ResultSet, table: String): Seq[String] = {
    val tablePos = resultSet.findColumn("TABLE")
    val seqPos = resultSet.findColumn("SEQ")
    val fkPos = resultSet.findColumn("FROM")
    val pkPos = resultSet.findColumn("TO")
    val updatePos = resultSet.findColumn("ON_UPDATE")
    val deletePos = resultSet.findColumn("ON_DELETE")
    resultSet.process { rs =>
      val keySeq = rs.getInt(seqPos) + 1
      val pkTable = rs.getString(tablePos)
      val fkColumn = rs.getString(fkPos)
      val pkColumn = Option(rs.getString(pkPos)) orElse PrimaryKey(connection, table).columns.headOption
      val updateRule = rs.getString(updatePos)
      val deleteRule = rs.getString(deletePos)
      importedKeysSelect(keySeq, pkTable, fkColumn, pkColumn getOrElse "", updateRule, deleteRule)
    }
  }

  override def getIndexInfo(
    catalog: String,
    schema: String,
    table: String,
    unique: Boolean,
    approximate: Boolean
    ): ResultSet = {
    val sql = WithStatement { statement =>
      Try(statement.executeQuery(pragmaIndexList(table))) match {
        case Success(resultSet) if !resultSet.isClosed =>
          val seqPos = resultSet.findColumn("SEQ")
          val namePos = resultSet.findColumn("NAME")
          val indexSeq: Seq[(String, Int)] = resultSet.process { rs =>
            (rs.getString(namePos), rs.getInt(seqPos))
          }.reverse

          val indexInfoSeq = indexSeq flatMap { tuple =>
            val (indexName, index) = tuple
            statement.executeQuery(pragmaIndexInfo(indexName)).process { rs =>
              IndexInfo(indexName, index, rs.getInt(rs.findColumn("SEQNO")) + 1, rs.getString(rs.findColumn("NAME")))
            }
          }

          indexInfo(table, indexInfoSeq)
        case _ =>
          emptyIndexInfo(table)
      }
    }
    statementResultSet(sql)
  }

  override def getPrimaryKeys(
    catalog: String,
    schema: String,
    table: String
    ): ResultSet = {
    val matrixCursor = new MatrixCursor(primaryKeysColumns)
    WithStatement { statement =>
      Try(statement.executeQuery(pragmaTable(table))) match {
        case Success(resultSet) =>
          val pkPos = resultSet.findColumn("PK")
          val namePos = resultSet.findColumn("NAME")
          resultSet.process { rs =>
            if (rs.getInt(pkPos) > 0)
              matrixCursor.addRow(scala.Array[AnyRef](javaNull, javaNull, table, rs.getString(namePos), javaNull, javaNull))
          }
        case _ =>
      }
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
    val tablePattern = Option(tableNamePattern) getOrElse "%"
    val safeTypes = Option(types) match {
      case Some(a) if a.length > 0 => a
      case _ => scala.Array(tableType)
    }
    safeTypes map { tableType =>
      cursorTableType(tableType, tablePattern)
    } match {
      case scala.Array(sql) =>
        statementResultSet(sql)
      case array if array.length > 1 =>
        statementResultSet(array.mkString(unionAll))
      case _ =>
        javaNull
    }
  }

  override def getTableTypes: ResultSet = tableTypesStatement.executeQuery()

  override def getTypeInfo: ResultSet = typeInfoStatement.executeQuery()

  override def getUDTs(
    catalog: String,
    schemaPattern: String,
    typeNamePattern: String,
    types: scala.Array[Int]
    ): ResultSet = udtsStatement.executeQuery()

  override def getVersionColumns(
    catalog: String,
    schema: String,
    table: String
    ): ResultSet = versionColumnsStatement.executeQuery()

  override val getCatalogSeparator: String = "."

  override val getCatalogTerm: String = "catalog"

  override def getDatabaseMajorVersion: Int = connection match {
    case c: SQLDroidConnection => c.withOpenDatabase(_.database.getVersion)
    case _ => 0
  }

  override val getDatabaseMinorVersion: Int = 0

  override val getDatabaseProductName: String = "SQLite for Android"

  override val getDatabaseProductVersion: String = ""

  override val getDefaultTransactionIsolation: Int = Connection.TRANSACTION_SERIALIZABLE

  override val getDriverMajorVersion: Int = 1

  override val getDriverMinorVersion: Int = 1

  override val getDriverName: String = SQLDroidDriver.driverName

  override val getDriverVersion: String = SQLDroidDriver.driverVersion

  override val getExtraNameCharacters: String = ""

  override val getIdentifierQuoteString: String = " "

  override val getJDBCMajorVersion: Int = 2

  override val getJDBCMinorVersion: Int = 1

  override val getMaxBinaryLiteralLength: Int = 0

  override val getMaxCatalogNameLength: Int = 0

  override val getMaxCharLiteralLength: Int = 0

  override val getMaxColumnNameLength: Int = 0

  override val getMaxColumnsInGroupBy: Int = 0

  override val getMaxColumnsInIndex: Int = 0

  override val getMaxColumnsInOrderBy: Int = 0

  override val getMaxColumnsInSelect: Int = 0

  override val getMaxColumnsInTable: Int = 0

  override val getMaxConnections: Int = 0

  override val getMaxCursorNameLength: Int = 0

  override val getMaxIndexLength: Int = 0

  override val getMaxProcedureNameLength: Int = 0

  override val getMaxRowSize: Int = 0

  override val getMaxSchemaNameLength: Int = 0

  override val getMaxStatementLength: Int = 0

  override val getMaxStatements: Int = 0

  override val getMaxTableNameLength: Int = 0

  override val getMaxTablesInSelect: Int = 0

  override val getMaxUserNameLength: Int = 0

  override val getNumericFunctions: String = ""

  override val getResultSetHoldability: Int = ResultSet.CLOSE_CURSORS_AT_COMMIT

  override val getSchemaTerm: String = "schema"

  override val getSearchStringEscape: String = ""

  override val getSQLKeywords: String = ""

  override val getSQLStateType: Int = DatabaseMetaData.sqlStateSQL99

  override val getStringFunctions: String = ""

  override val getSystemFunctions: String = ""

  override val getTimeDateFunctions: String = ""

  override val getUserName: String = ""

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

  private[this] def statementResultSet(sql: String): ResultSet = {
    val statement = connection.createStatement()
    new StatementResultSetWrapper(statement, statement.executeQuery(sql))
  }
}

object SQLDroidDatabaseMetaData {

  case class IndexInfo(name: String, index: Int, op: Int, cn: String)

  val tableType = "TABLE"

  val viewType = "VIEW"

  val unionAll = " UNION ALL "

  val primaryKeysColumns = scala.Array("TABLE_CAT", "TABLE_SCHEM", "TABLE_NAME", "COLUMN_NAME", "KEY_SEQ", "PK_NAME")

  def ruleMap(rule: String) = rule match {
    case "NO ACTION" => DatabaseMetaData.importedKeyNoAction
    case "CASCADE" => DatabaseMetaData.importedKeyCascade
    case "RESTRICT" => DatabaseMetaData.importedKeyRestrict
    case "SET NULL" => DatabaseMetaData.importedKeySetNull
    case "SET DEFAULT" => DatabaseMetaData.importedKeySetDefault
  }

  def sqliteMasterSql(table: String) = s"SELECT sql FROM sqlite_master WHERE lower(name) = lower('${table.escape}')"

  def foreignKeys(table: String) = s"pragma foreign_key_list('${table.escape}')"

  def sqliteMasterName(`type`: String) = s"SELECT name FROM sqlite_master WHERE lower(type) = lower('${`type`.escape}')"

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
       |  lower('${table.escape}') AS fkt,
       |  lower('${fkColumn.escape}') AS fkColumn,
       |  '${pkColumn.escape} AS pcn,
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
       |  '${table.escape}' AS PKTABLE_NAME,
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
    indexInfoBody(
      table = table,
      select = "SELECT null AS un, null AS n, null AS op, null AS cn",
      empty = true)

  def indexInfo(
    table: String,
    indexInfoSeq: Seq[IndexInfo]) =
    indexInfoBody(
      table = table,
      select = selectInfoSeq(indexInfoSeq),
      empty = false)

  private[this] def indexInfoBody(
    table: String,
    select: String,
    empty: Boolean) =
    s"""
       | SELECT
       |  null AS TABLE_CAT,
       |  null AS TABLE_SCHEM,
       |  '${table.escape}' AS TABLE_NAME,
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
       |  ${if (empty) "LIMIT 0" else ""}""".stripMargin

  private[this] def selectInfoSeq(indexInfoSeq: Seq[IndexInfo]): String =
    (indexInfoSeq map { i =>
      s"SELECT ${1 - i.index} AS un, '${i.name}' AS n, ${i.op} AS op, '${i.cn}' AS cn"
    }).mkString(unionAll)

  def cursorTableType(tableType: String, tablePattern: String): String =
    s"""
       | SELECT
       |  null AS TABLE_CAT,
       |  null AS TABLE_SCHEM,
       |  tbl_name AS TABLE_NAME,
       |  '${tableType.escape}' AS TABLE_TYPE,
       |  'No Comment' AS REMARKS,
       |  null AS TYPE_CAT,
       |  null AS TYPE_SCHEM,
       |  null AS TYPE_NAME,
       |  null AS SELF_REFERENCING_COL_NAME,
       |  null AS REF_GENERATION
       | FROM sqlite_master
       | WHERE tbl_name LIKE '$tablePattern'
       | AND name NOT LIKE 'sqlite_%'
       | AND name NOT LIKE 'android_metadata'
       | AND upper(type) = '${tableType.toUpperCase}'
       | UNION ALL
       | SELECT
       |  null AS TABLE_CAT,
       |  null AS TABLE_SCHEM,
       |  tbl_name AS TABLE_NAME,
       |  '${tableType.escape}' AS TABLE_TYPE,
       |  'No Comment' AS REMARKS,
       |  null AS TYPE_CAT,
       |  null AS TYPE_SCHEM,
       |  null AS TYPE_NAME,
       |  null AS SELF_REFERENCING_COL_NAME,
       |  null AS REF_GENERATION
       | FROM sqlite_temp_master
       | WHERE tbl_name LIKE '$tablePattern'
       | AND name NOT LIKE 'android_metadata'
       | AND upper(type) = '${tableType.toUpperCase}'""".stripMargin

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
       | '${parentCatalog.escape}' AS PKTABLE_CAT,
       | '${parentSchema.escape}' AS PKTABLE_SCHEM,
       | '${parentTable.escape}' AS PKTABLE_NAME,
       | '' AS PKCOLUMN_NAME,
       | '${foreignCatalog.escape}' AS FKTABLE_CAT,
       | '${foreignSchema.escape}' AS FKTABLE_SCHEM,
       | '${foreignTable.escape}' AS FKTABLE_NAME,
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
      | dt AS DATA_TYPE,
      | 0 AS PRECISION,
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
      | 10 AS NUM_PREC_RADIX
      | FROM (
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
      | )
      | ORDER BY TYPE_NAME""".stripMargin

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