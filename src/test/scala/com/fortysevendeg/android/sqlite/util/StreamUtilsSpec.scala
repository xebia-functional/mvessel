package com.fortysevendeg.android.sqlite.util

import java.io.{Closeable, StringReader, ByteArrayInputStream}

import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.specification.Scope
import StreamUtils._

class StreamUtilsSpecification
  extends Specification
  with Mockito {

  val string = "Sample String"

  class InputStreamScope
    extends Scope {

    val bytes = string.getBytes

    val inputStream = new ByteArrayInputStream(bytes)

    val emptyInputStream = new ByteArrayInputStream(Array.empty)

  }

  class ReaderScope
    extends Scope {

    val reader = new StringReader(string)

    val emptyReader = new StringReader("")

  }

  class CloseableScope
    extends Scope {

    val closeable = mock[Closeable]

  }

}

class StreamUtilsSpec
  extends StreamUtilsSpecification {

  "inputStreamToByteArray" should {

    "return the byte array when passing a InputStream" in
      new InputStreamScope {
        inputStreamToByteArray(inputStream) shouldEqual bytes
      }

    "return the byte array when passing a InputStream and a max length greater than the byte array length" in
      new InputStreamScope {
        inputStreamToByteArray(inputStream, Int.MaxValue) shouldEqual bytes
      }

    "return the byte array sliced when passing a InputStream and a max length" in
      new InputStreamScope {
        val maxLength = 2
        inputStreamToByteArray(inputStream, maxLength) shouldEqual bytes.slice(0, maxLength)
      }

    "return an empty byte array when passing an empty InputStream" in
      new InputStreamScope {
        inputStreamToByteArray(emptyInputStream, Int.MaxValue) shouldEqual Array.empty
      }

  }

  "readerToString" should {

    "return the string when passing a Reader" in
      new ReaderScope {
        readerToString(reader) shouldEqual string
      }

    "return the string when passing a Reader and a max length greater than the string" in
      new ReaderScope {
        readerToString(reader, Int.MaxValue) shouldEqual string
      }

    "return the string sliced when passing a Reader and a max length" in
      new ReaderScope {
        val maxLength = 2
        readerToString(reader, maxLength) shouldEqual string.substring(0, maxLength)
      }

    "return an empty byte array when passing an empty Reader" in
      new ReaderScope {
        readerToString(emptyReader) shouldEqual ""
      }
  }

  "withResource" should {

    "call close on resource when the function executes normally" in
      new CloseableScope {
        withResource(closeable)(_ => ())
        there was one(closeable).close
      }

    "call close on resource when the function throws an exception" in
      new CloseableScope {
        val msg = "Error"
        def f(c: Closeable): String = throw new RuntimeException(msg)
        withResource(closeable)(f) must throwA[RuntimeException](msg)
        there was one(closeable).close
      }

  }

}
