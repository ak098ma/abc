package jp.co.applibot.abc.shared.styles

import scalacss.DevDefaults._
import scala.concurrent.duration._

object Chat extends StyleSheet.Inline {
  import dsl._

  private val iconSize = 32

  val root = style(
    position.relative,
    width(100 %%),
    height(100 %%),
  )

  val localNavigation = style(
    position.fixed,
    borderWidth(1 px, 0 px, 0 px, 0 px),
    borderColor(Color(Colors.grey200)),
    borderStyle.solid,
    width(100 %%),
    height(32 px),
  )

  val container = style(
    display.flex,
    width(100 %%),
    height(100 %%),
    padding(32 px, 0 px, 0 px, 0 px),
  )

  val rooms = style(
    borderWidth(1 px, 1 px, 0 px, 0 px),
    borderStyle.solid,
    borderColor(Color(Colors.grey200)),
    backgroundColor(Color(Colors.grey50)),
    width(256 px),
    height(100 %%),
  )

  val roomsController = style(
    width(100 %%),
    textAlign.center,
  )

  val addRoomButton = style(
    &.hover(
      opacity(1),
      transform := "scale(1.1, 1.1)",
    ),
    &.active(
      transitionDuration(0.seconds),
      transform := "scale(1, 1)",
    ),
    opacity(0.7),
    Button.icon,
    width(iconSize px),
    height(iconSize px),
    backgroundImage := "url('/assets/images/icons/add.png')",
    transitionDuration(0.3.seconds),
  )

  val joinedRoom = style(

  )

  val availableRoom = style(

  )

  val chat = style(
    width(100 %%),
    height(100 %%),
    backgroundColor(Color(Colors.white)),
  )

  val items = style(
    borderWidth(1 px, 0 px, 1 px, 0 px),
    borderStyle.solid,
    borderColor(Color(Colors.grey200)),
    width(100 %%),
    height(100 %%),
    padding(0 px, 0 px, 32 px, 0 px),
  )

  val itemSystem = style(

  )

  val itemSelf = style(

  )

  val itemOthers = style(

  )

  val chatController = style(
    borderWidth(1 px, 0 px, 0 px, 0 px),
    borderStyle.solid,
    borderColor(Color(Colors.grey200)),
    width(100 %%),
    height(32 px),
    backgroundColor(Color(Colors.white)),
    position.fixed,
    bottom.`0`,
  )
}
