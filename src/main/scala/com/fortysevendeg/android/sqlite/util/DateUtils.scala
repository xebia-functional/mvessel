package com.fortysevendeg.android.sqlite.util

import java.text.SimpleDateFormat
import java.util.Date

import com.fortysevendeg.android.sqlite.logging.LogWrapper

import scala.util.{Failure, Success, Try}

trait DateUtils {

  val log: LogWrapper

  private[this] val dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S")

  def parseDate(date: String): Option[java.util.Date] =
    Try(dateFormat.parse(date)) match {
      case Success(d) => Some(d)
      case Failure(e) =>
        log.e("Can't parse date", Some(e))
        None
    }

  def formatDate(date: Date): Option[String] =
    Try(dateFormat.format(date)) match {
      case Success(d) => Some(d)
      case Failure(e) =>
        log.e("Can't format date", Some(e))
        None
    }

}