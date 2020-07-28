name := "scalatest-argonaut"

version := "0.0.5-SNAPSHOT"

scalaVersion := "2.12.12"

crossScalaVersions := Seq("2.12.12", "2.13.3")

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.1.2",
  "io.argonaut" %% "argonaut" % "6.2.5",
  "com.softwaremill.diffx" %% "diffx-core" % "0.3.15"
)
