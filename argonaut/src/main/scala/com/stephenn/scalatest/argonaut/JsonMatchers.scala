package com.stephenn.scalatest.argonaut

import org.scalatest.matchers.MatchResult
import org.scalatest.matchers.Matcher
import argonaut.Parse
import argonaut._
import Argonaut._

import scala.reflect.ClassTag

trait JsonMatchers {

  /**
    * Checks if the given json objects are equivalent.
    */
  def matchJson(right: String): Matcher[String] =
    Matcher[String] { left =>
      (Parse.parse(left), Parse.parse(right)) match {
        case (Left(err), _) =>
          MatchResult(
            matches = false,
            rawFailureMessage = "Could not parse json {0} error: {1}",
            rawNegatedFailureMessage = "Could not parse json {0} error: {1}",
            args = IndexedSeq(left.trim, err)
          )
        case (_, Left(err)) =>
          MatchResult(
            matches = false,
            rawFailureMessage = "Could not parse json {0} error: {1}",
            rawNegatedFailureMessage = "Could not parse json {0} error: {1}",
            args = IndexedSeq(right.trim, err)
          )
        case (Right(leftJs), Right(rightJs)) =>
          MatchResult(
            matches = leftJs == rightJs,
            rawFailureMessage =
              "Json was not the equivalent. {0} did not match {1}",
            rawNegatedFailureMessage =
              "Json was not the equivalent. {0} did not match {1}",
            args = IndexedSeq(leftJs.nospaces, rightJs.nospaces)
          )
      }
    }

  def matchJsonGolden[T: CodecJson: ClassTag](
      value: T
  ): Matcher[String] = {
    Matcher[String] { jsonString: String =>
      val valueAsJson = value.asJson

      Parse.parse(jsonString) match {
        case Left(err) =>
          MatchResult(
            matches = false,
            rawFailureMessage =
              "Could not parse json string. {0}. ParsingFailure: {1}",
            rawNegatedFailureMessage = "Json should not have matched {0} {1}",
            args = IndexedSeq(jsonString.trim, err)
          )
        case Right(json) =>
          valueAsJson == json match {
            case false =>
              MatchResult(
                matches = false,
                rawFailureMessage = "Json did not match {0} did not match {1}",
                rawNegatedFailureMessage =
                  "Json should not have matched {0} matched {1}",
                args = IndexedSeq(jsonString.trim, value.toString)
              )
            case true =>
              json.as[T].toEither match {
                case Left(value) =>
                  MatchResult(
                    matches = false,
                    rawFailureMessage =
                      "could not decode json to the model. error {0}",
                    rawNegatedFailureMessage =
                      "could not decode json to the model. error {0}",
                    args = IndexedSeq(jsonString.trim, value.toString)
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
