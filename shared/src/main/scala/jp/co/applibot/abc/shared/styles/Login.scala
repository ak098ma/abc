package jp.co.applibot.abc.shared.styles

import scala.concurrent.duration._
import scalacss.DevDefaults._

object Login extends StyleSheet.Inline {

  import dsl._

  private val iconSize = 48

  val container = style(
    margin(16 px, 0 px),
    display.flex,
    flexWrap.wrap,
    justifyContent.center,
    width(100 %%),
    padding(0 px, 8 px),
  )

  val form = style(
    borderStyle.solid,
    borderWidth(1 px),
    borderColor(Color("#e5e5e5")),
    borderRadius(8 px),
    backgroundColor(Color("white")),
    width(512 px),
    padding(8 px),
  )

  val loginButtonRow = style(
    display.flex,
    justifyContent.center,
    width(100 %%),
    padding(8 px),
  )

  val loginButtonContainer = style(
    textAlign.center,
    &.hover(
      opacity(1),
      transform := "scale(1.1, 1.1)",
    ),
    &.active(
      transitionDuration(0.seconds),
      transform := "scale(1, 1)",
    ),
    opacity(0.7),
    cursor.pointer,
    transitionDuration(0.3.seconds),
    transform := "scale(1, 1)",
  )

  val loginButton = style(
    backgroundSize := "contain",
    backgroundImage := "url(/assets/images/icons/thumb_up.png)",
    backgroundRepeat := "no-repeat",
    backgroundColor.transparent,
    borderStyle.none,
    outline.`0`,
    width(iconSize px),
    height(iconSize px),
    cursor.pointer,
  )

  val linkContainer = style(
    width(512 px),
    display.flex,
    justifyContent.center,
    padding(16 px, 0 px),
  )

  val gotoSignUpButton = style(
    &.hover(
      textDecoration := "underline",
    ),
    color(Color("#0fabff")),
    fontSize(12 px),
    fontWeight.lighter,
    cursor.pointer,
  )
}
