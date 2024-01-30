ThisBuild / version := "0.1.3"
ThisBuild / scalaVersion := "2.13.12"
ThisBuild / organization := "io.github.maciek-r"
ThisBuild / organizationName := "ruszczyk.maciek"

ThisBuild / scmInfo := Some(
  ScmInfo(url("https://github.com/Maciek-R/Neo4j-Support"), "scm:git@github.Maciek-R/Neo4j-Support.git")
)

ThisBuild / developers := List(
  Developer(
    id = "Maciek-R",
    name = "Maciek-R",
    email = "maciek3633@gmail.com",
    url = url("https://github.com/Maciek-R")
  )
)

ThisBuild / description := "Neo4j Support lib"
ThisBuild / licenses := List("The Unlicense" -> new URI("https://unlicense.org/").toURL)
ThisBuild / homepage := Some(url("https://github.com/Maciek-R/Neo4j-Support"))

ThisBuild / versionScheme := Some("pvp")

ThisBuild / publishTo := {
  val nexus = "https://s01.oss.sonatype.org/"
  if (isSnapshot.value) Some("snapshots" at nexus + "content/repositories/snapshots")
  else Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

lazy val commonSettings = Seq(
  pomIncludeRepository := { _ => false },
  publishMavenStyle := true
)

lazy val root = (project in file("."))
  .settings(commonSettings)
  .settings(
    name := "Neo4jSupport",
    publish / skip := true
  )
  .aggregate(neo4jSupport)

lazy val neo4jSupport = project
  .settings(commonSettings)
  .settings(
    name := "neo4j-support",
    libraryDependencies ++= Seq(
      "org.neo4j" % "neo4j" % "5.5.0",
      "org.neo4j.driver" % "neo4j-java-driver" % "5.6.0",
      "com.chuusai" %% "shapeless" % "2.3.10",
      "org.typelevel" %% "cats-effect" % "3.4.8",
      "com.softwaremill.magnolia1_2" %% "magnolia" % "1.1.8",
      "org.scala-lang" % "scala-reflect" % "2.13.12",
      "org.scalatest" %% "scalatest" % "3.2.15" % Test,
      "org.scalatestplus" %% "mockito-4-11" % "3.2.17.0" % Test
    ),
    publishConfiguration := publishConfiguration.value.withOverwrite(true)
  )
