package com.fortysevendeg.android.sqlite.metadata

import java.sql.{Connection, ResultSet, SQLException, Statement}

class PrimaryKey(val name: Option[String], val columns: Seq[String])

object PrimaryKey {

  val namedPattern = """(?i).* constraint +(.*?) +primary +key *\((.*?)\).*""".r

  val unnamedPattern = """(?i).* primary +key *\((.*?,+.*?)\).*""".r

  def apply(connection: Connection, table: String) = {
    val statement = connection.createStatement()
    val pk = findInSqliteMaster(statement, table) getOrElse findInPragma(statement, table)
    statement.close()
    pk
  }

  private[this] def findInSqliteMaster(statement: Statement, table: String): Option[PrimaryKey] = {
    val sqlResult = sqliteMaster(statement, table)
    findNamed(sqlResult) orElse findUnnamed(sqlResult)
  }

  private[this] def sqliteMaster(statement: Statement, table: String): String =
    Option(table) match {
      case Some(t) if !t.isEmpty =>
        val rs = statement.executeQuery(s"SELECT sql FROM sqlite_master WHERE lower(name) = lower('${escape(t)}') AND type = 'table'")
        val sql = if (rs.next()) Some(rs.getString(1)) else None
        rs.close()
        sql getOrElse (throw new SQLException(s"Table not found: '$t'"))
      case None =>
        throw new SQLException(s"Table name can't be null")
      case _ =>
        throw new SQLException(s"Invalid table name: '$table'")
    }

  private[this] def findNamed(sql: String): Option[PrimaryKey] =
    namedPattern.findFirstMatchIn(sql) map { m =>
      new PrimaryKey(Some(s"'${escape(m.group(1)).toLowerCase}'"), m.group(2).split(","))
    }

  private[this] def findUnnamed(sql: String): Option[PrimaryKey] =
    unnamedPattern.findFirstMatchIn(sql) map (m => new PrimaryKey(None, m.group(1).split(",")))

  private[this] def findInPragma(statement: Statement, table: String): PrimaryKey = {
    val rs = statement.executeQuery(s"pragma table_info('${escape(table)}');")
    val columns = readPragmaResultSet(rs)
    rs.close()
    new PrimaryKey(None, columns)
  }

  private[this] def readPragmaResultSet(resultSet: ResultSet): Seq[String] =
    resultSet.next() match {
      case true =>
        if (resultSet.getBoolean(6)) Seq(resultSet.getString(2))
        else readPragmaResultSet(resultSet)
      case false =>
        Seq.empty
    }

  private[this] def escape(string: String): String = string.replace("'", "''")
  
}