name := "scalatest-play-json"

version := "0.0.2-SNAPSHOT"

scalaVersion := "2.12.10"

crossScalaVersions := Seq("2.12.10")

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-json" % "2.7.4",
  "org.gnieh" %% "diffson-play-json" % "3.1.1",
  "org.scalatest" %% "scalatest" % "3.0.8"
)
