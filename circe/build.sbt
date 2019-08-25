name := "scalatest-circe"

version := "0.0.2-SNAPSHOT"

scalaVersion := "2.12.8"

crossScalaVersions := Seq("2.12.8")

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.8",
  "org.gnieh" %% "diffson-circe" % "4.0.0-M4"
)

val circeVersion = "0.12.0-RC3"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)
