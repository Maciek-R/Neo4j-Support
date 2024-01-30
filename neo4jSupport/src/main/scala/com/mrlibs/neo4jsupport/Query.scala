package com.mrlibs.neo4jsupport

import cats.effect.IO
import cats.implicits.toTraverseOps
import org.neo4j.driver.types.TypeSystem
import org.neo4j.driver.{AuthTokens, Driver, GraphDatabase, Result, Session}

import scala.jdk.CollectionConverters._

case class AppConfig(databaseConfig: DatabaseConfig)

case class DatabaseConfig(uri: String, neo4jCredentials: Neo4jCredentials)

case class Neo4jCredentials(username: String, password: String)

object QueryResult {
  def list[T: FromMap](result: Result): Either[FromValueExtractionError, List[T]] = {
    val results = result.asScala.toList.map { r =>
      convert[T](r)
    }
    results.sequence
  }

  def option[T: FromMap](result: Result): Either[FromValueExtractionError, Option[T]] = {
    result.asScala.toList match {
      case head :: Nil => convert[T](head).map(Option(_))
      case Nil         => Left(MissingFieldError) //TODO
      case rest        => Left(MissingFieldError) //TODO
    }
  }

  private def convert[T: FromMap](record: org.neo4j.driver.Record) = {
    val fields = record.fields().asScala.toList.map(p => (p.key(), p.value())).flatMap { case (key, value) =>
      if (value.hasType(TypeSystem.getDefault.STRING())) {
        Some((key, value.asString()))
      } else if (value.hasType(TypeSystem.getDefault.INTEGER())) {
        Some((key, value.asInt()))
      } else if (value.hasType(TypeSystem.getDefault.BOOLEAN())) {
        Some((key, value.asBoolean()))
      } else {
        None
      }
    }
    implicitly[FromMap[T]].fromMap(fields.toMap)
  }
}

case class Query(query: String) {
  def execute[T](funConversion: Result => T)(implicit session: Session): IO[T] = {
    for {
      result <- IO.blocking(session.run(query))
    } yield funConversion(result)
  }
}
