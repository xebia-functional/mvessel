package com.fortysevendeg.mvessel.data

import java.io.{BufferedReader, InputStream, Reader}
import java.sql.{SQLException, SQLFeatureNotSupportedException}

import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.specification.Scope

import scala.util.Random

trait ClobSpecification
  extends Specification
  with Mockito {

  trait ClobScope
    extends Scope {

    val string = Random.nextString(10)

    val emptyString = ""

    val clob = new Clob(string)

    val emptyClob = new Clob(emptyString)

    val mockClob = mock[Clob]

    def toString(is: InputStream): String =
      scala.io.Source.fromInputStream(is).mkString

    def toString(reader: Reader): String = {
      val buffer = new BufferedReader(reader)
      Stream.continually(buffer.readLine()).takeWhile(Option(_).isDefined).mkString
    }

  }

}

class ClobSpec
  extends ClobSpecification {

  "length" should {

    "returns the string size" in new ClobScope {
      clob.length shouldEqual string.length
    }

    "returns 0 for an empty string" in new ClobScope {
      emptyClob.length shouldEqual 0
    }

  }

  "getAsciiStream" should {

    "returns a valid InputStream with the string content" in new ClobScope {
      toString(clob.getAsciiStream) shouldEqual string
    }

    "returns an empty InputStream when the original string is empty" in new ClobScope {
      toString(emptyClob.getAsciiStream) shouldEqual emptyString
    }

  }

  "getSubString" should {

    "returns the complete string when specify the string dimensions" in new ClobScope {
      clob.getSubString(1, string.length) shouldEqual string
    }

    "returns an array with one element when specify length 1" in new ClobScope {
      clob.getSubString(2, 1) shouldEqual string.substring(1, 2)
    }

    "returns the complete string when the pos plus length is greater than the string length" in new ClobScope {
      clob.getSubString(1, string.length + 1) shouldEqual string
    }

    "throws a SQLException when the pos param is less than 1" in new ClobScope {
      clob.getSubString(0, string.length) must throwA[SQLException]
    }

    "throws a SQLException when the length param is less than 0" in new ClobScope {
      clob.getSubString(1, -1) must throwA[SQLException]
    }

  }

  "getCharacterStream" should {

    "returns a Reader for a valid string" in new ClobScope {
      toString(clob.getCharacterStream) shouldEqual string
    }

    "returns an InputStream when specify the string dimensions" in new ClobScope {
      toString(clob.getCharacterStream(1, string.length)) shouldEqual string
    }

    "returns an string with one element when specify length 1" in new ClobScope {
      toString(clob.getCharacterStream(2, 1)) shouldEqual string.substring(1, 2)
    }

    "returns the complete string when the pos plus length is greater than the string length" in new ClobScope {
      toString(clob.getCharacterStream(1, string.length + 1)) shouldEqual string
    }

    "throws a SQLException when the pos param is less than 1" in new ClobScope {
      clob.getCharacterStream(0, string.length) must throwA[SQLException]
    }

    "throws a SQLException when the length param is less than 0" in new ClobScope {
      clob.getCharacterStream(1, -1) must throwA[SQLException]
    }

  }

  "position" should {

    "throws a SQLFeatureNotSupportedException when passing an string" in new ClobScope {
      clob.position(emptyString, 0) must throwA[SQLFeatureNotSupportedException]
    }

    "throws a SQLFeatureNotSupportedException when passing a blob" in new ClobScope {
      clob.position(mockClob, 0) must throwA[SQLFeatureNotSupportedException]
    }

  }

  "setAsciiStream" should {

    "throws a SQLFeatureNotSupportedException" in new ClobScope {
      clob.setAsciiStream(0) must throwA[SQLFeatureNotSupportedException]
    }

  }

  "setCharacterStream" should {

    "throws a SQLFeatureNotSupportedException" in new ClobScope {
      clob.setCharacterStream(0) must throwA[SQLFeatureNotSupportedException]
    }

  }

  "setString" should {

    "throws a SQLFeatureNotSupportedException" in new ClobScope {
      clob.setString(0, string) must throwA[SQLFeatureNotSupportedException]
    }

    "throws a SQLFeatureNotSupportedException when passing a offset and length" in new ClobScope {
      clob.setString(0, string, 0, 1) must throwA[SQLFeatureNotSupportedException]
    }

  }

  "truncate" should {

    "throws a SQLFeatureNotSupportedException" in new ClobScope {
      {clob.truncate(0);true} must throwA[SQLFeatureNotSupportedException]
    }

  }

  "free" should {

    "throws a SQLFeatureNotSupportedException" in new ClobScope {
      {clob.free();true} must throwA[SQLFeatureNotSupportedException]
    }

  }

}
