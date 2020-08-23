package com.stephenn.scalatest.jsoniterscala

import com.github.plokhotnyuk.jsoniter_scala.core.{JsonValueCodec, _}
import com.softwaremill.diffx.Diff
import org.scalatest.matchers.{MatchResult, Matcher}

import scala.reflect.ClassTag
import scala.util.{Failure, Success, Try}

trait JsonMatchers {

  /**
    * Checks if the given JSON objects are equivalent.
    */
  def matchJson[T: JsonValueCodec : ClassTag](expected: String)(implicit diffInst: Diff[T]): Matcher[String] = {
    Matcher[String] { actual =>
      (parse(actual), parse(expected)) match {
        case (Left(error), _) => parseError(actual.trim, error.getMessage)
        case (_, Left(error)) => parseError(expected.trim, error.getMessage)
        case (Right(actual), Right(expected)) => comparisonResult(actual, expected)
      }
    }
  }

  /**
    * TODO: to handle cases with JSON as only "[]" use scanJsonArrayFromStream
    * see: https://github.com/plokhotnyuk/jsoniter-scala/blob/140fbcb74634aaa3b5371fe95e27bcf7fca85f3b/jsoniter-scala-core/shared/src/test/scala/com/github/plokhotnyuk/jsoniter_scala/core/PackageSpec.scala#L241
    * It will take also "null"
    */
  private def parse[T: JsonValueCodec : ClassTag](json: String): Either[Throwable, T] = {
    Try(readFromArray(json.getBytes("UTF-8"))) match {
      case Success(parsedData) => Right(parsedData)
      case Failure(error) => Left(error)
    }
  }

  private def parseError[T: JsonValueCodec : ClassTag](left: String, right: String) = {
    MatchResult(
      matches = false,
      rawFailureMessage = "Could not parse json {0} error: {1}",
      rawNegatedFailureMessage = "Could not parse json {0} error: {1}",
      args = IndexedSeq(left, right)
    )
  }

  private def comparisonResult[T: JsonValueCodec : ClassTag](actual: T, expected: T)(implicit diffInst: Diff[T]) = {
    val diffResult = Diff.compare(actual, expected)(diffInst)
    val s = diffResult.show
    MatchResult(
      matches = diffResult.isIdentical,
      rawFailureMessage = "Json did not match.\n" + s,
      rawNegatedFailureMessage = "Json did not match.\n" + s
    )
  }

}

object JsonMatchers extends JsonMatchers
