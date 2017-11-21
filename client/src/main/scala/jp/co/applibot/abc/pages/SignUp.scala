package jp.co.applibot.abc.pages

import japgolly.scalajs.react._
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.vdom.html_<^._
import jp.co.applibot.abc.models.Props
import jp.co.applibot.abc.mvc.actions.SignUpActions
import jp.co.applibot.abc.react.BackendUtils

object SignUp {
  class Backend(override val bs: BackendScope[Props, Unit]) extends BackendUtils[Props, Unit] {
    def render(props: Props) = {
      <.div(
        <.div(
          <.label("UserID"),
          <.input(
            ^.placeholder := "Enter id...",
            ^.value := "TODO: ",
            ^.onChange ==> handleChangeUserID
          )
        ),
        <.div(
          <.label("Nickname"),
          <.input(
            ^.placeholder := "Enter nickname...",
            ^.value := "TODO: ",
            ^.onChange ==> handleChangeNickname
          )
        ),
        <.div(
          <.label("Password"),
          <.input(
            ^.placeholder := "Enter password...",
            ^.value := "TODO: ",
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

    def componentWillMount: Callback = bs.props.map { props =>

    }

    def componentWillUnmount = Callback {

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
//      WebActions.signUp(User(id = state.signUp.id, nickname = state.signUp.nickname, password = state.signUp.password, joiningChatRooms = Seq.empty))
    }

    def handleClickAlreadyHaveAnAccount: Callback = Callback.empty
  }

  private val signUp = ScalaComponent.builder[Props]("SignUp")
    .stateless
    .backend(new Backend(_))
    .renderBackend
    .componentWillMount(_.backend.componentWillMount)
    .componentWillUnmount(_.backend.componentWillUnmount)
    .build

  def apply(props: Props): Unmounted[Props, Unit, Backend] = signUp(props)
}
