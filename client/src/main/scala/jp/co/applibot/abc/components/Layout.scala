package jp.co.applibot.abc.components

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import jp.co.applibot.abc.shared.styles
import jp.co.applibot.react.Router.Router

import scalacss.ScalaCssReact._

object Layout {

  case class Props(content: VdomElement, router: Router)

  class Backend(bs: BackendScope[Props, Unit]) {
    def render(props: Props) = {
      def isHomePage = props.router.pathname == "/"

      <.div(
        styles.Layout.container,
        <.header(
          styles.Layout.header,
          <.img(
            styles.Layout.logo,
            if (!isHomePage) styles.Layout.logoActive else EmptyVdom,
            ^.src := "/assets/images/favicon.png",
            ^.onClick --> Callback.when(!isHomePage)(props.router.push("/"))
          ),
        ),
        <.div(
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
