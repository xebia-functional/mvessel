package com.fortysevendeg.mvessel.data

import java.io.{ByteArrayInputStream, InputStream, OutputStream}
import java.sql.{Blob => SQLBlob, SQLException, SQLFeatureNotSupportedException}

class Blob(byteArray: Array[Byte]) extends SQLBlob {

  private[this] val arrayLength = Option(byteArray) map (_.length) getOrElse 0

  override def getBytes(pos: Long = 1, length: Int): Array[Byte] = {
    val newPos = pos - 1
    val newLength = if (length > arrayLength) arrayLength else length
    (newPos, newLength, arrayLength) match {
      case (p, l, myL) if p >= 0 && l >= 0 && p + 1 <= myL =>
        byteArray.slice(newPos.toInt, (newPos + l).toInt)
      case _ =>
        throw new SQLException("Invalid params")
    }
  }

  override def getBinaryStream: InputStream = new ByteArrayInputStream(byteArray)

  override def getBinaryStream(pos: Long = 1, length: Long): InputStream =
    new ByteArrayInputStream(getBytes(pos, length.toInt))

  override def length: Long = arrayLength

  override def position(pattern: SQLBlob, start: Long): Long =
    throw new SQLFeatureNotSupportedException

  override def position(pattern: Array[Byte], start: Long): Long =
    throw new SQLFeatureNotSupportedException

  override def setBinaryStream(pos: Long): OutputStream =
    throw new SQLFeatureNotSupportedException

  override def setBytes(pos: Long, theBytes: Array[Byte]): Int =
    throw new SQLFeatureNotSupportedException

  override def setBytes(pos: Long, theBytes: Array[Byte], offset: Int, len: Int): Int =
    throw new SQLFeatureNotSupportedException

  override def truncate(len: Long) =
    throw new SQLFeatureNotSupportedException

  override def free() =
    throw new SQLFeatureNotSupportedException

  override def toString: String = {
    Option(this.byteArray) match {
      case Some(b) =>
        val prefix = "Blob length %d".format(b.length)
        b.length match {
          case 0 => prefix
          case l =>
            val toPrint = if (l > 10) b.slice(0, 10) else b
            val hexString = toPrint map ("0x" + Integer.toHexString(_)) mkString ""
            val charString = toPrint map (b => "(" + Character.toString(b.toChar)) mkString ""
            s"$prefix $hexString $charString"
        }
      case None => "Empty Blob"
    }
  }
}
