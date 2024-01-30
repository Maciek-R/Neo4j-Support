# Neo4j Support

This project some generic Type Classes useful for handling data from databases.
Like Converting Map of String->Any into any case class.

For example given map can be transformed into specific case class with just using FromMap TypeClass:
```scala
case class TestClass(
    int1: Int,
    optInt1: Option[Int],
    str1: String,
    optStr1: Option[String],
    bool: Boolean,
    optBool: Option[Boolean]
)

val map = Map(
  "int1" -> 123,
  "optInt1" -> 456,
  "str1" -> "str1",
  "optStr1" -> "optStr1",
  "bool" -> true,
  "optBool" -> false
)

FromMap.apply[TestClass].fromMap(map)
```

Scala compiler should generate appropriate instance of FromMap[TestClass]

# Installation
```scala
libraryDependencies ++= Seq(
  "io.github.maciek-r" %% "neo4j-support" % "0.1.3"
)
```

Remember to add resolver to find dependency in Cloudsmith for example like this:

```scala
resolvers +=
  "releases" at "https://s01.oss.sonatype.org/content/groups/staging"
```