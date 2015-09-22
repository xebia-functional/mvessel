package com.fortysevendeg.mvessel.sample.slick.db

case class SampleEntity(id: Option[Int], title: String, description: String, map: Option[String], valueId: Int)

case class ValueEntity(id: Option[Int], value: String)
