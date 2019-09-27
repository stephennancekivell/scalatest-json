name := "scalatest-play-json"

version := "0.0.2-SNAPSHOT"

scalaVersion := "2.12.10"

crossScalaVersions := Seq("2.12.10", "2.13.1")

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-json" % "2.7.4",
  "org.gnieh" %% "diffson-play-json" % "4.0.0",
  "org.scalatest" %% "scalatest" % "3.0.8"
)
