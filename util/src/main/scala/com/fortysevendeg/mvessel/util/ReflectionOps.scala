package com.fortysevendeg.mvessel.util

import java.lang.reflect.Method
import java.sql.Types

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

import scala.util.{Success, Try}

object ReflectionOps {

  private[this] val cursorGetType: Option[Method] = Try(classOf[Cursor].getMethod("getType", classOf[Int])).toOption

  /**
   * The count of changed rows on Android it's a call to a package-private method
   */
  private[this] val databaseLastChangeCount: Option[Method] =
    Try(classOf[SQLiteDatabase].getDeclaredMethod("lastChangeCount")) match {
      case Success(m) =>
        m.setAccessible(true)
        Some(m)
      case _ => None
    }

  /**
   * Added in API level 11
   */
  val lockedExceptionClass: Option[Class[_]] =
    Try(Class.forName("android.database.sqlite.SQLiteDatabaseLockedException")).toOption

  def getTypeSafe(cursor: Cursor, index: Int): Int =
    cursorGetType map (_.invoke(cursor, index.asInstanceOf[Integer]).asInstanceOf[Int]) getOrElse Types.OTHER

  def lastChangeCount(database: SQLiteDatabase): Option[Int] =
    databaseLastChangeCount map (_.invoke(database).asInstanceOf[Int])
}
