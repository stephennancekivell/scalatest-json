
lazy val root = (project in file("."))
  .settings(commonSettings)
  .aggregate(jsonassert, json4s)

lazy val json4s = (project in file("json4s"))
	.settings(commonSettings)

lazy val jsonassert = (project in file("jsonassert"))
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
  publishTo := sonatypePublishTo.value
)