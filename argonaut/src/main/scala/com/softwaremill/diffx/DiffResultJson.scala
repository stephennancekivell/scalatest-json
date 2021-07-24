package com.softwaremill.diffx

case class DiffResultJson(fields: Map[DiffResult, DiffResult])
    extends DiffResult {
  override private[diffx] def showIndented(
      indent: Int,
      renderIdentical: Boolean
  )(implicit c: ConsoleColorConfig): String = {
    val showFields =
      fields.map(f =>
        s"""${i(indent)}"${f._1
          .show()}": ${f._2.showIndented(indent + 5, renderIdentical)}"""
      )
    showFields.mkString("{\n", ",\n", "}")
  }

  override def isIdentical: Boolean = fields.forall { case (k, v) =>
    k.isIdentical && v.isIdentical
  }
}
