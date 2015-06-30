package com.fortysevendeg.android.sqlite

import java.sql.Connection
import java.util.Properties

import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.specification.Scope

import scala.util.Random

trait SQLDroidDataSourceSpecification
  extends Specification
  with Mockito {

  trait SQLDroidDataSourceScope
    extends Scope {

    val driver = mock[SQLDroidDriver]

    val connection = mock[Connection]

    val properties = new Properties

    val dbPath = "/path/dbname.db"

    val sqlDroid = new SQLDroidDataSource(driver, properties, dbPath, new TestLogWrapper)

  }

}

class SQLDroidDataSourceSpec
  extends SQLDroidDataSourceSpecification {

  "getConnection" should {

    "call to connect method of driver with right parameters" in
      new SQLDroidDataSourceScope {

        driver.connect(contain(dbPath), beTheSameAs(properties)).returns(connection)

        sqlDroid.getConnection shouldEqual connection
      }

    "call to connect method of driver with right parameters when passing username and password" in
      new SQLDroidDataSourceScope {

        driver.connect(contain(dbPath), beTheSameAs(properties)).returns(connection)

        sqlDroid.getConnection(Random.nextString(10), Random.nextString(10)) shouldEqual connection
      }

  }

  "getParentLogger" should {

    "returns a java null" in new SQLDroidDataSourceScope {
      sqlDroid.getParentLogger shouldEqual javaNull
    }

  }

  "getLoginTimeout" should {

    "returns 0" in new SQLDroidDataSourceScope {
      sqlDroid.getLoginTimeout shouldEqual 0
    }

  }

  "isWrapperFor" should {

    "throws an UnsupportedOperationException" in new SQLDroidDataSourceScope {
      sqlDroid.isWrapperFor(classOf[SQLDroidDataSource]) must throwA[UnsupportedOperationException]
    }

  }

  "unwrap" should {

    "throws an UnsupportedOperationException" in new SQLDroidDataSourceScope {
      sqlDroid.unwrap(classOf[SQLDroidDataSource]) must throwA[UnsupportedOperationException]
    }

  }

}
