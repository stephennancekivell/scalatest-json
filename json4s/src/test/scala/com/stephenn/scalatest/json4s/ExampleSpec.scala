package com.stephenn.scalatest.json4s

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class ExampleSpec extends AnyFunSpec with Matchers with JsonMatchers {

  ignore("JsonMatchers usage") {
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
      //        Changed:
      //      {"someField":"different json"}

    }

    it("should fail on very different json explaining why") {
      val input = """
                    |{
                    | "someField": "valid json",
                    | "otherField": ["json", "content"]
                    |}
                  """.stripMargin

      val expected = """
                       |{
                       | "someField": "different json",
                       | "otherField": ["json", "stuff", "changes"]
                       |}
                     """.stripMargin

      input should matchJson(expected)
    }
  }
}
