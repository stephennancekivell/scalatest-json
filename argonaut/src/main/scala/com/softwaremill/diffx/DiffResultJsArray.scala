package com.softwaremill.diffx

case class DiffResultJsArray(diffs: List[DiffResult])
    extends DiffResultDifferent {
  override private[diffx] def showIndented(indent: Int)(implicit c: ConsoleColorConfig): String = {
    val showFields = diffs.map(f => s"${f.showIndented(indent)}")
    showFields.mkString(s"[\n${i(indent)}", s",\n${i(indent)}", "]")
  }
}
