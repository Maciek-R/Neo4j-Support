package com.mrlibs.neo4jsupport.utils

import cats.effect.unsafe.IORuntime
import com.mrlibs.neo4jsupport.{AppConfig, DatabaseConfig, Neo4jCredentials, Query}
import org.neo4j.driver.{AuthTokens, Driver, GraphDatabase, Session}
import org.scalatest.{BeforeAndAfterEach, Suite}
import cats.effect.unsafe.implicits.global

trait RepositoryTest extends BeforeAndAfterEach {
  this: Suite =>

  val appConfig = AppConfig(
    DatabaseConfig(
      "bolt://localhost/7687",
      Neo4jCredentials("neo4j", "neo4jneo4j")
    )
  )

  implicit val globalRuntime: IORuntime = global

  lazy val driver: Driver = GraphDatabase.driver(
    appConfig.databaseConfig.uri,
    AuthTokens.basic(
      appConfig.databaseConfig.neo4jCredentials.username,
      appConfig.databaseConfig.neo4jCredentials.password
    )
  )

  implicit lazy val session: Session = driver.session()

  override def beforeEach(): Unit = {
    val query = Query(s"MATCH (a) DETACH DELETE (a)")
    query.execute(_.consume().counters().nodesDeleted()).unsafeRunSync()(globalRuntime)
  }
}
