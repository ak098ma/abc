package jp.co.applibot.abc.shared.styles

import scalacss.DevDefaults._

object FormItem extends StyleSheet.Inline {
  import dsl._

  val formItem = style(
    display.flex,
    justifyContent.center,
  )

  val label = style(
    textAlign.center,
    width(128 px),
    padding(8 px),
  )

  val inputContainer = style(
    width(64 %%),
    padding(8 px),
  )

  val input = style(
    width(100 %%),
    padding(4 px),
  )
}
