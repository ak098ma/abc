package jp.co.applibot.abc.pages

import japgolly.scalajs.react._
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._
import jp.co.applibot.abc.{Page, Store}
import jp.co.applibot.abc.mvc.actions.{LoginActions, WebActions}
import jp.co.applibot.abc.models.State
import jp.co.applibot.abc.mvc.errors.LoginError
import jp.co.applibot.abc.shared.models.UserCredential
import jp.co.applibot.abc.react.BackendUtils

import scala.scalajs.js

trait Login {
  type Props = RouterCtl[Page]

  class Backend(override val bs: BackendScope[Props, State]) extends BackendUtils[Props, State] {
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
        ),
        <.div(
          <.button(
            "new user?",
            ^.onClick --> handleClickNewUser
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

    def handleClickLogin: Callback = callbackWithPS { (props, state) =>
      WebActions.login(
        userCredential = UserCredential(id = state.login.id, password = state.login.password),
        onSuccess = () => props.set(Page.Chat).runNow(),
        onFailure = {
          case LoginError.Unauthorized =>
            org.scalajs.dom.window.console.warn(LoginError.Unauthorized.message)
          case error =>
            org.scalajs.dom.window.console.error(error.message)
        }
      )
    }

    def handleClickNewUser: Callback = bs.props.flatMap(_.set(Page.SignUp))
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
