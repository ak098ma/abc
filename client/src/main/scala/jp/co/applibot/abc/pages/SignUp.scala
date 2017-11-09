package jp.co.applibot.abc.pages

import japgolly.scalajs.react._
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.vdom.html_<^._
import jp.co.applibot.abc.Store
import jp.co.applibot.abc.actions.SignUpActions
import jp.co.applibot.abc.models.State

import scala.scalajs.js

trait SignUp {

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
            ^.value := state.signUp.id,
            ^.onChange ==> handleChangeUserID
          )
        ),
        <.div(
          <.label("Nickname"),
          <.input(
            ^.placeholder := "Enter nickname...",
            ^.value := state.signUp.nickname,
            ^.onChange ==> handleChangeNickname
          )
        ),
        <.div(
          <.label("Password"),
          <.input(
            ^.placeholder := "Enter password...",
            ^.value := state.signUp.password,
            ^.`type` := "password",
            ^.onChange ==> handleChangePassword
          )
        ),
        <.div(
          <.button(
            "Login"
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
      SignUpActions.setUserId(value)
    }

    def handleChangeNickname(event: ReactEventFromInput): Callback = Callback {
      val value = event.target.value
      SignUpActions.setNickname(value)
    }

    def handleChangePassword(event: ReactEventFromInput): Callback = Callback {
      val value = event.target.value
      SignUpActions.setPassword(value)
    }
  }

  private val signUp = ScalaComponent.builder[Unit]("SignUp")
    .initialState(Store.getState)
    .backend(new Backend(_))
    .renderBackend
    .componentWillMount(_.backend.componentWillMount)
    .componentWillUnmount(_.backend.componentWillUnmount)
    .build

  def apply(): Unmounted[Unit, State, Backend] = signUp()
}
