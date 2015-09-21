package com.fortysevendeg.mvessel.sample.slick.repository

case class Sample(id: Option[Int] = None, title: String, description: String, map: Map[String, String], value: Value)

case class Value(id: Option[Int] = None, value: String)
