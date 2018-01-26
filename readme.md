Scalatest-json
===
[![Build Status](https://travis-ci.org/stephennancekivell/scalatest-json.svg?branch=master)](https://travis-ci.org/stephennancekivell/scalatest-json)

Scalatest matchers for Json with appropriate equality and descriptive error messages.

install
---

Add the dependency you want
```
libraryDependencies += "com.stephenn" %% "scalatest-json-jsonassert" % "0.0.2"
libraryDependencies += "com.stephenn" %% "scalatest-json4s" % "0.0.1"
```

usage
---
Your scalatest spec should extend or import `JsonMatchers`

```
class ExampleSpec extends FunSpec with Matchers with JsonMatchers {

  describe("JsonMatchers usage") {
    it("should pass matching json with different spacing and order") {
      val input = """
        |{
        | "some": "valid json",
        | "with": ["json", "content"]
        |}
      """.stripMargin

      val expected = """
                    |{
                    | "with": ["json", "content"],
                    |     "some":   "valid json"
                    |}
                  """.stripMargin

      input should matchJson(expected)
    }

    it("should fail on slightly different json explaining why") {
      val input = """
                    |{
                    | "someField": "valid json"
                    |}
                  """.stripMargin

      val expected = """
                       |{
                       | "someField": "different json"
                       |}
                     """.stripMargin

      input should matchJson(expected)
//
//      Fails
//
//      Json did not match {
//      "someField": "valid json"
//      } did not match {
//      "someField": "different json"
//      }
//
//      Json Diff:
//        someField
//      Expected: different json
//        got: valid json

    }
  }
}

```

Publishing
---
1) bump version in module to non snaphsot
2) commit and tag `git tag json4s-0.0.1` `git push --tags`
3) sbt +json4s/publishSigned
4) sbt sonatypeReleaseAll
4) bump version to snapshot
