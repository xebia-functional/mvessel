package com.fortysevendeg.android.sqlite

import java.io.{ByteArrayInputStream, InputStream, OutputStream}
import java.sql.{SQLException, Blob, SQLFeatureNotSupportedException}

class SQLDroidBlob(byteArray: Array[Byte]) extends Blob {

  private[this] val arrayLength = Option(byteArray) map (_.length) getOrElse 0

  def getBytes(pos: Long, length: Int): Array[Byte] = {
    val newPos = pos - 1
    val newLength = if (length > arrayLength) arrayLength else length
    (newPos, newLength, arrayLength) match {
      case (p, l, myL) if p >= 0 && l >= 0 && p + 1 <= myL =>
        byteArray.slice(newPos.toInt, (newPos + l).toInt)
      case _ =>
        throw new SQLException("Invalid params")
    }
  }

  def getBinaryStream: InputStream = new ByteArrayInputStream(byteArray)

  def getBinaryStream(pos: Long, length: Long): InputStream =
    new ByteArrayInputStream(getBytes(pos, length.toInt))

  def length: Long = arrayLength

  def position(pattern: Blob, start: Long): Long =
    throw new SQLFeatureNotSupportedException

  def position(pattern: Array[Byte], start: Long): Long =
    throw new SQLFeatureNotSupportedException

  def setBinaryStream(pos: Long): OutputStream =
    throw new SQLFeatureNotSupportedException

  def setBytes(pos: Long, theBytes: Array[Byte]): Int =
    throw new SQLFeatureNotSupportedException

  def setBytes(pos: Long, theBytes: Array[Byte], offset: Int, len: Int): Int =
    throw new SQLFeatureNotSupportedException

  def truncate(len: Long) =
    throw new SQLFeatureNotSupportedException

  def free() =
    throw new SQLFeatureNotSupportedException

  override def toString: String = {
    Option(this.byteArray) match {
      case Some(b) =>
        val prefix = "Blob length %d".format(b.length)
        val toPrint = if (b.length > 10) b.slice(0, 10) else b
        val hexString = toPrint map ("0x" + Integer.toHexString(_)) mkString " "
        val charString = toPrint map (b => "(" + Character.toString(b.toChar)) mkString " "
        prefix + " " + hexString + " " + charString
      case None => "Empty Blob"
    }
  }
}
