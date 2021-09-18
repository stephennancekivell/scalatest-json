name := "scalatest-jsoniter-scala"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.2.10",
  "com.github.plokhotnyuk.jsoniter-scala" %% "jsoniter-scala-core"   % "2.9.1",
  "com.github.plokhotnyuk.jsoniter-scala" %% "jsoniter-scala-macros" % "2.9.1" % "provided",
  "com.softwaremill.diffx" %% "diffx-core" % "0.3.30"
)
