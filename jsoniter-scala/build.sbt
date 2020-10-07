name := "scalatest-jsoniter-scala"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.2.1",
  "com.github.plokhotnyuk.jsoniter-scala" %% "jsoniter-scala-core"   % "2.6.2",
  "com.github.plokhotnyuk.jsoniter-scala" %% "jsoniter-scala-macros" % "2.6.2" % "provided",
  "com.softwaremill.diffx" %% "diffx-core" % "0.3.29"
)
