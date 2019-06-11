package com.stephenn.scalatest.json4s

import org.scalatest.matchers.{MatchResult, Matcher}
import org.json4s._
import org.json4s.native.JsonMethods._

trait JsonMatchers {

  /**
    * Checks if the given json objects are equivalent.
    */
  def matchJson(right: String): Matcher[String] = {
    Matcher[String] { left =>
      (parseOpt(left), parseOpt(right)) match {
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
  def matchJsonString(right: String): Matcher[JValue] = {
    Matcher[JValue] { left =>
      parseOpt(right) match {
        case None =>
          cantParseResult(compact(render(left)), right.trim)
        case Some(rightJson) =>
          matchJsonResult(compact(render(left)), right, left, rightJson)
      }
    }
  }

  private def matchJsonResult(left: String,
                              right: String,
                              leftJson: JValue,
                              rightJson: JValue) =
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

  private def diffMessage(left: JValue, right: JValue): String = {
    val Diff(c, a, d) = left diff right
    Seq(
      c.toSome.map(cc => s"Changed:\n${compact(render(cc))}"),
      a.toSome.map(aa => s"Added:\n${compact(render(aa))}"),
      d.toSome.map(dd => s"Removed:\n${compact(render(dd))}")
    ).flatten.mkString("\n")
  }
}

object JsonMatchers extends JsonMatchers
