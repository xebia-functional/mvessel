package com.fortysevendeg.android.sqlite.util

import java.io.{BufferedReader, Closeable, InputStream, Reader}

import scala.util.Try
import scala.util.control.Exception._

object StreamUtils {

  def inputStreamToByteArray(inputStream: InputStream): Array[Byte] = withResource(inputStream) { is =>
    Iterator continually is.read takeWhile (_ != -1) map (_.toByte) toArray
  }

  def readerToString(reader: Reader): String = withResource(reader) { r =>
    val buffer = new BufferedReader(r)
    val result = Iterator continually buffer.readLine takeWhile (Option(_).isDefined)
    Try(buffer.close())
    result.mkString
  }

  def withResource[C <: Closeable, R](closeable: C)(f: C => R) =
    allCatch.andFinally(closeable.close())(f(closeable))

}
