package com.stephenn.scalatest.circe

import io.circe.{Decoder, Encoder}
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class JsonMatchersSpec extends AnyFunSpec with Matchers {

  describe("JsonMatchers") {
    it("should pass when json is the same") {
      Seq("{}" -> "{}", "[]" -> "[]").foreach { case (left, right) =>
        val matchResult = JsonMatchers.matchJson(right).apply(left)
        matchResult.matches shouldBe true
      }
    }

    it("should pass when json is equivalent") {
      val matchResult = JsonMatchers.matchJson("{  }").apply("  {}  ")
      matchResult.matches shouldBe true

      Seq(
        "{}" -> " { }",
        " [ ] " -> "[]",
        """{"a":0, "b":1}""" -> """{"b":1,"a":0}"""
      ).foreach { case (left, right) =>
        val matchResult = JsonMatchers.matchJson(right).apply(left)
        matchResult.matches shouldBe true
      }
    }

    it("should fail when left has extra fields") {
      val matchResult = JsonMatchers.matchJson("""{"l":1}""").apply("{}")
      matchResult.matches shouldBe false
      matchResult.failureMessage.trim shouldBe
        """
          |Json did not match "{}" did not match "{"l":1}"
          |
          |Json Diff:
          |"[
          |  {
          |    "op" : "add",
          |    "path" : "/l",
          |    "value" : 1
          |  }
          |]"
        """.stripMargin.trim
    }

    it("should fail when right has extra fields") {
      val matchResult = JsonMatchers.matchJson("{}").apply("""{"r":0}""")
      matchResult.matches shouldBe false
      matchResult.failureMessage.trim shouldBe
        """
          |Json did not match "{"r":0}" did not match "{}"
          |
          |Json Diff:
          |"[
          |  {
          |    "op" : "remove",
          |    "path" : "/r"
          |  }
          |]"
        """.stripMargin.trim
    }

    it("should fail on invalid json") {
      val matchResult =
        JsonMatchers.matchJson("{}").apply("""{"a": nope]}""")
      matchResult.matches shouldBe false
      matchResult.failureMessage shouldBe "Could not parse json \"{\"a\": nope]}\" did not equal \"{}\""
    }

    it("should match a json object to a json string") {
      import io.circe.parser._
      val matchResult = JsonMatchers
        .matchJsonString("""{"b":1,"a":0}""")
        .apply(parse("""{"a":0, "b":1}""").toOption.get)
      matchResult.matches shouldBe true
    }
  }

  describe("golden test") {
    case class Foo(a: String)
    implicit val encoder: Encoder[Foo] =
      Encoder.forProduct1("a")(_.a)
    implicit val decoder: Decoder[Foo] = Decoder.forProduct1("a")(a => Foo(a))

    it("should pass a golden test") {
      val result = JsonMatchers
        .matchJsonGolden(""" {"a":"value"} """.stripMargin)
        .apply(Foo("value"))
      result.matches shouldBe true
    }

    it("should error if the json string isnt valid json") {
      val result = JsonMatchers
        .matchJsonGolden("not a valid json string")
        .apply(Foo("value"))
      result.matches shouldBe false
      result.failureMessage shouldBe """Could not parse json string. "not a valid json string". ParsingFailure: "expected null got 'not a ...' (line 1, column 1)""""
    }

    it("should error if the json decodes but doesnt match the value") {
      val result = JsonMatchers
        .matchJsonGolden(""" {"a":"different value"} """.stripMargin)
        .apply(Foo("value"))
      result.matches shouldBe false
      result.failureMessage shouldBe
        """Json did not match "{"a":"different value"}" did not match "Foo(value)"
          |
          |Json Diff:
          |"[
          |  {
          |    "op" : "replace",
          |    "path" : "/a",
          |    "value" : "value"
          |  }
          |]"""".stripMargin
    }

    it("should error if the json encoded value doesnt match the json") {
      case class Bar(a: String)
      case class B(a: String, b: String)

      implicit val encoder: Encoder[Bar] =
        Encoder.forProduct1("encodedA")(_.a)
      implicit val decoder: Decoder[Bar] = Decoder.forProduct1("a")(a => Bar(a))

      val result = JsonMatchers
        .matchJsonGolden(""" {"a":"value"} """.stripMargin)
        .apply(Bar("value"))
      result.matches shouldBe false
      result.failureMessage shouldBe
        """Json did not match "{"a":"value"}" did not match "Bar(value)"
          |
          |Json Diff:
          |"[
          |  {
          |    "op" : "remove",
          |    "path" : "/a"
          |  },
          |  {
          |    "op" : "add",
          |    "path" : "/encodedA",
          |    "value" : "value"
          |  }
          |]"""".stripMargin
    }
  }
}
