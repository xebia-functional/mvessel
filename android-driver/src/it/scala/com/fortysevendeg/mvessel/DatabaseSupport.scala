/*
 * The MIT License (MIT)
 *
 * Copyright (C) 2012 47 Degrees, LLC http://47deg.com hello@47deg.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 */

package com.fortysevendeg.mvessel

import java.sql.{Connection => SQLConnection, DriverManager}

import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.specification.{AfterEach, BeforeEach}

trait DatabaseSupport
  extends Specification
  with BeforeEach
  with AfterEach
  with Mockito {

  sequential

  var connection: Option[SQLConnection] = None

  def before = connection = {
    // register the driver
    Class.forName("org.sqlite.JDBC")
    Some(DriverManager.getConnection("jdbc:sqlite::memory:"))
  }

  def after = {
    connection foreach (_.close())
    connection = None
  }

  trait WithEmptyTable {

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