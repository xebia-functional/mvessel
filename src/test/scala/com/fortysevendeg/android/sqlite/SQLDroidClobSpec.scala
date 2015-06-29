package com.fortysevendeg.android.sqlite

import java.io.{BufferedReader, Reader, InputStream}
import java.sql.{Clob, SQLFeatureNotSupportedException, Blob, SQLException}

import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.specification.Scope

import scala.util.Random

trait SQLDroidClobSpecification
  extends Specification
  with Mockito {

  trait SQLDroidClobScope
    extends Scope {

    val string = Random.nextString(10)

    val emptyString = ""

    val sqlDroid = new SQLDroidClob(string)

    val emptySqlDroid = new SQLDroidClob(emptyString)

    val mockClob = mock[Clob]

    def toString(is: InputStream): String =
      scala.io.Source.fromInputStream(is).mkString

    def toString(reader: Reader): String = {
      val buffer = new BufferedReader(reader)
      Stream.continually(buffer.readLine()).takeWhile(_ != null).mkString
    }

  }

}

class SQLDroidClobSpec
  extends SQLDroidClobSpecification {

  "length" should {

    "returns the string size" in new SQLDroidClobScope {
      sqlDroid.length shouldEqual string.length
    }

    "returns 0 for an empty string" in new SQLDroidClobScope {
      emptySqlDroid.length shouldEqual 0
    }

  }

  "getAsciiStream" should {

    "returns a valid InputStream with the string content" in new SQLDroidClobScope {
      toString(sqlDroid.getAsciiStream) shouldEqual string
    }

    "returns an empty InputStream when the original string is empty" in new SQLDroidClobScope {
      toString(emptySqlDroid.getAsciiStream) shouldEqual emptyString
    }

  }

  "getSubString" should {

    "returns the complete string when specify the string dimensions" in new SQLDroidClobScope {
      sqlDroid.getSubString(1, string.length) shouldEqual string
    }

    "returns an array with one element when specify length 1" in new SQLDroidClobScope {
      sqlDroid.getSubString(2, 1) shouldEqual string.substring(1, 2)
    }

    "returns the complete string when the pos plus length is greater than the string length" in new SQLDroidClobScope {
      sqlDroid.getSubString(1, string.length + 1) shouldEqual string
    }

    "throws a SQLException when the pos param is less than 1" in new SQLDroidClobScope {
      sqlDroid.getSubString(0, string.length) must throwA[SQLException]
    }

    "throws a SQLException when the length param is less than 0" in new SQLDroidClobScope {
      sqlDroid.getSubString(1, -1) must throwA[SQLException]
    }

  }

  "getCharacterStream" should {

    "returns a Reader for a valid string" in new SQLDroidClobScope {
      toString(sqlDroid.getCharacterStream) shouldEqual string
    }

    "returns an InputStream when specify the string dimensions" in new SQLDroidClobScope {
      toString(sqlDroid.getCharacterStream(1, string.length)) shouldEqual string
    }

    "returns an string with one element when specify length 1" in new SQLDroidClobScope {
      toString(sqlDroid.getCharacterStream(2, 1)) shouldEqual string.substring(1, 2)
    }

    "returns the complete string when the pos plus length is greater than the string length" in new SQLDroidClobScope {
      toString(sqlDroid.getCharacterStream(1, string.length + 1)) shouldEqual string
    }

    "throws a SQLException when the pos param is less than 1" in new SQLDroidClobScope {
      sqlDroid.getCharacterStream(0, string.length) must throwA[SQLException]
    }

    "throws a SQLException when the length param is less than 0" in new SQLDroidClobScope {
      sqlDroid.getCharacterStream(1, -1) must throwA[SQLException]
    }

  }

  "position" should {

    "throws a SQLFeatureNotSupportedException when passing an string" in new SQLDroidClobScope {
      sqlDroid.position(emptyString, 0) must throwA[SQLFeatureNotSupportedException]
    }

    "throws a SQLFeatureNotSupportedException when passing a blob" in new SQLDroidClobScope {
      sqlDroid.position(mockClob, 0) must throwA[SQLFeatureNotSupportedException]
    }

  }

  "setAsciiStream" should {

    "throws a SQLFeatureNotSupportedException" in new SQLDroidClobScope {
      sqlDroid.setAsciiStream(0) must throwA[SQLFeatureNotSupportedException]
    }

  }

  "setCharacterStream" should {

    "throws a SQLFeatureNotSupportedException" in new SQLDroidClobScope {
      sqlDroid.setCharacterStream(0) must throwA[SQLFeatureNotSupportedException]
    }

  }

  "setString" should {

    "throws a SQLFeatureNotSupportedException" in new SQLDroidClobScope {
      sqlDroid.setString(0, string) must throwA[SQLFeatureNotSupportedException]
    }

    "throws a SQLFeatureNotSupportedException when passing a offset and length" in new SQLDroidClobScope {
      sqlDroid.setString(0, string, 0, 1) must throwA[SQLFeatureNotSupportedException]
    }

  }

  "truncate" should {

    "throws a SQLFeatureNotSupportedException" in new SQLDroidClobScope {
      {sqlDroid.truncate(0);true} must throwA[SQLFeatureNotSupportedException]
    }

  }

  "free" should {

    "throws a SQLFeatureNotSupportedException" in new SQLDroidClobScope {
      {sqlDroid.free();true} must throwA[SQLFeatureNotSupportedException]
    }

  }

}
