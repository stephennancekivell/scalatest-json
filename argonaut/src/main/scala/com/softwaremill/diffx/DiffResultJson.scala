package com.softwaremill.diffx

case class DiffResultJson(fields: Map[DiffResult, DiffResult])
    extends DiffResultDifferent {
  override private[diffx] def showIndented(indent: Int): String = {
    val showFields =
      fields.map(
        f => s"""${i(indent)}"${f._1.show}": ${f._2.showIndented(indent + 5)}"""
      )
    showFields.mkString("{\n", ",\n", "}")
  }
}
