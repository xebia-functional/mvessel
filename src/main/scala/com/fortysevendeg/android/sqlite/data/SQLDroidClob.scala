package com.fortysevendeg.android.sqlite.data

import java.io._
import java.sql.{Clob, SQLException, SQLFeatureNotSupportedException}

class SQLDroidClob(string: String) extends Clob {
  
  private[this] val stringLength = Option(string) map (_.length) getOrElse 0

  override def length(): Long = stringLength

  override def truncate(len: Long): Unit =
    throw new SQLFeatureNotSupportedException

  override def free(): Unit =
    throw new SQLFeatureNotSupportedException

  override def getAsciiStream: InputStream =
    Option(string) map (s => new ByteArrayInputStream(s.getBytes)) getOrElse (throw new SQLException("Empty Clob"))
  
  override def setAsciiStream(pos: Long): OutputStream =
    throw new SQLFeatureNotSupportedException

  override def getSubString(pos: Long = 1, length: Int): String = {
    val newPos = pos - 1
    val newLength = if (length > stringLength) stringLength else length
    (newPos, newLength) match {
      case (p, l) if p >= 0 && l >= 0 && p + l <= stringLength =>
        string.substring(p.toInt, (p + l).toInt)
      case _ => throw new SQLException("Invalid params")
    }
  }

  override def setString(pos: Long, str: String): Int =
    throw new SQLFeatureNotSupportedException

  override def setString(pos: Long, str: String, offset: Int, len: Int): Int =
    throw new SQLFeatureNotSupportedException

  override def getCharacterStream: Reader =
    Option(string) map (s => new StringReader(s)) getOrElse (throw new SQLException("Empty Clob"))

  override def getCharacterStream(pos: Long = 1, length: Long): Reader =
    new StringReader(getSubString(pos, length.toInt))

  override def setCharacterStream(pos: Long): Writer =
    throw new SQLFeatureNotSupportedException

  override def position(searchstr: String, start: Long): Long =
    throw new SQLFeatureNotSupportedException

  override def position(searchstr: Clob, start: Long): Long =
    throw new SQLFeatureNotSupportedException
}
