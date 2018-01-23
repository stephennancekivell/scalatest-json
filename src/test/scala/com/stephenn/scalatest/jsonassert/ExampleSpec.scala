package com.stephenn.scalatest.jsonassert

import org.scalatest.{FunSpec, ShouldMatchers}

class ExampleSpec extends FunSpec with ShouldMatchers with JsonMatchers {

  ignore("JsonMatchers usage") {
    it("should pass matching json") {
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

    it("test some json output") {
      val json: String = ???

      json should matchJson("""
          |{
          |   "foo": "bar",
          |  "baz":["bee","boo"]
          |}
        """.stripMargin)
    }
  }
}
