package com.stephenn.scalatest.argonaut

import org.scalatest.matchers.MatchResult
import org.scalatest.matchers.Matcher
import argonaut.Parse
import argonaut._
import Argonaut._

import scala.reflect.ClassTag
import com.softwaremill.diffx._

import scala.collection.immutable

trait JsonMatchers {

  private implicit val diffJson: Diff[Json] = new Diff[Json] {
    def apply(
      left: Json,
      right: Json,
      toIgnore: List[_root_.com.softwaremill.diffx.FieldPath]
    ): DiffResult = {

      def diffPrimative[A](leftA: Option[A],
                           rightA: Option[A]): Option[DiffResult] =
        diffWith(leftA, rightA) { (leftValue, rightValue) =>
          if (leftValue == rightValue)
            Identical(leftValue)
          else
            DiffResultValue(leftValue, rightValue)
        }

      def diffWith[A](leftA: Option[A], rightA: Option[A])(
        fn: (A, A) => DiffResult
      ): Option[DiffResult] = {
        for {
          leftValue <- leftA
          rightValue <- rightA
        } yield {
          fn(leftValue, rightValue)
        }
      }

      diffPrimative(left.string, right.string)
        .orElse(diffWith(left.number, right.number) { (leftValue, rightValue) =>
          if (leftValue == rightValue)
            Identical(leftValue.asJson.nospaces)
          else
            DiffResultValue(
              leftValue.asJson.nospaces,
              rightValue.asJson.nospaces
            )
        })
        .orElse(diffPrimative(left.bool, right.bool))
        .orElse({
          if (left.isNull && right.isNull)
            Some(Identical(left))
          else
            None
        })
        .orElse(diffWith(left.array, right.array) { (leftArr, rightArr) =>
          val zipped: immutable.Seq[(Json, Json)] = leftArr.zip(rightArr)
          val diffs: immutable.Seq[DiffResult] = zipped.map {
            case (a, b) =>
              val d: DiffResult = diffJson(a, b)
              d
          }

          val missing = leftArr.drop(rightArr.size).map(DiffResultMissing.apply)
          val additional =
            rightArr.drop(leftArr.size).map(DiffResultAdditional.apply)

          val all = (diffs ++ missing ++ additional).toList

          if (all.forall(_.isIdentical)) {
            Identical(all)
          } else {
            DiffResultSet(all)
          }
        })
        .orElse(diffWith(left.obj, right.obj) {
          case (leftObj, rightObj) =>
            val leftMap = leftObj.toMap
            val m: List[(DiffResult, DiffResult)] = leftObj.toList.map {
              case (key, leftValue) =>
                rightObj(key) match {
                  case Some(rightValue) =>
                    val d: DiffResult = diffJson(leftValue, rightValue)
                    Identical(key) -> d
                  case None =>
                    DiffResultMissing(key) -> DiffResultMissing(leftValue)
                }
            }

            val rightKeys = rightObj.toMap.keySet
            val leftKeys = leftMap.keySet
            val additional: List[(DiffResult, DiffResult)] = rightKeys.toList
              .filterNot(leftKeys.contains)
              .flatMap(
                k =>
                  rightObj(k).map(
                    v => DiffResultAdditional(k) -> DiffResultAdditional(v)
                )
              )

            val all = m ++ additional
            if (all.forall { case (k, v) => k.isIdentical && v.isIdentical }) {
              Identical(leftObj)
            } else {
              DiffResultJson(all.toMap)
            }
        })
        .getOrElse(DiffResultValue(left, right))
    }
  }

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
          val diffResult = Diff.compare(leftJs, rightJs)
          val s = diffResult.show
          MatchResult(
            matches = diffResult.isIdentical,
            rawFailureMessage = "Json was not the equivalent.\n" + s,
            rawNegatedFailureMessage = "Json was not the equivalent.\n" + s
          )
      }
    }

  def decodeTo[T: DecodeJson: ClassTag](right: T): Matcher[String] =
    Matcher[String] { string =>
      string.decode[T] match {
        case Left(error) =>
          MatchResult(
            matches = false,
            rawFailureMessage = "Could not parse json {0} error: {1}",
            rawNegatedFailureMessage = "Could not parse json {0} error: {1}",
            args = IndexedSeq(string.trim, error)
          )
        case Right(value) =>
          MatchResult(
            matches = value == right,
            rawFailureMessage = "Values are not equal {0} did not match {1}",
            rawNegatedFailureMessage =
              "Values are not equal {0} did not match {1}",
            args = IndexedSeq(value, right)
          )
      }
    }

  def matchJsonGolden[T: EncodeJson: DecodeJson: ClassTag](
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
