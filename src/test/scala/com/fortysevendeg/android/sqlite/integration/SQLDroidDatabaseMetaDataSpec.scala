package com.fortysevendeg.android.sqlite.integration

import java.sql.{SQLException, Connection, DriverManager}

import com.fortysevendeg.android.sqlite.metadata.SQLDroidDatabaseMetaData
import com.fortysevendeg.android.sqlite.util.CursorUtils._
import com.fortysevendeg.android.sqlite.javaNull
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.specification.{AfterEach, BeforeEach, Scope}

trait SQLDroidDatabaseMetaDataSpecification
  extends Specification
  with BeforeEach
  with AfterEach
  with Mockito {

  sequential

  var connection: Option[Connection] = None

  def before = connection = Some(DriverManager.getConnection("jdbc:sqlite::memory:"))

  def after = {
    connection foreach (_.close())
    connection = None
  }

  trait WithDatabaseConnection
    extends Scope {

    val sqlDroid = new SQLDroidDatabaseMetaData(connection.getOrElse(throw new IllegalStateException("Connection is not created")))
  }

  trait WithEmptyTable {
    self: WithDatabaseConnection =>

    val tableName = "sample"

    val tableWithFKName = "sample2"

    val nonExistingTableName = "nonExisting"

    val columnId = "id"

    val columnForeignKeyId = "idfk"

    val columnText = "name"

    val columnTextNotNull = "name2"

    val uniqueIndex = "index_u"

    val nonUniqueIndex = "index_nu"

    connection map { st =>
      st.createStatement.executeUpdate(s"CREATE TABLE $tableName($columnId INTEGER PRIMARY KEY, $columnText TEXT, $columnTextNotNull TEXT NOT NULL)")
      st.createStatement.executeUpdate(s"CREATE UNIQUE INDEX $uniqueIndex ON $tableName($columnTextNotNull)")
      st.createStatement.executeUpdate(s"CREATE UNIQUE INDEX $nonUniqueIndex ON $tableName($columnText)")
      st.createStatement.executeUpdate(
        s"""
         |CREATE TABLE $tableWithFKName(
         |$columnId INTEGER,
         |$columnText TEXT,
         |$columnForeignKeyId INTEGER,
         |FOREIGN KEY($columnForeignKeyId) REFERENCES $tableName($columnId))
         |""".stripMargin)
    }

  }

}

class SQLDroidDatabaseMetaDataSpec
  extends SQLDroidDatabaseMetaDataSpecification {

  "getAttributes" should {
    "return a valid ResultSet with no elements" in new WithDatabaseConnection {
      val resultSet = sqlDroid.getAttributes("", "", "", "")
      resultSet.next() should beFalse
      resultSet.close()
    }
  }

  "getBestRowIdentifier" should {
    "return a valid ResultSet with no elements" in new WithDatabaseConnection {
      val resultSet = sqlDroid.getBestRowIdentifier("", "", "", 0, false)
      resultSet.next() should beFalse
      resultSet.close()
    }
  }

  "getCatalogs" should {
    "return a valid ResultSet with no elements" in new WithDatabaseConnection {
      val resultSet = sqlDroid.getCatalogs()
      resultSet.next() should beFalse
      resultSet.close()
    }
  }

  "getColumnPrivileges" should {
    "return a valid ResultSet with no elements" in new WithDatabaseConnection {
      val resultSet = sqlDroid.getColumnPrivileges("", "", "", "")
      resultSet.next() should beFalse
      resultSet.close()
    }
  }

  "getProcedureColumns" should {
    "return a valid ResultSet with no elements" in new WithDatabaseConnection {
      val resultSet = sqlDroid.getProcedureColumns("", "", "", "")
      resultSet.next() should beFalse
      resultSet.close()
    }
  }

  "getProcedures" should {
    "return a valid ResultSet with no elements" in new WithDatabaseConnection {
      val resultSet = sqlDroid.getProcedures("", "", "")
      resultSet.next() should beFalse
      resultSet.close()
    }
  }

  "getSuperTables" should {
    "return a valid ResultSet with no elements" in new WithDatabaseConnection {
      val resultSet = sqlDroid.getSuperTables("", "", "")
      resultSet.next() should beFalse
      resultSet.close()
    }
  }

  "getSuperTypes" should {
    "return a valid ResultSet with no elements" in new WithDatabaseConnection {
      val resultSet = sqlDroid.getSuperTypes("", "", "")
      resultSet.next() should beFalse
      resultSet.close()
    }
  }

  "getUDTs" should {
    "return a valid ResultSet with no elements" in new WithDatabaseConnection {
      val resultSet = sqlDroid.getUDTs("", "", "", Array.empty)
      resultSet.next() should beFalse
      resultSet.close()
    }
  }

  "getVersionColumns" should {
    "return a valid ResultSet with no elements" in new WithDatabaseConnection {
      val resultSet = sqlDroid.getVersionColumns("", "", "")
      resultSet.next() should beFalse
      resultSet.close()
    }
  }

  "getTablePrivileges" should {
    "return a valid ResultSet with no elements" in new WithDatabaseConnection {
      val resultSet = sqlDroid.getTablePrivileges("", "", "")
      resultSet.next() should beFalse
      resultSet.close()
    }
  }

  "getTableTypes" should {
    "return a valid ResultSet with two elements: TYPE and TABLE" in new WithDatabaseConnection {
      sqlDroid.getTableTypes().process(_.getString(1)).toSet shouldEqual Set("VIEW", "TABLE")
    }
  }

  "getTypeInfo" should {
    "return a valid ResultSet with five elements: BLOB, INTEGER, NULL, REAL and TEXT" in new WithDatabaseConnection {
      sqlDroid.getTypeInfo().process(_.getString(1)).toSet shouldEqual Set("BLOB", "INTEGER", "NULL", "REAL", "TEXT")
    }
  }

  "getExportedKeys" should {

    "return a valid ResultSet with no elements" in new WithDatabaseConnection with WithEmptyTable {
      val resultSet = sqlDroid.getExportedKeys("", "", tableName)
      resultSet.next() should beFalse
      resultSet.close()
    }

    "throws a SQLException when specify a non existing table" in
      new WithDatabaseConnection with WithEmptyTable {
        sqlDroid.getExportedKeys("", "", nonExistingTableName) must throwA[SQLException]("Table not found")
      }

    "throws a SQLException when specify an invalid table name" in
      new WithDatabaseConnection with WithEmptyTable {
        sqlDroid.getExportedKeys("", "", "") must throwA[SQLException]("Invalid table name")
      }

    "throws a SQLException when pass a null in the table name argument" in
      new WithDatabaseConnection with WithEmptyTable {
        sqlDroid.getExportedKeys("", "", javaNull) must throwA[SQLException]("Table name can't be null")
      }
  }

  "getImportedKeys" should {

    "return a valid ResultSet with no elements when the table doesn't have foreign keys" in
      new WithDatabaseConnection with WithEmptyTable {
        val resultSet = sqlDroid.getImportedKeys("", "", tableName)
        resultSet.next() should beFalse
        resultSet.close()
      }

    "return a valid ResultSet with one element when the table has one foreign key" in
      new WithDatabaseConnection with WithEmptyTable {
        sqlDroid.getImportedKeys("", "", tableWithFKName).process { rs =>
          (
            rs.getString("PKTABLE_NAME"),
            rs.getString("PKCOLUMN_NAME"),
            rs.getString("FKTABLE_NAME"),
            rs.getString("FKCOLUMN_NAME"))
        } shouldEqual Seq((tableName, columnId, tableWithFKName, columnForeignKeyId))
      }

    "return a valid ResultSet with no elements when specify a non existing table" in
      new WithDatabaseConnection with WithEmptyTable {
        val resultSet = sqlDroid.getImportedKeys("", "", nonExistingTableName)
        resultSet.next() should beFalse
        resultSet.close()
      }

  }

  "getTables" should {

    "return a valid ResultSet with one elements" in new WithDatabaseConnection with WithEmptyTable {
      sqlDroid.getTables("", "", tableName, scala.Array.empty).process { rs =>
        rs.getString("TABLE_NAME")
      } shouldEqual Seq(tableName)
    }

    "return a valid ResultSet with no elements when specify a non existent table" in
      new WithDatabaseConnection with WithEmptyTable {
        val resultSet = sqlDroid.getTables("", "", nonExistingTableName, scala.Array.empty)
        resultSet.next() should beFalse
        resultSet.close()
      }
  }

  "getColumns" should {

    "return all columns with the right types" in new WithDatabaseConnection with WithEmptyTable {
      val columns = sqlDroid.getColumns("", "", tableName, "").process { rs =>
        (rs.getString("COLUMN_NAME"), rs.getInt("DATA_TYPE"))
      }.toSet
      columns shouldEqual Set(
        ("id", java.sql.Types.INTEGER),
        ("name", java.sql.Types.VARCHAR),
        ("name2", java.sql.Types.VARCHAR))
    }

    "return empty ResultSet for a non existing table" in new WithDatabaseConnection with WithEmptyTable {
      val resultSet = sqlDroid.getColumns("", "", nonExistingTableName, "")
      resultSet.next() should beFalse
      resultSet.close()
    }

  }

  "getCrossReference" should {

    "return a valid ResultSet with no elements when parent table is defined but no foreign table" in
      new WithDatabaseConnection with WithEmptyTable {
        val resultSet = sqlDroid.getCrossReference("", "", tableName, javaNull, javaNull, javaNull)
        resultSet.next() should beFalse
        resultSet.close()
      }

    "throws a SQLException when specify a non existing table when parent table is defined but no foreign table" in
      new WithDatabaseConnection with WithEmptyTable {
        sqlDroid.getCrossReference("", "", nonExistingTableName, javaNull, javaNull, javaNull) must throwA[SQLException]("Table not found")
      }

    "throws a SQLException when specify an invalid table name when parent table is defined but no foreign table" in
      new WithDatabaseConnection with WithEmptyTable {
        sqlDroid.getCrossReference("", "", "", javaNull, javaNull, javaNull) must throwA[SQLException]("Invalid table name")
      }

    "return a valid ResultSet with no elements when foreign table is defined but no parent table" in
      new WithDatabaseConnection with WithEmptyTable {
        val resultSet = sqlDroid.getCrossReference(javaNull, javaNull, javaNull, "", "", tableName)
        resultSet.next() should beFalse
        resultSet.close()
      }

    "return a valid ResultSet with one element when the table has one foreign key but not specify parent table" in
      new WithDatabaseConnection with WithEmptyTable {
        sqlDroid.getCrossReference(javaNull, javaNull, javaNull, "", "", tableWithFKName).process { rs =>
          (
            rs.getString("PKTABLE_NAME"),
            rs.getString("PKCOLUMN_NAME"),
            rs.getString("FKTABLE_NAME"),
            rs.getString("FKCOLUMN_NAME"))
        } shouldEqual Seq((tableName, columnId, tableWithFKName, columnForeignKeyId))
      }

    "return a valid ResultSet with no elements when specify a non existing table as foreign table no parent table is defined" in
      new WithDatabaseConnection with WithEmptyTable {
        val resultSet = sqlDroid.getCrossReference(javaNull, javaNull, javaNull, "", "", nonExistingTableName)
        resultSet.next() should beFalse
        resultSet.close()
      }

    "return a valid ResultSet with no elements when parent and foreign table is defined" in
      new WithDatabaseConnection with WithEmptyTable {
        val resultSet = sqlDroid.getCrossReference("", "", tableName, "", "", tableWithFKName)
        resultSet.next() should beFalse
        resultSet.close()
      }

    "throws a SQLException when specify a non existing parent table and a foreign table" in
      new WithDatabaseConnection with WithEmptyTable {
        sqlDroid.getCrossReference("", "", nonExistingTableName, javaNull, javaNull, tableWithFKName) must throwA[SQLException]("Table not found")
      }

  }

  "getIndexInfo" should {

    "return two indexes with the right types" in new WithDatabaseConnection with WithEmptyTable {
      val indexes = sqlDroid.getIndexInfo("", "", tableName, false, true).process { rs =>
        (rs.getInt("NON_UNIQUE"), rs.getString("INDEX_NAME"), rs.getString("COLUMN_NAME"))
      }.toSet
      indexes shouldEqual Set(
        (0, uniqueIndex, columnTextNotNull),
        (1, nonUniqueIndex, columnText))
    }

    "return empty ResultSet for a table without indexes" in new WithDatabaseConnection with WithEmptyTable {
      val resultSet = sqlDroid.getIndexInfo("", "", tableWithFKName, false, true)
      resultSet.next() should beFalse
      resultSet.close()
    }

    "return empty ResultSet for a non existing table" in new WithDatabaseConnection with WithEmptyTable {
      val resultSet = sqlDroid.getIndexInfo("", "", nonExistingTableName, false, true)
      resultSet.next() should beFalse
      resultSet.close()
    }

  }

  "getPrimaryKeys" should {

    "return PK column for a table with one primary key" in new WithDatabaseConnection with WithEmptyTable {
      val columns = sqlDroid.getPrimaryKeys("", "", tableName).process { rs =>
        rs.getString("COLUMN_NAME")
      }
      columns shouldEqual Seq("id")
    }

    "return empty ResultSet for a table without primary keys" in new WithDatabaseConnection with WithEmptyTable {
      val resultSet = sqlDroid.getPrimaryKeys("", "", tableWithFKName)
      resultSet.next() should beFalse
      resultSet.close()
    }

    "return empty ResultSet for a non existing table" in new WithDatabaseConnection with WithEmptyTable {
      val resultSet = sqlDroid.getPrimaryKeys("", "", nonExistingTableName)
      resultSet.next() should beFalse
      resultSet.close()
    }

  }

}
