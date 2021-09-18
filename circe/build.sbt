name := "scalatest-circe"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.2.10",
  "org.gnieh" %% "diffson-circe" % "4.1.1"
)

val circeVersion = "0.14.1"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)
