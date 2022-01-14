import sbt.Keys.crossScalaVersions

inThisBuild(
  List(
    organization := "com.stephenn",
    homepage := Some(
      url("https://github.com/stephennancekivell/scalatest-json")
    ),
    scmInfo := Some(
      ScmInfo(
        url("https://github.com/stephennancekivell/scalatest-json"),
        "git@github.com:stephennancekivell/scalatest-json.git"
      )
    ),
    developers := List(
      Developer(
        "stephennancekivell",
        "Stephen Nancekivell",
        "stephennancekivell+github@gmail.com",
        url("https://stephenn.com")
      ),
      Developer(
        "baldram",
        "Marcin Szałomski",
        "baldram+github@gmail.com",
        url("https://twitter.com/baldram")
      )
    ),
    licenses += ("Apache-2.0", url(
      "http://www.apache.org/licenses/LICENSE-2.0"
    ))
  )
)

lazy val root = (project in file("."))
  .settings(
    commonSettings,
    publish / skip := true
  )
  .aggregate(jsonassert, json4s, playJson, circe, argonaut, jsoniterScala)

lazy val json4s = (project in file("json4s"))
  .settings(commonSettings)

lazy val jsonassert = (project in file("jsonassert"))
  .settings(commonSettings, crossScalaVersions += "3.0.1")

lazy val playJson = (project in file("play-json"))
  .settings(commonSettings)

lazy val circe = (project in file("circe"))
  .settings(commonSettings, crossScalaVersions += "3.0.1")

lazy val argonaut = (project in file("argonaut"))
  .settings(commonSettings)

lazy val jsoniterScala = (project in file("jsoniter-scala"))
  .settings(commonSettings)

lazy val commonSettings = Seq(
  scalaVersion := "2.12.14",
  crossScalaVersions := Seq("2.12.14", "2.13.8"),
  scalacOptions ++= Seq(
    "-deprecation",
    "-encoding",
    "UTF-8", // yes, this is 2 args
    "-language:higherKinds",
    "-language:existentials",
    "-language:implicitConversions",
    "-unchecked",
    "-Xfatal-warnings"
//    "-Xlint",
//    "-Ywarn-numeric-widen",
//    "-Ywarn-value-discard",
    // "-source:3.0-migration"
  )
)
