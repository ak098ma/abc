package jp.co.applibot.abc.pages

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import jp.co.applibot.abc.models.Props
import jp.co.applibot.abc.shared.styles

import scalacss.ScalaCssReact._

object Home {

  class Backend(bs: BackendScope[Props, Unit]) {

    def render(props: Props) = {
      <.div(
        styles.Home.alternative,
        <.div(
          styles.Home.panel,
          <.div(
            styles.Home.link,
            ^.onClick --> props.router.push("/sign-up"),
            <.button(
              styles.Home.signUp,
            ),
            <.div(
              "新規登録"
            ),
          ),
        ),
        <.div(
          styles.Home.panel,
          <.div(
            styles.Home.link,
            ^.onClick --> props.router.push("/login"),
            <.button(
              styles.Home.login,
            ),
            <.div(
              "ログイン"
            ),
          ),
        ),
      )
    }
  }

  private val home = ScalaComponent.builder[Props]("Home")
    .stateless
    .backend(new Backend(_))
    .renderBackend
    .build

  def apply(props: Props) = home(props)
}
