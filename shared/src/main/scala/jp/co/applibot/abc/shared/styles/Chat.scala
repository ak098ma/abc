package jp.co.applibot.abc.shared.styles

import scalacss.DevDefaults._
import scala.concurrent.duration._

object Chat extends StyleSheet.Inline {
  import dsl._

  private val iconSize = 32
  private val max = 100.%%

  private val divider = mixin(
    borderStyle.solid,
    borderColor(Color(Colors.grey300)),
  )

  val root = style(
    display.flex,
    flexDirection.row,
    width(max),
    divider,
    borderWidth(1.px, 0.px, 0.px, 0.px),
  )

  val rooms = style(
    flex := "0 0 256px",
    display.flex,
    flexDirection.column,
  )

  val roomNav = style(
    flex := "0 0 40px",
    display.flex,
    justifyContent.center,
    alignItems.center,
    divider,
    borderWidth(0.px, 0.px, 1.px, 0.px),
  )

  val roomList = style(
    flex := "1 0 0",
    display.flex,
    flexDirection.column,
    overflowY.scroll,
  )

  val room = style(
    &.hover(
      backgroundColor(Color(Colors.grey200)),
    ),
    &.active(
      backgroundColor(Color(Colors.grey100)),
    ),
    display.flex,
    flexDirection.row,
    borderColor(Color(Colors.grey300)),
    borderWidth(0.px, 0.px, 1.px, 0.px),
    borderStyle.solid,
    flex := "0 0 64px",
    justifyContent.spaceBetween,
    alignItems.center,
    cursor.pointer,
    userSelect := "none",
  )

  val roomTitle = style(
    margin(0.px, 20.px),
    width(128.px),
    textAlign.center,
  )

  val joinedIcon = style(
    margin(0.px, 20.px),
  )

  val chat = style(
    flex := "1 0 0",
    display.flex,
    flexDirection.column,
    divider,
    minWidth(256.px),
    borderWidth(0.px, 1.px),
  )

  val chatTitle = style(
    flex := "0 0 40px",
    divider,
    borderWidth(0.px, 0.px, 1.px, 0.px),
  )

  val messages = style(
    flex := "1 0 0",
  )

  private val messageMixin = mixin(
    display.flex,
    alignItems.center,
    width(100.%%),
    padding(4.px),
  )

  val myMessageContainer = style(
    messageMixin,
    justifyContent.flexStart,
  )

  val myMessage = style(
    width(75.%%),
    borderColor(Color(Colors.lightBlue500)),
    borderStyle.solid,
    borderWidth(1.px),
    borderRadius(12.px, 12.px, 12.px, 0.px),
    padding(0.px, 4.px),
    backgroundColor(Color(Colors.white)),
  )

  val otherMessageContainer = style(
    messageMixin,
    justifyContent.flexEnd,
  )

  val otherMessage = style(
    width(75.%%),
    borderColor(Color(Colors.grey300)),
    borderStyle.solid,
    borderWidth(1.px),
    borderRadius(12.px, 12.px, 0.px, 12.px),
    padding(0.px, 8.px),
    backgroundColor(Color(Colors.white)),
  )

  val chatController = style(
    flex := "0 0 40px",
    display.flex,
    justifyContent.center,
    alignItems.center,
    flexDirection.row,
    divider,
    borderWidth(1.px, 0.px, 0.px, 0.px),
  )

  val inputMessage = style(
    flex := "1 0 0",
    height(32.px),
    margin(0.px, 4.px),
  )

  private val sendButtonHighlight = mixin(
    transform := "scale(1, 1) rotate(-11deg)",
    opacity(1),
  )
  val sendButton = style(
    &.hover(sendButtonHighlight),
    &.focus(sendButtonHighlight),
    &.active(
      transform := "scale(0.9, 0.9) rotate(-11deg)",
      transitionDuration(0.seconds),
      opacity(0.7),
    ),
    flex := "0 0 32px",
    width(32.px),
    height(32.px),
    Button.icon,
    backgroundImage := "url(/assets/images/icons/send.png)",
    transform := "scale(0.94, 0.94) rotate(-11deg)",
    transitionDuration(0.3.seconds),
    margin(0.px, 4.px, 0.px, 0.px),
    opacity(0.7),
  )

  val users = style(
    flex := "0 0 256px",
    display.flex,
    flexDirection.column,
  )

  val you = style(
    flex := "0 0 40px",
    display.flex,
    justifyContent.center,
    alignItems.center,
    divider,
    borderWidth(0.px, 0.px, 1.px, 0.px),
  )

  val members = style(
    flex := "1 0 0",
    overflowY.scroll,
  )

  val addRoomButton = style(
    &.hover(
      opacity(1),
      transform := "scale(1.11, 1.11)",
    ),
    &.active(
      transitionDuration(0.seconds),
      transform := "scale(1, 1)",
    ),
    opacity(0.7),
    Button.icon,
    width(iconSize.px),
    height(iconSize.px),
    cursor.pointer,
    backgroundImage := "url('/assets/images/icons/add.png')",
    transitionDuration(0.3.seconds),
  )

  val createRoomModal = style(
    backgroundColor(Color(Colors.white)),
    borderRadius(4.px),
    padding(8.px),
    width(256.px),
    textAlign.center,
  )

  val roomTitleInput = style(
    width(214.px),
    padding(4.px),
    margin(8.px),
  )

  val createRoomModalControllerContainer = style(
    display.flex,
    justifyContent.center,
  )

  val modalButtonBase = style(
    width(80.px),
    height(24.px),
    margin(0.px, 8.px),
  )
}
