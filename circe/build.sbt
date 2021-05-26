name := "scalatest-circe"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.2.9",
  "org.gnieh" %% "diffson-circe" % "4.1.0"
)

val circeVersion = "0.14.0"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)
