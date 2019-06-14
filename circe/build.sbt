name := "scalatest-circe"

version := "0.0.2-SNAPSHOT"

scalaVersion := "2.12.8"

crossScalaVersions := Seq("2.12.8")

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.8",
  "org.gnieh" %% "diffson-circe" % "3.1.1"
)

val circeVersion = "0.11.1"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)
