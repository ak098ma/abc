package jp.co.applibot.abc.shared.styles

import scala.concurrent.duration._
import scalacss.DevDefaults._

object Home extends StyleSheet.Inline {
  import dsl._

  private val panelSize = 240
  private val iconSize = 80

  val alternative = style(
    margin(8 px, 0 px),
    display.flex,
    flexWrap.wrap,
    justifyContent.center,
    width(100 %%),
    height(256 px),
    padding(0 px, 8 px),
  )

  val panel = style(
    textAlign.center,
    margin(8 px, 16 px),
    width(panelSize px),
    height(panelSize px),
    padding((panelSize - iconSize - 24) / 2 px, (panelSize - iconSize) / 2 px),
    backgroundColor(Color(Colors.white)),
    borderRadius(8 px),
  )

  val link = style(
    &.active(
      transitionDuration(0.seconds),
      transform := "translate(2px,2px)",
    ),
    &.hover(
      opacity(1),
      transform := "scale(1.04, 1.04)",
    ),
    opacity(0.7),
    width(iconSize px),
    height(iconSize + 24 px),
    cursor.pointer,
    transitionDuration(0.3.seconds),
  )

  private val icon = mixin(
    width(iconSize px),
    height(iconSize px),
    backgroundRepeat := "no-repeat",
    backgroundSize := "contain",
    backgroundColor.transparent,
    borderStyle.none,
    outline.`0`,
  )

  val signUp = style(
    icon,
    backgroundImage := "url('/assets/images/icons/fiber_new.png')",
  )

  val login = style(
    icon,
    backgroundImage := "url('/assets/images/icons/exit_to_app.png')",
  )
}
