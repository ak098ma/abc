package jp.co.applibot.abc.pages

import japgolly.scalajs.react._
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.vdom.html_<^._
import jp.co.applibot.abc.models.Props
import jp.co.applibot.abc.mvc.actions.LoginActions
import jp.co.applibot.abc.react.BackendUtils

object Login {
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

    }

    def componentWillUnmount = Callback {

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
    .stateless
    .backend(new Backend(_))
    .renderBackend
    .componentWillMount(_.backend.componentWillMount)
    .componentWillUnmount(_.backend.componentWillUnmount)
    .build

  def apply(props: Props): Unmounted[Props, Unit, Backend] = login(props)
}
