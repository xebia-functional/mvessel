package com.fortysevendeg.mvessel.sample.slick.repository

import com.fortysevendeg.mvessel.sample.slick.db.{SampleEntity, ValueEntity, SlickDatabaseHelper}
import play.api.libs.json.Json
import slick.driver.SQLiteDriver.api._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

class Repository(helper: SlickDatabaseHelper) {

  def insertSample(sample: Sample)(implicit executionContext: ExecutionContext): Future[Sample] =
    helper.database.run(
      for {
        valueId <- (helper.valueTable returning helper.valueTable.map(_.id)) +=
          toValueEntity(sample.value)
        sampleId <- (helper.sampleTable returning helper.sampleTable.map(_.id)) +=
          toSampleEntity(sample, valueId)
      } yield sample.copy(id = Some(sampleId)))

  def fetchAllSample()(implicit executionContext: ExecutionContext): Future[Seq[Sample]] = {
    val innerJoin = for {
      (sampleEntity, valueEntity) <- helper.sampleTable join helper.valueTable on (_.valueId === _.id)
    } yield (sampleEntity, valueEntity)

    helper.database.run(innerJoin.result) map fromSampleEntitySeq
  }

  private def toValueEntity(value: Value): ValueEntity = ValueEntity(value.id, value.value)

  private def toSampleEntity(sample: Sample, valueId: Int): SampleEntity =
    SampleEntity(
      id = sample.id,
      title = sample.title,
      description = sample.description,
      map = mapToJson(sample.map),
      valueId = valueId)

  private def fromValueEntity(valueEntity: ValueEntity): Value = Value(valueEntity.id, valueEntity.value)

  private def fromSampleEntity(sampleEntity: SampleEntity, value: Value): Sample =
    Sample(
      id = sampleEntity.id,
      title = sampleEntity.title,
      description = sampleEntity.description,
      map = sampleEntity.map map jsonToMap getOrElse Map.empty,
      value = value)

  private def fromSampleEntitySeq(seq: Seq[(SampleEntity, ValueEntity)]): Seq[Sample] =
    seq map (t => fromSampleEntity(t._1, fromValueEntity(t._2)))

  private def jsonToMap(json: String): Map[String, String] =
    Try {
      Json.parse(json).as[Map[String, String]]
    } match {
      case Success(m) => m
      case Failure(e) => Map.empty
    }

  private def mapToJson(map: Map[String, String]): Option[String] =
    Try {
      Json.toJson(map).toString()
    } match {
      case Success(s) => Some(s)
      case Failure(e) => None
    }
}
