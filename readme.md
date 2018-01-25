Scalatest-json
===
[![Build Status](https://travis-ci.org/stephennancekivell/scalatest-json.svg?branch=master)](https://travis-ci.org/stephennancekivell/scalatest-json)

Scalatest matchers for Json with appropriate equality and descriptive error messages.

install
---

```
libraryDependencies ++= Seq(
    "com.stephenn" %% "scalatest-json-jsonassert" % "0.0.2"
)
```

usage
---

```
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
```