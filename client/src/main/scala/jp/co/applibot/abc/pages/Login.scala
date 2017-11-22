package jp.co.applibot.abc.pages

import japgolly.scalajs.react._
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.vdom.html_<^._
import jp.co.applibot.abc.actions.LoginActions
import jp.co.applibot.abc.components.FormItem
import jp.co.applibot.abc.models.Props
import jp.co.applibot.abc.react.BackendUtils
import jp.co.applibot.abc.shared.styles

import scalacss.ScalaCssReact._

object Login {
  class Backend(override val bs: BackendScope[Props, Unit]) extends BackendUtils[Props, Unit] {
    def render(props: Props) = {
      <.div(
        styles.Login.container,
        <.div(
          <.div(
            styles.Login.form,
            FormItem(FormItem.Props("ユーザーID", props.state.login.id, "user_id", (_) => Callback.empty)),
            FormItem(FormItem.Props("パスワード", props.state.login.password, "password", (_) => Callback.empty, true)),
          ),
          <.div(
            styles.Login.linkContainer,
            <.a(
              styles.Login.gotoSignUpButton,
              "はじめての方はこちら",
              ^.onClick --> props.router.push("/sign-up"),
            )
          ),
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
