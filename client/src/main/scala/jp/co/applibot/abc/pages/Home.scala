package jp.co.applibot.abc.pages

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import jp.co.applibot.abc.models.Props
import scalacss.ScalaCssReact._

object Home {
  val home = ScalaComponent.builder[Props]("Home")
    .stateless
    .render_P((props) =>
      <.div(
        <.h1("Welcome to Applibot Chat"),
        <.div(
          <.div(
            <.button(
              "新規登録",
              ^.onClick --> Callback(props.actions),  // TODO:
            ),
          ),
          <.div(
            <.button(
              "ログイン",
              ^.onClick --> Callback(props.actions),  // TODO:
            )
          ),
        ),
      )
    )
    .build

  def apply(props: Props) = home(props)
}
