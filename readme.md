Scalatest-json
===
[![Build Status](https://travis-ci.org/stephennancekivell/scalatest-json.svg?branch=master)](https://travis-ci.org/stephennancekivell/scalatest-json)

Scalatest matchers for Json with appropriate equality and descriptive error messages.

install
---

Add the dependency you want
```
libraryDependencies += "com.stephenn" %% "scalatest-json-jsonassert" % "0.0.5"
libraryDependencies += "com.stephenn" %% "scalatest-json4s" % "0.0.4"
libraryDependencies += "com.stephenn" %% "scalatest-play-json" % "0.0.3"
libraryDependencies += "com.stephenn" %% "scalatest-circe" % "0.0.4"
libraryDependencies += "com.stephenn" %% "scalatest-argonaut" % "0.0.2"
```

You can also check the latest version on [maven central](https://search.maven.org/search?q=com.stephenn%20scalatest).

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

Golden Test
---
For the circe module you write a golden test that encodes and decodes a model, checking that it matches a golden example json. 
```
it("can compare json and a model") {
    case class Foo(sameField: String)
    implicit val encoder = Encoder.forProduct1("someField")(Foo.unapply)
    implicit val decoder = Decoder.forProduct1("someField")(Foo.apply)
    
    val json = """
                |{
                | "someField": "valid json"
                |}
              """.stripMargin
    
    val model = Foo("valid json")
    
    model should matchJsonGolden(json)
}
```

Publishing
---
* bump version to non snaphsot
* commit and tag `git tag scalatest-json-0.1.0` `git push --tags`
* export GPG_TTY=$(tty)
* sbt +publishSigned
* sbt sonatypeReleaseAll
* bump version to snapshot
