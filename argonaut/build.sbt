name := "scalatest-argonaut"

version := "0.0.1-SNAPSHOT"

scalaVersion := "2.12.10"

crossScalaVersions := Seq("2.12.10", "2.13.1")

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.1.0",
  "io.argonaut" %% "argonaut" % "6.2.3"
)
