name := "scalatest-play-json"

version := "0.0.2-SNAPSHOT"

scalaVersion := "2.11.12"

crossScalaVersions := Seq("2.11.12", "2.12.8")

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-json" % "2.6.7",
  "org.gnieh" %% "diffson-play-json" % "2.2.4",
  "org.scalatest" %% "scalatest" % "3.0.4"
)
