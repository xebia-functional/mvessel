package com.fortysevendeg.mvessel.metadata

import java.sql.SQLException

import com.fortysevendeg.mvessel._
import org.specs2.specification.Scope
import com.fortysevendeg.mvessel.util.ResultSetProcessorOps._

trait DatabaseMetaDataSpecification
  extends DatabaseSupport {

  trait DatabaseMetadataScope
    extends Scope {

    val databaseMetaData = new DatabaseMetaData(
      connection.getOrElse(throw new IllegalStateException("Connection is not created")),
      new TestLogWrapper)

  }

}

class DatabaseMetaDataSpec
  extends DatabaseMetaDataSpecification {

  "getAttributes" should {
    "return a valid ResultSet with no elements" in new DatabaseMetadataScope {
      val resultSet = databaseMetaData.getAttributes("", "", "", "")
      resultSet.next() should beFalse
      resultSet.close()
    }
  }

  "getBestRowIdentifier" should {
    "return a valid ResultSet with no elements" in new DatabaseMetadataScope {
      val resultSet = databaseMetaData.getBestRowIdentifier("", "", "", 0, false)
      resultSet.next() should beFalse
      resultSet.close()
    }
  }

  "getCatalogs" should {
    "return a valid ResultSet with no elements" in new DatabaseMetadataScope {
      val resultSet = databaseMetaData.getCatalogs()
      resultSet.next() should beFalse
      resultSet.close()
    }
  }

  "getColumnPrivileges" should {
    "return a valid ResultSet with no elements" in new DatabaseMetadataScope {
      val resultSet = databaseMetaData.getColumnPrivileges("", "", "", "")
      resultSet.next() should beFalse
      resultSet.close()
    }
  }

  "getProcedureColumns" should {
    "return a valid ResultSet with no elements" in new DatabaseMetadataScope {
      val resultSet = databaseMetaData.getProcedureColumns("", "", "", "")
      resultSet.next() should beFalse
      resultSet.close()
    }
  }

  "getProcedures" should {
    "return a valid ResultSet with no elements" in new DatabaseMetadataScope {
      val resultSet = databaseMetaData.getProcedures("", "", "")
      resultSet.next() should beFalse
      resultSet.close()
    }
  }

  "getSuperTables" should {
    "return a valid ResultSet with no elements" in new DatabaseMetadataScope {
      val resultSet = databaseMetaData.getSuperTables("", "", "")
      resultSet.next() should beFalse
      resultSet.close()
    }
  }

  "getSuperTypes" should {
    "return a valid ResultSet with no elements" in new DatabaseMetadataScope {
      val resultSet = databaseMetaData.getSuperTypes("", "", "")
      resultSet.next() should beFalse
      resultSet.close()
    }
  }

  "getUDTs" should {
    "return a valid ResultSet with no elements" in new DatabaseMetadataScope {
      val resultSet = databaseMetaData.getUDTs("", "", "", Array.empty)
      resultSet.next() should beFalse
      resultSet.close()
    }
  }

  "getVersionColumns" should {
    "return a valid ResultSet with no elements" in new DatabaseMetadataScope {
      val resultSet = databaseMetaData.getVersionColumns("", "", "")
      resultSet.next() should beFalse
      resultSet.close()
    }
  }

  "getTablePrivileges" should {
    "return a valid ResultSet with no elements" in new DatabaseMetadataScope {
      val resultSet = databaseMetaData.getTablePrivileges("", "", "")
      resultSet.next() should beFalse
      resultSet.close()
    }
  }

  "getTableTypes" should {
    "return a valid ResultSet with two elements: TYPE and TABLE" in new DatabaseMetadataScope {
      databaseMetaData.getTableTypes().process(_.getString(1)).toSet shouldEqual Set("VIEW", "TABLE")
    }
  }

  "getTypeInfo" should {
    "return a valid ResultSet with five elements: BLOB, INTEGER, NULL, REAL and TEXT" in new DatabaseMetadataScope {
      databaseMetaData.getTypeInfo().process(_.getString(1)).toSet shouldEqual Set("BLOB", "INTEGER", "NULL", "REAL", "TEXT")
    }
  }

  "getExportedKeys" should {

    "return a valid ResultSet with no elements" in new DatabaseMetadataScope with WithEmptyTable {
      val resultSet = databaseMetaData.getExportedKeys("", "", tableName)
      resultSet.next() should beFalse
      resultSet.close()
    }

    "throws a SQLException when specify a non existing table" in
      new DatabaseMetadataScope with WithEmptyTable {
        databaseMetaData.getExportedKeys("", "", nonExistingTableName) must throwA[SQLException]("Table not found")
      }

    "throws a SQLException when specify an invalid table name" in
      new DatabaseMetadataScope with WithEmptyTable {
        databaseMetaData.getExportedKeys("", "", "") must throwA[SQLException]("Invalid table name")
      }

    "throws a SQLException when pass a null in the table name argument" in
      new DatabaseMetadataScope with WithEmptyTable {
        databaseMetaData.getExportedKeys("", "", javaNull) must throwA[SQLException]("Table name can't be null")
      }
  }

  "getImportedKeys" should {

    "return a valid ResultSet with no elements when the table doesn't have foreign keys" in
      new DatabaseMetadataScope with WithEmptyTable {
        val resultSet = databaseMetaData.getImportedKeys("", "", tableName)
        resultSet.next() should beFalse
        resultSet.close()
      }

    "return a valid ResultSet with one element when the table has one foreign key" in
      new DatabaseMetadataScope with WithEmptyTable {
        databaseMetaData.getImportedKeys("", "", tableWithFKName).process { rs =>
          (
            rs.getString("PKTABLE_NAME"),
            rs.getString("PKCOLUMN_NAME"),
            rs.getString("FKTABLE_NAME"),
            rs.getString("FKCOLUMN_NAME"))
        } shouldEqual Seq((tableName, columnId, tableWithFKName, columnForeignKeyId))
      }

    "return a valid ResultSet with no elements when specify a non existing table" in
      new DatabaseMetadataScope with WithEmptyTable {
        val resultSet = databaseMetaData.getImportedKeys("", "", nonExistingTableName)
        resultSet.next() should beFalse
        resultSet.close()
      }

  }

  "getTables" should {

    "return a valid ResultSet with one elements" in new DatabaseMetadataScope with WithEmptyTable {
      databaseMetaData.getTables("", "", tableName, scala.Array.empty).process { rs =>
        rs.getString("TABLE_NAME")
      } shouldEqual Seq(tableName)
    }

    "return a valid ResultSet with no elements when specify a non existent table" in
      new DatabaseMetadataScope with WithEmptyTable {
        val resultSet = databaseMetaData.getTables("", "", nonExistingTableName, scala.Array.empty)
        resultSet.next() should beFalse
        resultSet.close()
      }
  }

  "getColumns" should {

    "return all columns with the right types" in new DatabaseMetadataScope with WithEmptyTable {
      val columns = databaseMetaData.getColumns("", "", tableName, "").process { rs =>
        (rs.getString("COLUMN_NAME"), rs.getInt("DATA_TYPE"))
      }.toSet
      columns shouldEqual Set(
        ("id", java.sql.Types.INTEGER),
        ("name", java.sql.Types.VARCHAR),
        ("name2", java.sql.Types.VARCHAR))
    }

    "return empty ResultSet for a non existing table" in new DatabaseMetadataScope with WithEmptyTable {
      val resultSet = databaseMetaData.getColumns("", "", nonExistingTableName, "")
      resultSet.next() should beFalse
      resultSet.close()
    }

  }

  "getCrossReference" should {

    "return a valid ResultSet with no elements when parent table is defined but no foreign table" in
      new DatabaseMetadataScope with WithEmptyTable {
        val resultSet = databaseMetaData.getCrossReference("", "", tableName, javaNull, javaNull, javaNull)
        resultSet.next() should beFalse
        resultSet.close()
      }

    "throws a SQLException when specify a non existing table when parent table is defined but no foreign table" in
      new DatabaseMetadataScope with WithEmptyTable {
        databaseMetaData.getCrossReference("", "", nonExistingTableName, javaNull, javaNull, javaNull) must throwA[SQLException]("Table not found")
      }

    "throws a SQLException when specify an invalid table name when parent table is defined but no foreign table" in
      new DatabaseMetadataScope with WithEmptyTable {
        databaseMetaData.getCrossReference("", "", "", javaNull, javaNull, javaNull) must throwA[SQLException]("Invalid table name")
      }

    "return a valid ResultSet with no elements when foreign table is defined but no parent table" in
      new DatabaseMetadataScope with WithEmptyTable {
        val resultSet = databaseMetaData.getCrossReference(javaNull, javaNull, javaNull, "", "", tableName)
        resultSet.next() should beFalse
        resultSet.close()
      }

    "return a valid ResultSet with one element when the table has one foreign key but not specify parent table" in
      new DatabaseMetadataScope with WithEmptyTable {
        databaseMetaData.getCrossReference(javaNull, javaNull, javaNull, "", "", tableWithFKName).process { rs =>
          (
            rs.getString("PKTABLE_NAME"),
            rs.getString("PKCOLUMN_NAME"),
            rs.getString("FKTABLE_NAME"),
            rs.getString("FKCOLUMN_NAME"))
        } shouldEqual Seq((tableName, columnId, tableWithFKName, columnForeignKeyId))
      }

    "return a valid ResultSet with no elements when specify a non existing table as foreign table no parent table is defined" in
      new DatabaseMetadataScope with WithEmptyTable {
        val resultSet = databaseMetaData.getCrossReference(javaNull, javaNull, javaNull, "", "", nonExistingTableName)
        resultSet.next() should beFalse
        resultSet.close()
      }

    "return a valid ResultSet with no elements when parent and foreign table is defined" in
      new DatabaseMetadataScope with WithEmptyTable {
        val resultSet = databaseMetaData.getCrossReference("", "", tableName, "", "", tableWithFKName)
        resultSet.next() should beFalse
        resultSet.close()
      }

    "throws a SQLException when specify a non existing parent table and a foreign table" in
      new DatabaseMetadataScope with WithEmptyTable {
        databaseMetaData.getCrossReference("", "", nonExistingTableName, javaNull, javaNull, tableWithFKName) must throwA[SQLException]("Table not found")
      }

  }

  "getIndexInfo" should {

    "return two indexes with the right types" in new DatabaseMetadataScope with WithEmptyTable {
      val indexes = databaseMetaData.getIndexInfo("", "", tableName, false, true).process { rs =>
        (rs.getInt("NON_UNIQUE"), rs.getString("INDEX_NAME"), rs.getString("COLUMN_NAME"))
      }.toSet
      indexes shouldEqual Set(
        (0, uniqueIndex, columnTextNotNull),
        (1, nonUniqueIndex, columnText))
    }

    "return empty ResultSet for a table without indexes" in new DatabaseMetadataScope with WithEmptyTable {
      val resultSet = databaseMetaData.getIndexInfo("", "", tableWithFKName, false, true)
      resultSet.next() should beFalse
      resultSet.close()
    }

    "return empty ResultSet for a non existing table" in new DatabaseMetadataScope with WithEmptyTable {
      val resultSet = databaseMetaData.getIndexInfo("", "", nonExistingTableName, false, true)
      resultSet.next() should beFalse
      resultSet.close()
    }

  }

  "getPrimaryKeys" should {

    "return PK column for a table with one primary key" in new DatabaseMetadataScope with WithEmptyTable {
      val columns = databaseMetaData.getPrimaryKeys("", "", tableName).process { rs =>
        rs.getString("COLUMN_NAME")
      }
      columns shouldEqual Seq("id")
    }

    "return empty ResultSet for a table without primary keys" in new DatabaseMetadataScope with WithEmptyTable {
      val resultSet = databaseMetaData.getPrimaryKeys("", "", tableWithFKName)
      resultSet.next() should beFalse
      resultSet.close()
    }

    "return empty ResultSet for a non existing table" in new DatabaseMetadataScope with WithEmptyTable {
      val resultSet = databaseMetaData.getPrimaryKeys("", "", nonExistingTableName)
      resultSet.next() should beFalse
      resultSet.close()
    }

  }

}
