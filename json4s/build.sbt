name := "scalatest-json4s"

version := "0.0.2-SNAPSHOT"

scalaVersion := "2.11.12"

crossScalaVersions := Seq("2.11.12", "2.12.4")

libraryDependencies ++= Seq(
	"org.json4s" %% "json4s-native" % "3.5.3",
	"org.scalatest" %% "scalatest" % "3.0.4"
)

