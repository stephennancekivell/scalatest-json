name := "scalatest-play-json"

version := "0.0.4-SNAPSHOT"

scalaVersion := "2.12.12"

crossScalaVersions := Seq("2.12.12", "2.13.3")

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-json" % "2.8.0",
  "org.gnieh" %% "diffson-play-json" % "4.0.2",
  "org.scalatest" %% "scalatest" % "3.1.2"
)
