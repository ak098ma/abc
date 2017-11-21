package jp.co.applibot.abc.shared.styles

import scala.concurrent.duration._
import scalacss.DevDefaults._

object SignUp extends StyleSheet.Inline {

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

  val signUpButtonContainer = style(
    display.flex,
    justifyContent.center,
    width(100 %%),
    padding(8 px),
  )

  val signUpButton = style(
    &.hover(
      opacity(1),
      transform := "rotate(-377deg)",
    ),
    &.active(
      transitionDuration(0.seconds),
      transform := "translate(2px,2px) rotate(-11deg)",
    ),
    backgroundSize := "contain",
    backgroundImage := "url(/assets/images/icons/send.png)",
    backgroundRepeat := "no-repeat",
    opacity(0.7),
    cursor.pointer,
    borderStyle.none,
    outline.`0`,
    width(iconSize px),
    height(iconSize px),
    transitionDuration(0.3.seconds),
    transformOrigin := s"${iconSize / 2 - 8}px ${iconSize / 2 + 8}px",
    transform := "rotate(-11deg)",
  )

  val linkContainer = style(
    width(512 px),
    display.flex,
    justifyContent.center,
    padding(16 px, 0 px),
  )

  val gotoLoginButton = style(
    &.hover(
      textDecoration := "underline",
    ),
    color(Color("#0fabff")),
    fontSize(12 px),
    fontWeight.lighter,
    cursor.pointer,
  )
}
