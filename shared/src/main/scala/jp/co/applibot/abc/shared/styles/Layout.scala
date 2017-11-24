package jp.co.applibot.abc.shared.styles

import scalacss.DevDefaults._

object Layout extends StyleSheet.Inline {
  import dsl._

  private val headerHeight = 80
  private val max = 100.%%
  private val logoSize = 64.px

  val container = style(
    display.flex,
    flexDirection.column,
    borderWidth(2.px, 0.px, 0.px, 0.px),
    borderStyle.solid,
    borderColor(Color(Colors.lightBlue500)),
    width(max),
    minWidth(768.px),
    height(max),
    backgroundColor(Color(Colors.grey50)),
  )

  val header = style(
    flex := s"0 0 ${headerHeight}px",
    backgroundColor(Color(Colors.white)),
    width(max),
    textAlign.center,
  )

  val logo = style(
    margin(8.px, 0.px),
    maxWidth(logoSize),
    maxHeight(logoSize),
  )

  val logoActive = style(cursor.pointer)

  val content = style(
    display.flex,
    flex := "1 0 0",
    width(max),
  )
}
