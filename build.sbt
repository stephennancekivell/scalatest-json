organization := "com.stephenn"
publishTo := sonatypePublishTo.value

lazy val root = (project in file("."))
  .aggregate(jsonassert, json4s)

lazy val jsonassert = project

lazy val json4s = project
