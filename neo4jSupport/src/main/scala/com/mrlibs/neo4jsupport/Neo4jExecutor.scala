package com.mrlibs.neo4jsupport

import cats.effect.IO
import org.neo4j.driver.Session
import org.neo4j.driver.summary.SummaryCounters

trait Neo4jExecutor {
  def list[T: FromMap](query: Query): IO[Either[FromValueExtractionError, List[T]]]
  def option[T: FromMap](query: Query): IO[Either[FromValueExtractionError, Option[T]]]
  def execute(query: Query): IO[SummaryCounters]
}

class Neo4jExecutorImpl(implicit val session: Session) extends Neo4jExecutor {
  override def list[T: FromMap](query: Query): IO[Either[FromValueExtractionError, List[T]]] = {
    query.execute { result =>
      QueryResult.list[T](result)
    }
  }

  override def option[T: FromMap](query: Query): IO[Either[FromValueExtractionError, Option[T]]] = {
    query.execute { result =>
      QueryResult.option[T](result)
    }
  }

  override def execute(query: Query): IO[SummaryCounters] = {
    query.execute { _.consume().counters() }
  }
}
