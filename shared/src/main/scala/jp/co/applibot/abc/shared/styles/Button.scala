package jp.co.applibot.abc.shared.styles

import scalacss.DevDefaults._

object Button extends StyleSheet.Inline {
  import dsl._

  val icon = mixin(
    backgroundRepeat := "no-repeat",
    backgroundSize := "contain",
    backgroundColor.transparent,
    borderStyle.none,
    outline.`0`,
  )
}
