package com.stephenn.scalatest.jsoniterscala

import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class ExampleSpec extends AnyFunSpec with Matchers with JsonMatchers {

  case class Data(some: String, plus: Seq[String])
  case class OtherData(someField: String, otherField: Seq[String])

  implicit val codec: JsonValueCodec[Data] = JsonCodecMaker.make

  // Please un-ignore test to see it in action
  ignore("Jsoniter Scala JsonMatchers usage") {

    it("should pass matching json with different spacing and order") {
      val input =
        """
          |{
          | "some": "valid json",
          | "plus": ["json", "content"]
          |}
        """.stripMargin

      val expected =
        """
          |{
          | "plus": ["json", "content"],
          |     "some":   "valid json"
          |}
        """.stripMargin

      input should matchJson(expected)
    }

    it("should fail on slightly different json explaining why") {
      val input =
        """
          |{
          | "some": "valid json"
          |}
        """.stripMargin

      val expected =
        """
          |{
          | "some": "different json"
          |}
        """.stripMargin

      input should matchJson(expected)

      //      Fails:
      //
      //      Json did not match.
      //      Data(
      //        some: valid json -> different json,
      //        plus: List())

    }

    it("should fail on very different json explaining why") {
      implicit val codec: JsonValueCodec[OtherData] = JsonCodecMaker.make

      val input =
        """
          |{
          | "someField": "valid json",
          | "otherField": ["json", "content"]
          |}
        """.stripMargin

      val expected =
        """
          |{
          | "someField": "different json",
          | "otherField": ["json", "stuff", "changes"]
          |}
        """.stripMargin

      input should matchJson[OtherData](expected)
    }

  }
}
