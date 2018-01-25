package com.stephenn.scalatest.jsonassert

import org.scalatest.{FunSpec, Matchers}

class JsonMatchersSpec extends FunSpec with Matchers {
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

      Seq("{}" -> " { }",
          " [ ] " -> "[]",
          "0" -> "0",
          """{"a":0, "b":1}""" -> """{"b":1,"a":0}""").foreach {
        case (left, right) =>
          val matchResult = JsonMatchers.matchJson(right).apply(left)
          matchResult.matches shouldBe true
      }
    }

    it("should fail when left has extra fields") {
      val matchResult = JsonMatchers.matchJson("""{"l":1}""").apply("{}")
      matchResult.matches shouldBe false
      matchResult.failureMessage shouldBe "Json did not match {} did not match {\"l\":1}\n\nJson Diff:\n\nExpected: l\n     but none found\n"
    }

    it("should fail when right has extra fields") {
      val matchResult = JsonMatchers.matchJson("{}").apply("""{"r":0}""")
      matchResult.matches shouldBe false
      matchResult.failureMessage shouldBe "Json did not match {\"r\":0} did not match {}\n\nJson Diff:\n\nUnexpected: r\n"
    }

    it("should fail on invalid json") {
      val matchResult =
        JsonMatchers.matchJson("{}").apply("""{"a": [1  "two"]}""")
      matchResult.matches shouldBe false
      matchResult.failureMessage shouldBe "Couldnt parse json {\"a\": [1  \"two\"]} did not equal {}"
    }
  }
}
