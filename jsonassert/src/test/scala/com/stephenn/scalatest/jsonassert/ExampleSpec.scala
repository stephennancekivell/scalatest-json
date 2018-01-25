package com.stephenn.scalatest.jsonassert

import org.scalatest.{FunSpec, Matchers}

class ExampleSpec extends FunSpec with Matchers with JsonMatchers {

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
//        someField
//      Expected: different json
//        got: valid json

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
