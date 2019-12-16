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
        s"""
          |Json was not the equivalent.
          |{
          |     "${green("l")}": ${green("1")}}
        """.stripMargin.trim
    }

    it("should fail when left has different value") {
      val matchResult =
        JsonMatchers.matchJson("""{"l":{"r":1}}""").apply("""{"l":{"r":2}}""")
      matchResult.matches shouldBe false
      matchResult.failureMessage shouldBe
        s"""
          |Json was not the equivalent.
          |{
          |     "l": {
          |          "r": ${red("2")} -> ${green("1")}}}
        """.stripMargin.trim
    }

    it("should fail when right has extra fields") {
      val matchResult = JsonMatchers.matchJson("{}").apply("""{"r":0}""")
      matchResult.matches shouldBe false
      matchResult.failureMessage shouldBe
        s"""
          |Json was not the equivalent.
          |{
          |     "${red("r")}": ${red("0")}}
        """.stripMargin.trim
    }

    it("should fail on invalid json") {
      val matchResult =
        JsonMatchers.matchJson("{}").apply("""{"a": [1  "two"]}""")
      matchResult.matches shouldBe false
      matchResult.failureMessage shouldBe """Could not parse json "{"a": [1  "two"]}" error: "Expected entry separator token but found: "two"]}""""
    }
  }

  describe("decodeTo") {
    case class Foo(a: String)
    implicit val codec: CodecJson[Foo] = casecodec1(Foo.apply, Foo.unapply)("a")

    it("should pass") {
      val result = JsonMatchers
        .decodeTo(Foo("value"))
        .apply(""" {"a":"value"} """)
      result.matches shouldBe true
    }

    it("should fail when invalid json") {
      val result = JsonMatchers
        .decodeTo(Foo("value"))
        .apply(""" not json """)
      result.matches shouldBe false
      result.failureMessage shouldBe """Could not parse json "not json" error: Left("Unexpected content found: not json ")"""
    }

    it("should fail when decodes to model but values are different") {
      val result = JsonMatchers
        .decodeTo(Foo("value"))
        .apply(""" {"a":"different"} """)
      result.matches shouldBe false
      result.failureMessage shouldBe "Values are not equal Foo(different) did not match Foo(value)"
    }
  }

  describe("golden test") {
    case class Foo(a: String)
    implicit val codec: CodecJson[Foo] = casecodec1(Foo.apply, Foo.unapply)("a")

    it("should pass a golden test") {
      val result = JsonMatchers
        .matchJsonGolden(Foo("value"))
        .apply(""" {"a":"value"} """)
      result.matches shouldBe true
    }

    it("should error if the json string isnt valid json") {
      val result = JsonMatchers
        .matchJsonGolden(Foo("value"))
        .apply("not a valid json string")
      result.matches shouldBe false
      result.failureMessage shouldBe """Could not parse json string. "not a valid json string". ParsingFailure: "Unexpected content found: not a valid json string""""
    }

    it("should error if the json decodes but doesnt match the value") {
      val result = JsonMatchers
        .matchJsonGolden(Foo("value"))
        .apply(""" {"a":"different value"} """)
      result.matches shouldBe false
      result.failureMessage shouldBe
        """Json did not match "{"a":"different value"}" did not match "Foo(value)""""
    }
  }

  def red(s: String): String = Console.RED + s + Console.RESET
  def green(s: String): String = Console.GREEN + s + Console.RESET
}
