name := "scalatest-json-jsonassert"

version := "0.0.5-SNAPSHOT"

scalaVersion := "2.12.8"

crossScalaVersions := Seq("2.12.8", "2.13.0")

libraryDependencies ++= Seq(
  "org.skyscreamer" % "jsonassert" % "1.5.0",
  "org.scalatest" %% "scalatest" % "3.0.8"
)
