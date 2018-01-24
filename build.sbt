name := "scalatest-json-jsonassert"
organization := "com.stephenn"
version := "0.0.2"

scalaVersion := "2.11.12"

libraryDependencies ++= Seq(
	"org.skyscreamer" % "jsonassert" % "1.5.0",
	"org.scalatest" %% "scalatest" % "2.2.6"
)

publishTo := sonatypePublishTo.value
