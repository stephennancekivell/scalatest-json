name := "scalatest-json4s"

version := "0.0.4-SNAPSHOT"

scalaVersion := "2.11.12"

crossScalaVersions := Seq("2.11.12", "2.12.8", "2.13.0")

libraryDependencies ++= Seq(
  "org.json4s" %% "json4s-native" % "3.6.6",
  "org.scalatest" %% "scalatest" % "3.0.8"
)
