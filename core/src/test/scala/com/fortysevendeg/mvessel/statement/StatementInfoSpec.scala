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

package com.fortysevendeg.mvessel.statement

import org.specs2.mutable.Specification
import org.specs2.specification.Scope

trait StatementInfoSpecification
  extends Specification {

  val selectSql = "SeLeCt * FROM table;"

  val multiLineSelectSql =
    """SeLeCt * FROM table
       WHERE name = ''
       AND age = 10;
    """

  val limit = 10

  val selectWithLimitSql = s"SeLeCt * FROM table LIMIT $limit;"

  val insertSql = "InSeRt INTO table VALUES(1, 'name', 10);"

  val insertColumnsSql = "InSeRt INTO table(id, name, age) VALUES(1, '', 10);"

  val updateSql = "UpDaTe table SET name = '', age = 10 WHERE id = 1;"

  val deleteSql = "DeLeTe FROM table WHERE id = 1;"

  val multiLineInsertSql =
    """InSeRt INTO table
       VALUES(1, 'name', 10);
    """

  val multiLineUpdateSql =
    """UpDaTe table
       SET name = '', age = 10
       WHERE id = 1;
    """

  val multiLineDeleteSql =
    """DeLeTe FROM table
       WHERE id = 1;
    """

  trait StatementInfoScope
    extends Scope {

    val statementInfo = new StatementInfo {}

  }

}

class StatementInfoSpec
  extends StatementInfoSpecification {

  "isSelect" should {

    "return true for a select statement" in new StatementInfoScope {
      statementInfo.isSelect(selectSql) must beTrue
    }

    "return true for a select statement in lower case" in new StatementInfoScope {
      statementInfo.isSelect(selectSql.toLowerCase) must beTrue
    }

    "return true for a select statement in upper case" in new StatementInfoScope {
      statementInfo.isSelect(selectSql.toUpperCase) must beTrue
    }

    "return true for a multi-line select statement" in new StatementInfoScope {
      statementInfo.isSelect(multiLineSelectSql) must beTrue
    }

    "return false for a insert statement" in new StatementInfoScope {
      statementInfo.isSelect(insertSql) must beFalse
    }

    "return false for a update statement" in new StatementInfoScope {
      statementInfo.isSelect(updateSql) must beFalse
    }

    "return false for a delete statement" in new StatementInfoScope {
      statementInfo.isSelect(deleteSql) must beFalse
    }

  }

  "readLimit" should {

    "return the right value for a statement with a LIMIT clause" in new StatementInfoScope {
      statementInfo.readLimit(selectWithLimitSql) must beSome[Int].which(_ == limit)
    }

    "return None for a statement without a LIMIT clause" in new StatementInfoScope {
      statementInfo.readLimit(selectSql) must beNone
    }

  }

  "toLimitedSql" should {

    "return the same statement for a statement with a LIMIT clause" in new StatementInfoScope {
      statementInfo.toLimitedSql(selectWithLimitSql, 0) shouldEqual selectWithLimitSql
    }

    "return a statement with the LIMIT clause" in new StatementInfoScope {
      statementInfo.toLimitedSql(selectSql, limit) shouldEqual selectWithLimitSql
    }

  }

  "isChange" should {

    "return true for a insert statement" in new StatementInfoScope {
      statementInfo.isChange(insertSql) must beTrue
    }

    "return true for a insert statement with columns specification" in new StatementInfoScope {
      statementInfo.isChange(insertColumnsSql) must beTrue
    }

    "return true for a insert statement in lower case" in new StatementInfoScope {
      statementInfo.isChange(insertSql.toLowerCase) must beTrue
    }

    "return true for a insert statement in upper case" in new StatementInfoScope {
      statementInfo.isChange(insertSql.toUpperCase) must beTrue
    }

    "return true for a multi-line insert statement" in new StatementInfoScope {
      statementInfo.isChange(multiLineInsertSql) must beTrue
    }

    "return true for a update statement" in new StatementInfoScope {
      statementInfo.isChange(updateSql) must beTrue
    }

    "return true for a update statement in lower case" in new StatementInfoScope {
      statementInfo.isChange(updateSql.toLowerCase) must beTrue
    }

    "return true for a update statement in upper case" in new StatementInfoScope {
      statementInfo.isChange(updateSql.toUpperCase) must beTrue
    }

    "return true for a multi-line update statement" in new StatementInfoScope {
      statementInfo.isChange(multiLineUpdateSql) must beTrue
    }

    "return true for a delete statement" in new StatementInfoScope {
      statementInfo.isChange(deleteSql) must beTrue
    }

    "return true for a delete statement in lower case" in new StatementInfoScope {
      statementInfo.isChange(deleteSql.toLowerCase) must beTrue
    }

    "return true for a delete statement in upper case" in new StatementInfoScope {
      statementInfo.isChange(deleteSql.toUpperCase) must beTrue
    }

    "return true for a multi-line delete statement" in new StatementInfoScope {
      statementInfo.isChange(multiLineDeleteSql) must beTrue
    }

    "return false for a select statement" in new StatementInfoScope {
      statementInfo.isChange(selectSql) must beFalse
    }

  }

}
