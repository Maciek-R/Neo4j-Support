package com.mrlibs.neo4jsupport

import com.mrlibs.neo4jsupport.utils.RepositoryTest
import org.scalatest.EitherValues
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Neo4jExecutorTest extends AnyFlatSpec with Matchers with EitherValues with RepositoryTest {

  private val neo4jExecutor = new Neo4jExecutorImpl

  case class Member(name: String)

  it should "create nodes" in {
    val query = Query(s"CREATE (m:Member{name: 'testName'})")

    val summary = neo4jExecutor.execute(query).unsafeRunSync()
    summary.nodesCreated() shouldBe 1
  }

  it should "create and get node" in {
    val query = Query(s"CREATE (m:Member{name: 'testName'})")

    val createMember = neo4jExecutor.execute(query)

    val getQuery = Query(
      s"""MATCH (m:Member)
         |RETURN m.name as name
         |""".stripMargin
    )

    val getMember = neo4jExecutor.option[Member](getQuery)

    val member = (createMember >> getMember).unsafeRunSync().value
    member shouldBe Some(Member("testName"))
  }

  it should "create and get nodes" in {
    val query1 = Query(s"CREATE (m:Member{name: 'testName1'})")
    val query2 = Query(s"CREATE (m:Member{name: 'testName2'})")

    val createMembers = neo4jExecutor.execute(query1) >> neo4jExecutor.execute(query2)

    val getQuery = Query(
      s"""MATCH (m:Member)
         |RETURN m.name as name
         |""".stripMargin
    )

    val getMembers = neo4jExecutor.list[Member](getQuery)

    val members = (createMembers >> getMembers).unsafeRunSync().value
    members should contain allElementsOf List(Member("testName1"), Member("testName2"))
  }
}
