package com.fortysevendeg.mvessel.sample.slick.db

import slick.driver.SQLiteDriver.api._

trait SlickDatabaseConfig {

  class ValueTable(tag: Tag) extends Table[ValueEntity](tag, "value") {
    def id = column[Int]("_id", O.PrimaryKey, O.AutoInc)

    def value = column[String]("value")

    def * = (id.?, value) <> (ValueEntity.tupled, ValueEntity.unapply)
  }

  lazy val valueTable = TableQuery[ValueTable]

  class SampleTable(tag: Tag) extends Table[SampleEntity](tag, "sample") {
    def id = column[Int]("_id", O.PrimaryKey, O.AutoInc)

    def title = column[String]("title")

    def description = column[String]("description")

    def map = column[Option[String]]("map")

    def valueId = column[Int]("value_id")

    def * = (id.?, title, description, map, valueId) <> (
      {t: (Option[Int], String, String, Option[String], Int) => SampleEntity(t._1, t._2, t._3, t._4, t._5)},
      {t: (SampleEntity) => Some(t.id, t.title, t.description, t.map, t.valueId)}
    )

    def value = foreignKey("value_fk", valueId, valueTable)(_.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Cascade)
  }

  lazy val sampleTable = TableQuery[SampleTable]
}
