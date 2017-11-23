package jp.co.applibot.abc.shared.styles

import scalacss.DevDefaults._

object Chat extends StyleSheet.Inline {
  import dsl._

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

  val room = style(
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

  val controller = style(
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
