package jp.co.applibot.abc.pages

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import jp.co.applibot.react.Router.Router

object Home {
  type Props = Router

  val home = ScalaComponent.builder[Props]("Home")
    .initialState("")
    .renderPS((bs, props, state) =>
      <.div(
        <.h1("Welcome to Applibot Chat"),
        <.div(
          "Goto...",
        ),
        <.div(
          <.input(
            ^.value := state,
            ^.onChange ==> {(event: ReactEventFromInput) =>
              val value = event.target.value
              bs.modState(_ => value)
            },
            ^.onKeyDown ==> ((event: ReactKeyboardEventFromInput) => Callback.when(event.keyCode == 13)(props.push(state)))
          )
        ),
      )
    )
    .build

  def apply(props: Props) = home(props)
}
