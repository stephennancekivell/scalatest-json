package com.stephenn.scalatest.jsoniterscala

import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.{CodecMakerConfig, JsonCodecMaker}
import com.softwaremill.diffx.{Derived, Diff}
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class JsonMatchersSpec extends AnyFunSpec with Matchers {

  // The Jsoniter Scala parses JSON to concrete case classes representing them.
  // They are defined here for whole test. Further, we generate codecs using JsonCodecMaker macro.

  case class Root(items: Seq[Data])

  case class Data(a: Int, b: Option[Int])

  describe("JsonMatchers with implicit codec") {

    implicit val codec: JsonValueCodec[Data] =
      JsonCodecMaker.make(CodecMakerConfig.withSkipUnexpectedFields(false))

    it("should pass when JSON objects are the same") {
      Seq(
        """{"a":1}""" -> """{"a":1}""",
        """{"a":1,"b": 2}""" -> """{"a":1,"b":2}"""
      ).foreach {
        case (left, right) =>
          val matchResult = JsonMatchers.matchJson(right).apply(left)
          matchResult.matches shouldBe true
      }
    }

    it("should pass when another JSON object is equivalent") {
      Seq(
        // optional fields ("b" here), are ignored whether they are omitted or null
        """{"a":1}""" -> """{ "a" : 1 }""",
        """{"a":1}""" -> """{ "a" : 1, "b" : null }""",
        """{"a":0, "b":1}""" -> """{"b":1,"a":0}""",
        """{"b":1,"a":0}""" -> """{"a":0, "b":1}"""
      ).foreach {
        case (left, right) =>
          val matchResult = JsonMatchers.matchJson(right).apply(left)
          matchResult.matches shouldBe true
      }
    }

    it("should show deviation between two JSON objects") {
      val matchResult = JsonMatchers.matchJson(
        """{"b":2,"a":0}""").apply("""{"a":0, "b":1}""")
      matchResult.matches shouldBe false
      matchResult.failureMessage shouldBe
        s"""
          Json did not match.
           |Data(
           |     a: 0,
           |     b: ${red("1")}${red(" -> ")}${green("2")})
           |""".stripMargin.trim
    }

    it("should fail when left has extra fields") {
      val matchResult = JsonMatchers.matchJson("""{"a":1,"x":1}""").apply("""{"a":1}""")
      matchResult.matches shouldBe false
      matchResult.failureMessage shouldBe
        """
          |Could not parse json "{"a":1,"x":1}" error: "unexpected field "x", offset: 0x0000000a, buf:
          |+----------+-------------------------------------------------+------------------+
          ||          |  0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f | 0123456789abcdef |
          |+----------+-------------------------------------------------+------------------+
          || 00000000 | 7b 22 61 22 3a 31 2c 22 78 22 3a 31 7d          | {"a":1,"x":1}    |
          |+----------+-------------------------------------------------+------------------+"
          |""".stripMargin.trim
    }

    it("should fail when right has extra fields") {
      val matchResult = JsonMatchers.matchJson("""{"a":1}""").apply("""{"a":1,"x":1}""")
      matchResult.matches shouldBe false
      matchResult.failureMessage shouldBe
        """
          |Could not parse json "{"a":1,"x":1}" error: "unexpected field "x", offset: 0x0000000a, buf:
          |+----------+-------------------------------------------------+------------------+
          ||          |  0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f | 0123456789abcdef |
          |+----------+-------------------------------------------------+------------------+
          || 00000000 | 7b 22 61 22 3a 31 2c 22 78 22 3a 31 7d          | {"a":1,"x":1}    |
          |+----------+-------------------------------------------------+------------------+"
          |""".stripMargin.trim
    }

    it("should fail on invalid JSON Object") {
      val matchResult = JsonMatchers.matchJson("""{"a":1}""").apply("""{"a":nope}""")
      matchResult.matches shouldBe false
      matchResult.failureMessage shouldBe
        """Could not parse json "{"a":nope}" error: "illegal number, offset: 0x00000005, buf:
          |+----------+-------------------------------------------------+------------------+
          ||          |  0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f | 0123456789abcdef |
          |+----------+-------------------------------------------------+------------------+
          || 00000000 | 7b 22 61 22 3a 6e 6f 70 65 7d                   | {"a":nope}       |
          |+----------+-------------------------------------------------+------------------+"
          |""".stripMargin.trim
    }

  }

  describe("JsonMatchers with explicit codec") {

    // Note that we just generate a codec without any additional config parameters.
    // There is no make(CodecMakerConfig.withSkipUnexpectedFields(false)),
    // and the option is enabled by default.
    // Please find Scala Doc for more info on available options
    // https://github.com/plokhotnyuk/jsoniter-scala/blob/49aaf309e7f3d49d674522c1d1b0bce0b37bd4ac/jsoniter-scala-macros/shared/src/main/scala/com/github/plokhotnyuk/jsoniter_scala/macros/JsonCodecMaker.scala#L71
    implicit val codec2: JsonValueCodec[Data] = JsonCodecMaker.make
    implicit val codec1: JsonValueCodec[Root] = JsonCodecMaker.make
    implicit val codec3: JsonValueCodec[List[Data]] = JsonCodecMaker.make

    it("should pass when JSON objects are the same") {

      implicit val diffData2: Diff[Root] = Derived[Diff[Root]]

      Seq(
        """{"items": [{"a":1}]}""" -> """{"items": [{"a":1}]}""",
        """{ "items": [ { "a":1,"b": 2 } ] }""" -> """{"items": [{"a":1,"b":2}]}"""
      ).foreach {
        case (left, right) =>
          // note that explicit [Root] is for binding with particular codec explicitly
          val matchResult = JsonMatchers.matchJson[Root](right).apply(left)
          matchResult.matches shouldBe true
      }
    }

    it("should pass even left has extra field, but skipUnexpectedFields config param is disabled") {
      // note that explicit [Data] is for binding with particular codec explicitly
      val matchResult = JsonMatchers.matchJson[Data](
        """{"a":1,"x":1}""").apply("""{"a":1}""")
      matchResult.matches shouldBe true
    }

    it("should pass when another JSON object is equivalent") {
      Seq(
        """[{"a":1}]""" -> """[ { "a" : 1 } ]""",
        """[]""" -> """[ ]"""
      ).foreach {
        case (left, right) =>
          // note that explicit [List[Data]] is for binding with particular codec explicitly
          val matchResult = JsonMatchers.matchJson[List[Data]](right).apply(left)
          matchResult.matches shouldBe true
      }
    }

  }

  def red(s: String): String = s"${Console.RED}$s${Console.RESET}"

  def green(s: String): String = s"${Console.GREEN}$s${Console.RESET}"
}
