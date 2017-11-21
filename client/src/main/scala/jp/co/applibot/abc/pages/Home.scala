package jp.co.applibot.abc.pages

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import jp.co.applibot.abc.models.Props
import jp.co.applibot.abc.shared.styles

import scalacss.ScalaCssReact._

object Home {
  val home = ScalaComponent.builder[Props]("Home")
    .stateless
    .render_P((props) =>
      <.div(
        styles.Home.alternative,
        <.div(
          styles.Home.panel,
          <.button(
            styles.Home.signUp,
            ^.onClick --> props.router.push("/sign-up"),
          ),
        ),
        <.div(
          styles.Home.panel,
          <.button(
            styles.Home.login,
            ^.onClick --> props.router.push("/login"),
          )
        ),
      )
    )
    .build

  def apply(props: Props) = home(props)
}
