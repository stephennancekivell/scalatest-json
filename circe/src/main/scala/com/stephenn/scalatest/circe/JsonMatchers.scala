package com.stephenn.scalatest.circe

import org.scalatest.matchers.{MatchResult, Matcher}

import io.circe._
import io.circe.parser._
import io.circe.syntax._

import scala.reflect.ClassTag

trait JsonMatchers {

  /** Checks if the given json objects are equivalent.
    */
  def matchJson(right: String): Matcher[String] = {
    Matcher[String] { left =>
      (parse(left), parse(right)) match {
        case (Right(leftJson), Right(rightJson)) =>
          matchJsonResult(left, right, leftJson, rightJson)
        case _ =>
          cantParseResult(left, right)
      }
    }
  }

  /** Checks if the given json objects are equivalent.
    */
  def matchJsonString(right: String): Matcher[Json] = {
    Matcher[Json] { left =>
      parse(right) match {
        case Right(rightJson) =>
          matchJsonResult(left.spaces2, right, left, rightJson)
        case _ =>
          cantParseResult(left.spaces2, right.trim)
      }
    }
  }

  private def matchJsonResult(
      left: String,
      right: String,
      leftJson: Json,
      rightJson: Json
  ) =
    MatchResult(
      matches = leftJson == rightJson,
      rawFailureMessage =
        "Json did not match {0} did not match {1}\n\nJson Diff:\n{2}",
      rawNegatedFailureMessage =
        "Json should not have matched {0} matched {1}\n\nJson Diff:\n{2}",
      args = IndexedSeq(left.trim, right.trim, diffMessage(leftJson, rightJson))
    )

  private def cantParseResult(left: String, right: String) = MatchResult(
    matches = false,
    rawFailureMessage = "Could not parse json {0} did not equal {1}",
    rawNegatedFailureMessage = "Json should not have matched {0} {1}",
    args = IndexedSeq(left.trim, right.trim)
  )

  private def diffMessage(left: Json, right: Json): String = {
    import diffson._
    import diffson.lcs._
    import diffson.circe._
    import diffson.jsonpatch.lcsdiff._
    import io.circe._
    import io.circe.syntax._

    implicit val lcs: Patience[Json] = new Patience[Json]

    diff(left, right).asJson.toString
  }

  def matchJsonGolden[T: Encoder: Decoder: ClassTag](
      jsonString: String
  ): Matcher[T] = {
    Matcher[T] { value =>
      val valueAsJson = value.asJson

      parse(jsonString) match {
        case Left(err) =>
          MatchResult(
            matches = false,
            rawFailureMessage =
              "Could not parse json string. {0}. ParsingFailure: {1}",
            rawNegatedFailureMessage = "Json should not have matched {0} {1}",
            args = IndexedSeq(jsonString.trim, err.message)
          )
        case Right(json) =>
          valueAsJson == json match {
            case false =>
              MatchResult(
                matches = false,
                rawFailureMessage =
                  "Json did not match {0} did not match {1}\n\nJson Diff:\n{2}",
                rawNegatedFailureMessage =
                  "Json should not have matched {0} matched {1}\n\nJson Diff:\n{2}",
                args = IndexedSeq(
                  jsonString.trim,
                  value.toString,
                  diffMessage(json, valueAsJson)
                )
              )
            case true =>
              json.as[T] match {
                case Left(value) =>
                  MatchResult(
                    matches = false,
                    rawFailureMessage =
                      "could not decode json to the model. error {0}",
                    rawNegatedFailureMessage =
                      "could not decode json to the model. error {0}",
                    args = IndexedSeq(
                      jsonString.trim,
                      value.toString,
                      diffMessage(json, valueAsJson)
                    )
                  )
                case Right(decodedValue) =>
                  MatchResult(
                    decodedValue == value,
                    rawFailureMessage =
                      "The encoded and decoded models do not match. {0} {1}",
                    rawNegatedFailureMessage =
                      "The encoded and decoded models do not match. {0} {1}",
                    args = IndexedSeq(decodedValue, value)
                  )
              }
          }
      }
    }
  }
}

object JsonMatchers extends JsonMatchers
