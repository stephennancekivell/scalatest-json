name := "scalatest-circe"

version := "0.0.2-SNAPSHOT"

scalaVersion := "2.12.8"

crossScalaVersions := Seq("2.12.8", "2.13.0")

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.8",
  "org.gnieh" %% "diffson-circe" % "4.0.0-M5"
)

val circeVersion = "0.12.1"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)
