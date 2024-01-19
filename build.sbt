ThisBuild / version := "0.1.2"
ThisBuild / scalaVersion := "2.13.12"
ThisBuild / organization := "mrlibs"

lazy val commonSettings = Seq(
  publishTo := {
    Some("mrlibs" at "https://maven.cloudsmith.io/mrlibs/neo4jsupport/")
  },
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
      "com.chuusai" %% "shapeless" % "2.3.10",
      "org.typelevel" %% "cats-effect" % "3.4.8",
      "com.softwaremill.magnolia1_2" %% "magnolia" % "1.1.8",
      "org.scala-lang" % "scala-reflect" % "2.13.12",
      "org.scalatest" %% "scalatest" % "3.2.15" % Test,
      "org.scalatestplus" %% "mockito-4-11" % "3.2.17.0" % Test
    ),
    publishConfiguration := publishConfiguration.value.withOverwrite(true)
  )
