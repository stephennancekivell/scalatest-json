name := "scalatest-json4s"

version := "0.0.5-SNAPSHOT"

scalaVersion := "2.12.12"

crossScalaVersions := Seq("2.12.12", "2.13.3")

libraryDependencies ++= Seq(
  "org.json4s" %% "json4s-native" % "3.6.9",
  "org.scalatest" %% "scalatest" % "3.1.2"
)
