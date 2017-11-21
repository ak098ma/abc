package jp.co.applibot.abc.shared.styles

import scalacss.DevDefaults._

object Index extends StyleSheet.Standalone {
  import dsl._

  "*" - (
    margin(0 px),
    padding(0 px),
    boxSizing.borderBox,
  )
}
