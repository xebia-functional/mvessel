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

package com.fortysevendeg.mvessel.data

import java.io.InputStream
import java.sql.{SQLException, SQLFeatureNotSupportedException}

import com.fortysevendeg.mvessel.javaNull
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.specification.Scope

trait BlobSpecification
  extends Specification
  with Mockito {

  trait BlobScope
    extends Scope {

    val array = Array[Byte](1, 2, 3)

    val emptyArray = Array.empty[Byte]

    val blob = new Blob(array)

    val emptyBlob = new Blob(emptyArray)

    val nullBlob = new Blob(javaNull)

    val mockBlob = mock[Blob]

    def toByteArray(is: InputStream): Array[Byte] =
      Stream.continually(is.read).takeWhile(_ != -1).map(_.toByte).toArray

  }

}

class BlobSpec
  extends BlobSpecification {

  "getBytes" should {

    "returns the complete array when specify the array dimensions" in new BlobScope {
      blob.getBytes(1, array.length) shouldEqual array
    }

    "returns an array with one element when specify length 1" in new BlobScope {
      blob.getBytes(2, 1) shouldEqual Array[Byte](array(1))
    }

    "returns the complete array when the pos plus length is greater than the array length" in new BlobScope {
      blob.getBytes(1, array.length + 1) shouldEqual array
    }

    "throws a SQLException when the pos param is less than 1" in new BlobScope {
      blob.getBytes(0, array.length) must throwA[SQLException]
    }

    "throws a SQLException when the length param is less than 0" in new BlobScope {
      blob.getBytes(1, -1) must throwA[SQLException]
    }

  }

  "getBinaryStream" should {

    "returns an InputStream for a valid byte Array" in new BlobScope {
      toByteArray(blob.getBinaryStream) shouldEqual array
    }

    "returns an InputStream when specify the array dimensions" in new BlobScope {
      toByteArray(blob.getBinaryStream(1, array.length)) shouldEqual array
    }

    "returns an array with one element when specify length 1" in new BlobScope {
      toByteArray(blob.getBinaryStream(2, 1)) shouldEqual Array[Byte](array(1))
    }

    "returns the complete array when the pos plus length is greater than the array length" in new BlobScope {
      toByteArray(blob.getBinaryStream(1, array.length + 1)) shouldEqual array
    }

    "throws a SQLException when the pos param is less than 1" in new BlobScope {
      blob.getBinaryStream(0, array.length) must throwA[SQLException]
    }

    "throws a SQLException when the length param is less than 0" in new BlobScope {
      blob.getBinaryStream(1, -1) must throwA[SQLException]
    }

  }

  "length" should {

    "returns the array size" in new BlobScope {
      blob.length shouldEqual array.length
    }

    "returns 0 for an empty array" in new BlobScope {
      emptyBlob.length shouldEqual 0
    }

  }

  "position" should {

    "throws a SQLFeatureNotSupportedException when passing an array" in new BlobScope {
      blob.position(emptyArray, 0) must throwA[SQLFeatureNotSupportedException]
    }

    "throws a SQLFeatureNotSupportedException when passing a blob" in new BlobScope {
      blob.position(mockBlob, 0) must throwA[SQLFeatureNotSupportedException]
    }

  }

  "setBinaryStream" should {

    "throws a SQLFeatureNotSupportedException" in new BlobScope {
      blob.setBinaryStream(0) must throwA[SQLFeatureNotSupportedException]
    }

  }

  "setBytes" should {

    "throws a SQLFeatureNotSupportedException" in new BlobScope {
      blob.setBytes(0, emptyArray) must throwA[SQLFeatureNotSupportedException]
    }

    "throws a SQLFeatureNotSupportedException when passing a offset and length" in new BlobScope {
      blob.setBytes(0, emptyArray, 0, 1) must throwA[SQLFeatureNotSupportedException]
    }

  }

  "truncate" should {

    "throws a SQLFeatureNotSupportedException" in new BlobScope {
      {blob.truncate(0);true} must throwA[SQLFeatureNotSupportedException]
    }

  }

  "free" should {

    "throws a SQLFeatureNotSupportedException" in new BlobScope {
      {blob.free();true} must throwA[SQLFeatureNotSupportedException]
    }

  }

  "toString" should {

    "return a 'Empty Blob' message when the array is null" in new BlobScope {
      nullBlob.toString must beMatching("(?i).*Empty Blob.*")
    }

    "return a 'Blob length 0' message when the array is empty" in new BlobScope {
      emptyBlob.toString must beMatching("(?i).*Blob length 0.*")
    }

    "return a 'Blob length x' message with the size of the array" in new BlobScope {
      blob.toString must beMatching(s"(?i).*Blob length ${array.length}.*")
    }

  }

}
