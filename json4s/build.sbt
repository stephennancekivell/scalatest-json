name := "scalatest-json4s"

version := "0.0.5-SNAPSHOT"

scalaVersion := "2.12.10"

crossScalaVersions := Seq("2.12.10", "2.13.1")

libraryDependencies ++= Seq(
  "org.json4s" %% "json4s-native" % "3.6.8",
  "org.scalatest" %% "scalatest" % "3.1.0"
)
