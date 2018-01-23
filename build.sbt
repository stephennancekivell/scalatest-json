name := "scalatest-json-jsonassert"
organization := "com.stephenn"
version := "0.0.1-SNAPSHOT"

scalaVersion := "2.11.11"

libraryDependencies ++= Seq(
	"org.skyscreamer" % "jsonassert" % "1.5.0",
	"org.scalatest" %% "scalatest" % "2.2.6"
)
