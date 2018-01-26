package com.stephenn.scalatest.circe

import org.scalatest.matchers.{MatchResult, Matcher}
import io.circe._
import io.circe.parser._

trait JsonMatchers {

  /**
    * Checks if the given json objects are equivalent.
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

  /**
    * Checks if the given json objects are equivalent.
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

  private def matchJsonResult(left: String,
                              right: String,
                              leftJson: Json,
                              rightJson: Json) =
    MatchResult(
      matches = leftJson == rightJson,
      rawFailureMessage =
        "Json did not match {0} did not match {1}\n\nJson Diff:\n{2}",
      rawNegatedFailureMessage =
        "Json should not have matched {0} matched {1}\n\nJson Diff:\n{2}",
      args = Array(left.trim, right.trim, diffMessage(leftJson, rightJson))
    )

  private def cantParseResult(left: String, right: String) = MatchResult(
    matches = false,
    rawFailureMessage = "Could not parse json {0} did not equal {1}",
    rawNegatedFailureMessage = "Json should not have matched {0} {1}",
    args = Array(left.trim, right.trim)
  )

  private def diffMessage(left: Json, right: Json): String = {
    import gnieh.diffson.circe._

    JsonDiff.diff(left, right, remember = false).toString()
  }
}

object JsonMatchers extends JsonMatchers
