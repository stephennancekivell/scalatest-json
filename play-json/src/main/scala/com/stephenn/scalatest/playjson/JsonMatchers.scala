package com.stephenn.scalatest.playjson

import org.scalatest.matchers.{MatchResult, Matcher}
import play.api.libs.json._

import scala.util.Try

trait JsonMatchers {

  /**
    * Checks if the given json objects are equivalent.
    */
  def matchJson(right: String): Matcher[String] = {
    Matcher[String] { left =>
      (Try(Json.parse(left)).toOption, Try(Json.parse(right)).toOption) match {
        case (Some(leftJson), Some(rightJson)) =>
          matchJsonResult(left, right, leftJson, rightJson)
        case _ =>
          cantParseResult(left, right)
      }
    }
  }

  /**
    * Checks if the given json objects are equivalent.
    */
  def matchJsonString(right: String): Matcher[JsValue] = {
    Matcher[JsValue] { left =>
      Try(Json.parse(right)).toOption match {
        case None =>
          cantParseResult(Json.stringify(left), right.trim)
        case Some(rightJson) =>
          matchJsonResult(Json.stringify(left), right, left, rightJson)
      }
    }
  }

  private def matchJsonResult(left: String,
                              right: String,
                              leftJson: JsValue,
                              rightJson: JsValue) =
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

  private def diffMessage(left: JsValue, right: JsValue): String = {
    import gnieh.diffson.playJson._

    JsonDiff.diff(left, right, remember = false).toString()
  }
}

object JsonMatchers extends JsonMatchers
