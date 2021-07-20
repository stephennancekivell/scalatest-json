package com.stephenn.scalatest.playjson

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class JsonMatchersSpec extends AnyFunSpec with Matchers {

  describe("JsonMatchers") {
    it("should pass when json is the same") {
      Seq("{}" -> "{}", "[]" -> "[]").foreach {
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
      matchResult.failureMessage.trim shouldBe
        """
          |Json did not match "{}" did not match "{"l":1}"
          |
          |Json Diff:
          |"JsonPatch(List(Add(Pointer(Chain(Left(l))),1)))"
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
          |"JsonPatch(List(Remove(Pointer(Chain(Left(r))),None)))"
        """.stripMargin.trim
    }

    it("should fail on invalid json") {
      val matchResult =
        JsonMatchers.matchJson("{}").apply("""{"a": nope]}""")
      matchResult.matches shouldBe false
      matchResult.failureMessage shouldBe "Could not parse json \"{\"a\": nope]}\" did not equal \"{}\""
    }

    it("should match a json object to a json string") {
      import play.api.libs.json._
      val matchResult = JsonMatchers
        .matchJsonString("""{"b":1,"a":0}""")
        .apply(Json.parse("""{"a":0, "b":1}"""))
      matchResult.matches shouldBe true
    }
  }
}
