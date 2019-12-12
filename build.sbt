
lazy val root = (project in file("."))
  .settings(
    commonSettings,
    publish / skip := true
  )
  .aggregate(jsonassert, json4s, playJson, circe, argonaut)

lazy val json4s = (project in file("json4s"))
	.settings(commonSettings)

lazy val jsonassert = (project in file("jsonassert"))
	.settings(commonSettings)

lazy val playJson = (project in file("play-json"))
  .settings(commonSettings)

lazy val circe = (project in file("circe"))
  .settings(commonSettings)

lazy val argonaut = (project in file("argonaut"))
  .settings(commonSettings)

lazy val commonSettings = Seq(
  organization := "com.stephenn",
  homepage := Some(url("https://github.com/stephennancekivell/scalatest-json")),
  scmInfo := Some(ScmInfo(url("https://github.com/stephennancekivell/scalatest-json"),
                            "git@github.com:stephennancekivell/scalatest-json.git")),
  developers := List(Developer("stephennancekivell",
                             "Stephen Nancekivell",
                             "stephennancekivell@gmail.com",
                             url("https://stephenn.com"))),
  licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0")),
  publishMavenStyle := true,
  publishTo := sonatypePublishTo.value,
  scalacOptions ++= Seq(
    "-deprecation",
    "-encoding", "UTF-8",       // yes, this is 2 args
    "-language:existentials",
    "-language:higherKinds",
    "-language:implicitConversions",
    "-unchecked",
    "-Xfatal-warnings",
    "-Xlint",
    "-Ywarn-numeric-widen",
    "-Ywarn-value-discard",
  )
)