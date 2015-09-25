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

package com.fortysevendeg.mvessel.util

import java.io.{BufferedReader, Closeable, InputStream, Reader}

import scala.annotation.tailrec
import scala.util.Try
import scala.util.control.Exception._

object StreamUtils {

  def inputStreamToByteArray(is: InputStream, maxLength: Int): Array[Byte] =
    inputStreamToByteArray(is, Some(maxLength))

  def inputStreamToByteArray(is: InputStream, maxLength: Option[Int] = None): Array[Byte] =
    withResource(is) { is =>
      def isLengthValid(length: Int): Boolean = maxLength match {
        case Some(m) => length < m
        case _ => true
      }
      @tailrec
      def loop(seq: Seq[Byte] = Seq.empty, length: Int = 0): Seq[Byte] = {
        is.read match {
          case b if isLengthValid(length) && b > 0 =>
            loop(seq :+ b.toByte, length + 1)
          case _ =>
            seq
        }
      }
      loop()
    }.toArray

  def readerToString(reader: Reader, maxLength: Int): String =
    readerToString(reader, Some(maxLength))

  def readerToString(reader: Reader, maxLength: Option[Int] = None): String = {
    def isLengthValid(length: Int): Boolean = maxLength match {
      case Some(m) => length < m
      case _ => true
    }
    def fixLength(result: String): String = maxLength match {
      case Some(m) if m < result.length => result.substring(0, m)
      case _ => result
    }
    withResource(reader) { r =>
      val buffer = new BufferedReader(r)
      @tailrec
      def loop(seq: Seq[String] = Seq.empty, length: Int = 0): Seq[String] = {
        buffer.readLine match {
          case b if isLengthValid(length) && Option(b).isDefined =>
            loop(seq :+ b, length + b.length)
          case _ =>
            seq
        }
      }
      val result = loop().mkString
      Try(buffer.close())
      fixLength(result)
    }
  }

  def withResource[C <: Closeable, R](closeable: C)(f: C => R) =
    allCatch.andFinally(closeable.close())(f(closeable))

}
