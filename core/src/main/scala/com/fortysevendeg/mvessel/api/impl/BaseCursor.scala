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
