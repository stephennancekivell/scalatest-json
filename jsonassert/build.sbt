name := "scalatest-json-jsonassert"

version := "0.0.4-SNAPSHOT"

scalaVersion := "2.11.12"

crossScalaVersions := Seq("2.11.12", "2.12.4")

libraryDependencies ++= Seq(
	"org.skyscreamer" % "jsonassert" % "1.5.0",
	"org.scalatest" %% "scalatest" % "3.0.4"
)

