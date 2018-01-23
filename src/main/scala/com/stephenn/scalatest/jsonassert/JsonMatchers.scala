package com.stephenn.scalatest.jsonassert

import org.scalactic.Prettifier
import org.scalatest.matchers.MatchResult
import org.skyscreamer.jsonassert.{JSONCompare, JSONCompareMode}

import scala.util.{Failure, Success, Try}
import org.scalatest.matchers.Matcher

trait JsonMatchers {

  /**
    * A matcher that checks whether the given object is or can be parsed into a json value.
    */
  def matchJson(right: String): Matcher[String] =
    Matcher[String] { left =>
      Try(
        JSONCompare
          .compareJSON(right, left, JSONCompareMode.STRICT)
      ) match {
        case Failure(_) =>
          MatchResult(
            matches = false,
            rawFailureMessage = "Couldnt parse json {0} did not equal {1}",
            rawNegatedFailureMessage = "Json should not have matched {0} {1}",
            args = Array(left.trim, right.trim)
          ).copy(prettifier = noopPrettifier)
        case Success(jSONCompareResult) =>
          MatchResult(
            matches = jSONCompareResult.passed(),
            rawFailureMessage =
              "Json did not match {0} did not match {1}\n\nJson Diff:\n{2}",
            rawNegatedFailureMessage =
              "Json should not have matched {0} matched {1}\n\nJson Diff:\n{2}",
            args = Array(left.trim, right.trim, jSONCompareResult.getMessage)
          ).copy(prettifier = noopPrettifier)
      }
    }

  private val noopPrettifier: Prettifier =
    new Prettifier {
      override def apply(v1: Any): String = v1.toString
    }
}

object JsonMatchers extends JsonMatchers
