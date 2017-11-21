package views.styles

import scalacss.DevDefaults._

object Index extends StyleSheet.Standalone {
  import dsl._

  "*" - (
    margin(0 px),
    padding(0 px),
  )
}
