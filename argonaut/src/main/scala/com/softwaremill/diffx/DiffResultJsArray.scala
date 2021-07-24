package com.softwaremill.diffx

case class DiffResultJsArray(diffs: List[DiffResult]) extends DiffResult {
  override private[diffx] def showIndented(
      indent: Int,
      renderIdentical: Boolean
  )(implicit c: ConsoleColorConfig): String = {
    val showFields =
      diffs.map(f => s"${f.showIndented(indent, renderIdentical)}")
    showFields.mkString(s"[\n${i(indent)}", s",\n${i(indent)}", "]")
  }

  override def isIdentical: Boolean = diffs.forall(_.isIdentical)
}
