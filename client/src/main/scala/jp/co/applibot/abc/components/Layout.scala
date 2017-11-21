package jp.co.applibot.abc.components

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import jp.co.applibot.abc.shared.styles

import scalacss.ScalaCssReact._

object Layout {

  case class Props(content: VdomElement)

  class Backend(bs: BackendScope[Props, Unit]) {
    def render(props: Props) = {
      <.div(
        styles.Layout.container,
        <.header(
          styles.Layout.header,
          <.img(styles.Layout.logo, ^.src := "/assets/images/favicon.png"),
        ),
        <.section(
          styles.Layout.content,
          props.content,
        )
      )
    }
  }

  private val header = ScalaComponent.builder[Props]("Header")
    .stateless
    .backend(new Backend(_))
    .renderBackend
    .build

  def apply(props: Props) = header(props)
}
