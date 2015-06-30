package com.fortysevendeg.android.sqlite.data

import java.io.InputStream
import java.sql.{Blob, SQLException, SQLFeatureNotSupportedException}

import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.specification.Scope

trait SQLDroidBlobSpecification
  extends Specification
  with Mockito {

  trait SQLDroidBlobScope
    extends Scope {

    val array = Array[Byte](1, 2, 3)

    val emptyArray = Array.empty[Byte]

    val sqlDroid = new SQLDroidBlob(array)

    val emptySqlDroid = new SQLDroidBlob(emptyArray)

    val mockBlob = mock[Blob]

    def toByteArray(is: InputStream): Array[Byte] =
      Stream.continually(is.read).takeWhile(-1 !=).map(_.toByte).toArray

  }

}

class SQLDroidBlobSpec
  extends SQLDroidBlobSpecification {

  "getBytes" should {

    "returns the complete array when specify the array dimensions" in new SQLDroidBlobScope {
      sqlDroid.getBytes(1, array.length) shouldEqual array
    }

    "returns an array with one element when specify length 1" in new SQLDroidBlobScope {
      sqlDroid.getBytes(2, 1) shouldEqual Array[Byte](array(1))
    }

    "returns the complete array when the pos plus length is greater than the array length" in new SQLDroidBlobScope {
      sqlDroid.getBytes(1, array.length + 1) shouldEqual array
    }

    "throws a SQLException when the pos param is less than 1" in new SQLDroidBlobScope {
      sqlDroid.getBytes(0, array.length) must throwA[SQLException]
    }

    "throws a SQLException when the length param is less than 0" in new SQLDroidBlobScope {
      sqlDroid.getBytes(1, -1) must throwA[SQLException]
    }

  }

  "getBinaryStream" should {

    "returns an InputStream for a valid byte Array" in new SQLDroidBlobScope {
      toByteArray(sqlDroid.getBinaryStream) shouldEqual array
    }

    "returns an InputStream when specify the array dimensions" in new SQLDroidBlobScope {
      toByteArray(sqlDroid.getBinaryStream(1, array.length)) shouldEqual array
    }

    "returns an array with one element when specify length 1" in new SQLDroidBlobScope {
      toByteArray(sqlDroid.getBinaryStream(2, 1)) shouldEqual Array[Byte](array(1))
    }

    "returns the complete array when the pos plus length is greater than the array length" in new SQLDroidBlobScope {
      toByteArray(sqlDroid.getBinaryStream(1, array.length + 1)) shouldEqual array
    }

    "throws a SQLException when the pos param is less than 1" in new SQLDroidBlobScope {
      sqlDroid.getBinaryStream(0, array.length) must throwA[SQLException]
    }

    "throws a SQLException when the length param is less than 0" in new SQLDroidBlobScope {
      sqlDroid.getBinaryStream(1, -1) must throwA[SQLException]
    }

  }

  "length" should {

    "returns the array size" in new SQLDroidBlobScope {
      sqlDroid.length shouldEqual array.length
    }

    "returns 0 for an empty array" in new SQLDroidBlobScope {
      emptySqlDroid.length shouldEqual 0
    }

  }

  "position" should {

    "throws a SQLFeatureNotSupportedException when passing an array" in new SQLDroidBlobScope {
      sqlDroid.position(emptyArray, 0) must throwA[SQLFeatureNotSupportedException]
    }

    "throws a SQLFeatureNotSupportedException when passing a blob" in new SQLDroidBlobScope {
      sqlDroid.position(mockBlob, 0) must throwA[SQLFeatureNotSupportedException]
    }

  }

  "setBinaryStream" should {

    "throws a SQLFeatureNotSupportedException" in new SQLDroidBlobScope {
      sqlDroid.setBinaryStream(0) must throwA[SQLFeatureNotSupportedException]
    }

  }

  "setBytes" should {

    "throws a SQLFeatureNotSupportedException" in new SQLDroidBlobScope {
      sqlDroid.setBytes(0, emptyArray) must throwA[SQLFeatureNotSupportedException]
    }

    "throws a SQLFeatureNotSupportedException when passing a offset and length" in new SQLDroidBlobScope {
      sqlDroid.setBytes(0, emptyArray, 0, 1) must throwA[SQLFeatureNotSupportedException]
    }

  }

  "truncate" should {

    "throws a SQLFeatureNotSupportedException" in new SQLDroidBlobScope {
      {sqlDroid.truncate(0);true} must throwA[SQLFeatureNotSupportedException]
    }

  }

  "free" should {

    "throws a SQLFeatureNotSupportedException" in new SQLDroidBlobScope {
      {sqlDroid.free();true} must throwA[SQLFeatureNotSupportedException]
    }

  }

}
