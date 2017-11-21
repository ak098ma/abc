package jp.co.applibot.abc.shared.styles

import scala.concurrent.duration._
import scalacss.DevDefaults._

object Home extends StyleSheet.Inline {
  import dsl._

  private val headerHeight = 80
  private val panelSize = 240
  private val iconSize = 80

  val container = style(
    borderWidth(2 px, 0 px, 0 px, 0 px),
    borderStyle.solid,
    borderColor(Color("#0fabff")),
    width(100 %%),
    height(100 %%),
    backgroundColor(Color("#f9f9f9")),
  )

  val header = style(
    position.fixed,
    backgroundColor(Color("white")),
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

  val content = style(
    paddingTop(headerHeight px),
    width(100 %%),
    height(100 %%),
  )

  val alternative = style(
    margin(8 px, auto),
    display.flex,
    flexWrap.wrap,
    justifyContent.center,
    width(100 %%),
    height(256 px),
    padding(0 px, 8 px),
  )

  val panel = style(
    margin(8 px, 16 px),
    width(panelSize px),
    height(panelSize px),
    padding((panelSize - iconSize) / 2 px, (panelSize - iconSize) / 2 px),
    backgroundColor(Color("white")),
    borderRadius(8 px),
  )

  private val icon = mixin(
    &.hover(
      opacity(1),
    ),
    &.active(
      transitionDuration(0.seconds),
      transform := "translate(2px,2px)",
    ),
    width(iconSize px),
    height(iconSize px),
    backgroundRepeat := "no-repeat",
    backgroundSize := "contain",
    opacity(0.7),
    cursor.pointer,
    borderStyle.none,
    outline.`0`,
    transitionDuration(0.3.seconds),
  )

  val signUp = style(
    icon,
    backgroundImage := "url('/assets/images/icons/fiber_new.png')",
  )

  val login = style(
    icon,
    backgroundImage := "url('/assets/images/icons/input.png')",
  )
}
