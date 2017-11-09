package jp.co.applibot.abc.pages

import japgolly.scalajs.react._
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.vdom.html_<^._
import jp.co.applibot.abc.Store
import jp.co.applibot.abc.actions.{LoginActions, WebActions}
import jp.co.applibot.abc.models.State
import jp.co.applibot.abc.shared.models.UserCredential

import scala.scalajs.js

trait Login {

  class Backend(bs: BackendScope[Unit, State]) {
    private val update: js.Function1[State, Unit] = (state) => {
      bs.setState(state).runNow()
    }

    def render(state: State) = {
      <.div(
        <.div(
          <.label("UserID"),
          <.input(
            ^.placeholder := "Enter id...",
            ^.value := state.login.id,
            ^.onChange ==> handleChangeUserID
          )
        ),
        <.div(
          <.label("Password"),
          <.input(
            ^.placeholder := "Enter password...",
            ^.value := state.login.password,
            ^.`type` := "password",
            ^.onChange ==> handleChangePassword
          )
        ),
        <.div(
          <.button(
            "Login",
            ^.onClick --> handleClickLogin
          )
        )
      )
    }

    def componentWillMount = Callback {
      Store.subscribe(update)
    }

    def componentWillUnmount = Callback {
      Store.unsubscribe(update)
    }

    def handleChangeUserID(event: ReactEventFromInput): Callback = Callback {
      val value = event.target.value
      LoginActions.setUserId(value)
    }

    def handleChangePassword(event: ReactEventFromInput): Callback = Callback {
      val value = event.target.value
      LoginActions.setPassword(value)
    }

    def handleClickLogin: Callback = bs.state.map { state =>
      WebActions.login(UserCredential(id = state.login.id, password = state.login.password))
    }
  }

  private def login = ScalaComponent.builder[Unit]("Login")
    .initialState(Store.getState)
    .backend(new Backend(_))
    .renderBackend
    .componentWillMount(_.backend.componentWillMount)
    .componentWillUnmount(_.backend.componentWillUnmount)
    .build

  def apply(): Unmounted[Unit, State, Backend] = login()
}
