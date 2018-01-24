Scalatest-json-matchers
===

Scalatest matchers for Json with appropriate equality and descriptive error messages.

install
---

```
libraryDependencies ++= Seq(
    "com.stephenn" %% "scalatest-json-matchers" % "0.0.1"
)
```

usage
---

```
it("test some json output") {
  val json: String = ???
  
  json should matchJson(
    """
      |{
      |   "foo": "bar",
      |  "baz":["bee","boo"]
      |}
    """.stripMargin)
}
```