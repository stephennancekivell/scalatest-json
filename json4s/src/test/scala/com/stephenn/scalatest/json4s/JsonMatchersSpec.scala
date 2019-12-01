package com.stephenn.scalatest.json4s

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

      Seq("{}" -> " { }",
          " [ ] " -> "[]",
          """{"a":0, "b":1}""" -> """{"b":1,"a":0}""").foreach {
        case (left, right) =>
          val matchResult = JsonMatchers.matchJson(right).apply(left)
          matchResult.matches shouldBe true
      }
    }

    it("should fail when left has extra fields") {
      val matchResult = JsonMatchers.matchJson("""{"l":1}""").apply("{}")
      matchResult.matches shouldBe false
      matchResult.failureMessage shouldBe "Json did not match \"{}\" did not match \"{\"l\":1}\"\n\nJson Diff:\n\"Added:\n{\"l\":1}\""
    }

    it("should fail when right has extra fields") {
      val matchResult = JsonMatchers.matchJson("{}").apply("""{"r":0}""")
      matchResult.matches shouldBe false
      matchResult.failureMessage shouldBe "Json did not match \"{\"r\":0}\" did not match \"{}\"\n\nJson Diff:\n\"Removed:\n{\"r\":0}\""
    }

    it("should fail on invalid json") {
      val matchResult =
        JsonMatchers.matchJson("{}").apply("""{"a": nope]}""")
      matchResult.matches shouldBe false
      matchResult.failureMessage shouldBe "Could not parse json \"{\"a\": nope]}\" did not equal \"{}\""
    }

    it("should match a json object to a json string") {
      import org.json4s.native.JsonMethods._
      val matchResult = JsonMatchers
        .matchJsonString("""{"b":1,"a":0}""")
        .apply(parse("""{"a":0, "b":1}"""))
      matchResult.matches shouldBe true
    }
  }
}
