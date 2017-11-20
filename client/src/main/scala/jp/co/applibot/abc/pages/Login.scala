package jp.co.applibot.abc.pages

import japgolly.scalajs.react._
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.vdom.html_<^._
import jp.co.applibot.abc.Store
import jp.co.applibot.abc.models.State
import jp.co.applibot.abc.mvc.actions.LoginActions
import jp.co.applibot.abc.react.BackendUtils
import jp.co.applibot.react.Router.Router

object Login {
  type Props = Router

  class Backend(override val bs: BackendScope[Props, State]) extends BackendUtils[Props, State] {
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
        ),
        <.div(
          <.button(
            "new user?",
            ^.onClick --> handleClickNewUser
          )
        ),
      )
    }

    def componentWillMount: Callback = bs.props.map { props =>
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

    def handleClickLogin: Callback = callbackWithPS { (props, state) =>

    }

    def handleClickNewUser: Callback = Callback {}
  }

  private def login = ScalaComponent.builder[Props]("Login")
    .initialState(Store.getState)
    .backend(new Backend(_))
    .renderBackend
    .componentWillMount(_.backend.componentWillMount)
    .componentWillUnmount(_.backend.componentWillUnmount)
    .build

  def apply(props: Props): Unmounted[Props, State, Backend] = login(props)
}
