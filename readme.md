Scalatest-json
===
[![Build Status](https://travis-ci.org/stephennancekivell/scalatest-json.svg?branch=master)](https://travis-ci.org/stephennancekivell/scalatest-json)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.stephenn/scalatest-argonaut_2.13/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.stephenn/scalatest-argonaut_2.13)


Scalatest matchers for Json with appropriate equality and descriptive error messages.

Install
---

Add the dependency you want
```sbt
libraryDependencies += "com.stephenn" %% "scalatest-json-jsonassert" % "0.2.0"
libraryDependencies += "com.stephenn" %% "scalatest-json4s" % "0.2.0"
libraryDependencies += "com.stephenn" %% "scalatest-play-json" % "0.2.0"
libraryDependencies += "com.stephenn" %% "scalatest-circe" % "0.2.0"
libraryDependencies += "com.stephenn" %% "scalatest-argonaut" % "0.2.0"
libraryDependencies += "com.stephenn" %% "scalatest-jsoniter-scala" % "0.2.0"
```

You can also check the latest version on [maven central](https://search.maven.org/search?q=com.stephenn%20scalatest).

Usage
---
Your scalatest spec should extend or import `JsonMatchers`

```scala
class ExampleSpec extends FunSpec with Matchers with JsonMatchers {

  describe("JsonMatchers usage") {
    it("should pass matching json with different spacing and order") {
      val input = """
        |{
        | "some": "valid json",
        | "plus": ["json", "content"]
        |}
      """.stripMargin

      val expected = """
                    |{
                    | "plus": ["json", "content"],
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

For **[Jsoniter Scala](https://github.com/plokhotnyuk/jsoniter-scala)** the data structures and corresponding codec shall be defined, eg.:

```scala
  import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
  import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker

  case class Data(some: String, plus: Seq[String])
  
  implicit val codec: JsonValueCodec[Data] = JsonCodecMaker.make
```

Please find a complete example [here](jsoniter-scala/src/test/scala/com/stephenn/scalatest/jsoniterscala/ExampleSpec.scala).

Golden Test
---
For the circe module you write a golden test that encodes and decodes a model, checking that it matches a golden example json.
```scala
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
Done from github actions using [sbt-ci-release](https://github.com/olafurpg/sbt-ci-release)

eg
```
git tag -a v0.2.3 -m "v0.2.3"
git push origin v0.2.3
```