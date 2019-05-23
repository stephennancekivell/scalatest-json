name := "scalatest-circe"

version := "0.0.2-SNAPSHOT"

scalaVersion := "2.11.12"

crossScalaVersions := Seq("2.11.12", "2.12.8")

libraryDependencies ++= Seq(
	"org.scalatest" %% "scalatest" % "3.0.4",
  "org.gnieh" %% "diffson-circe" % "2.2.5"
)

val circeVersion = "0.9.0"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)