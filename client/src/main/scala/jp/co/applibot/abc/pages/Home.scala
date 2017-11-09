package jp.co.applibot.abc.pages

import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._
import jp.co.applibot.abc._
import jp.co.applibot.abc.models.State

import scala.scalajs.js

trait Home {
  type Props = RouterCtl[Page]

  class Backend(bs: BackendScope[Props, State]) {
    private val update: js.Function1[State, Unit] = (state) => {
      bs.setState(state).runNow()
    }

    def render(props: Props, state: State) = {
      <.div(
        <.div(
          <.button(
            "New user?",
            ^.onClick --> handleClickSignUp
          )
        ),
        <.div(
          <.button(
            "Already have an account?",
            ^.onClick --> handleClickLogin
          )
        )
      )
    }

    def componentWillMount: Callback = Callback {
      Store.subscribe(update)
    }

    def componentWillUnmount: Callback = Callback {
      Store.unsubscribe(update)
    }

    def handleClickSignUp: Callback = bs.props.flatMap(_.set(SignUp))

    def handleClickLogin: Callback = bs.props.flatMap(_.set(Login))
  }

  private val home = ScalaComponent.builder[Props]("Home")
    .initialState(Store.getState)
    .backend(new Backend(_))
    .renderBackend
    .componentWillMount(_.backend.componentWillMount)
    .componentWillUnmount(_.backend.componentWillUnmount)
    .build

  def apply(props: Props): japgolly.scalajs.react.vdom.VdomElement = home(props)
}
