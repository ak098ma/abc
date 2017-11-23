package jp.co.applibot.abc.shared.styles

import scalacss.DevDefaults._

object Layout extends StyleSheet.Inline {
  import dsl._

  private val headerHeight = 80

  val container = style(
    borderWidth(2 px, 0 px, 0 px, 0 px),
    borderStyle.solid,
    borderColor(Color(Colors.lightBlue500)),
    width(100 %%),
    height(100 %%),
    backgroundColor(Color(Colors.grey50)),
  )

  val header = style(
    position.fixed,
    backgroundColor(Color(Colors.white)),
    width(100 %%),
    height(headerHeight px),
    textAlign.center,
    fontSize(24 px),
  )

  val logo = style(
    margin(8 px, 0 px),
    maxWidth(64 px),
    maxHeight(64 px),
  )

  val logoActive = style(cursor.pointer)

  val content = style(
    paddingTop(headerHeight px),
    width(100 %%),
    height(100 %%),
  )
}
