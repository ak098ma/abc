package jp.co.applibot.abc.pages

import japgolly.scalajs.react._
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._
import jp.co.applibot.abc.{Page, Store}
import jp.co.applibot.abc.mvc.actions.{SignUpActions, WebActions}
import jp.co.applibot.abc.models.State
import jp.co.applibot.abc.mvc.errors.SignUpError
import jp.co.applibot.abc.react.BackendUtils
import jp.co.applibot.abc.shared.models.User

import scala.scalajs.js

trait SignUp {
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
            "SignUp",
            ^.onClick --> handleClickSignUp
          )
        ),
        <.div(
          <.button(
            "already have an account?",
            ^.onClick --> handleClickAlreadyHaveAnAccount
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

    def handleClickSignUp: Callback = callbackWithPS { (props, state) =>
      WebActions.signUp(
        user = User(id = state.signUp.id, nickname = state.signUp.nickname, password = state.signUp.password),
        onSuccess = () => props.set(Page.Login),
        onFailure = {
          case SignUpError.InvalidPassword =>
            org.scalajs.dom.window.console.warn(SignUpError.InvalidPassword.message)
          case error =>
            org.scalajs.dom.window.console.error(error.message)
        },
      )
    }

    def handleClickAlreadyHaveAnAccount: Callback = bs.props.flatMap(_.set(Page.Login))
  }

  private val signUp = ScalaComponent.builder[Props]("SignUp")
    .initialState(Store.getState)
    .backend(new Backend(_))
    .renderBackend
    .componentWillMount(_.backend.componentWillMount)
    .componentWillUnmount(_.backend.componentWillUnmount)
    .build

  def apply(props: Props): Unmounted[Props, State, Backend] = signUp(props)
}
