package com.stephenn.scalatest.argonaut

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers
import argonaut._
import Argonaut._

class JsonMatchersSpec extends AnyFunSpec with Matchers {
  describe("JsonMatchers") {
    it("should pass when json is the same") {
      Seq("{}" -> "{}", "[]" -> "[]", "0" -> "0").foreach {
        case (left, right) =>
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
        "0" -> "0",
        """{"a":0, "b":1}""" -> """{"b":1,"a":0}"""
      ).foreach {
        case (left, right) =>
          val matchResult = JsonMatchers.matchJson(right).apply(left)
          matchResult.matches shouldBe true
      }
    }

    it("should fail when left has extra fields") {
      val matchResult = JsonMatchers.matchJson("""{"l":1}""").apply("{}")
      matchResult.matches shouldBe false
      matchResult.failureMessage shouldBe
        """
          |Json was not the equivalent. "{}" did not match "{"l":1}"
        """.stripMargin.trim
    }

    it("should fail when right has extra fields") {
      val matchResult = JsonMatchers.matchJson("{}").apply("""{"r":0}""")
      matchResult.matches shouldBe false
      matchResult.failureMessage shouldBe
        """
          |Json was not the equivalent. "{"r":0}" did not match "{}"
        """.stripMargin.trim
    }

    it("should fail on invalid json") {
      val matchResult =
        JsonMatchers.matchJson("{}").apply("""{"a": [1  "two"]}""")
      matchResult.matches shouldBe false
      matchResult.failureMessage shouldBe """Could not parse json "{"a": [1  "two"]}" error: "Expected entry separator token but found: "two"]}""""
    }
  }

  describe("golden test") {
    case class Foo(a: String)
    implicit val codec: CodecJson[Foo] = casecodec1(Foo.apply, Foo.unapply)("a")

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
      result.failureMessage shouldBe """Could not parse json string. "not a valid json string". ParsingFailure: "Unexpected content found: not a valid json string""""
    }

    it("should error if the json decodes but doesnt match the value") {
      val result = JsonMatchers
        .matchJsonGolden(""" {"a":"different value"} """.stripMargin)
        .apply(Foo("value"))
      result.matches shouldBe false
      result.failureMessage shouldBe
        """Json did not match "{"a":"different value"}" did not match "Foo(value)""""
    }
  }
}
