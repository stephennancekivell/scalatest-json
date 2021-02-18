name := "scalatest-circe"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.2.4",
  "org.gnieh" %% "diffson-circe" % "4.0.3"
)

val circeVersion = "0.13.0"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)
