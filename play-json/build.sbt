name := "scalatest-play-json"

version := "0.0.4-SNAPSHOT"

scalaVersion := "2.12.10"

crossScalaVersions := Seq("2.12.10", "2.13.1")

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-json" % "2.8.0",
  "org.gnieh" %% "diffson-play-json" % "4.0.3",
  "org.scalatest" %% "scalatest" % "3.1.2"
)
