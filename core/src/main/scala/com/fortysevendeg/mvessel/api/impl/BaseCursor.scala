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

package com.fortysevendeg.mvessel.api.impl

import com.fortysevendeg.mvessel.api.CursorProxy

trait BaseCursor extends CursorProxy {

  private[this] var closed: Boolean = false

  private[this] var index: Int = -1

  override def isClosed: Boolean = closed

  override def getPosition: Int = index

  override def isBeforeFirst: Boolean = index < 0

  override def isAfterLast: Boolean = index >= getCount

  override def isLast: Boolean = index == getCount - 1

  override def isFirst: Boolean = index == 0

  override def close(): Unit = closed = true

  override def moveToFirst(): Boolean = tryToMove(0)

  override def move(offset: Int): Boolean = tryToMove(index + offset)

  override def moveToNext(): Boolean = tryToMove(index + 1)

  override def moveToPrevious(): Boolean = tryToMove(index - 1)

  override def moveToLast(): Boolean = tryToMove(getCount - 1)

  override def moveToPosition(position: Int): Boolean = tryToMove(position)

  private[this] def tryToMove(position: Int): Boolean = position match {
    case p if getCount == 0 && p >= 0 => false
    case p if p < -1 || p > getCount => false
    case p if p == index => false
    case p =>
      index = p
      index >= 0 && index < getCount
  }
}
