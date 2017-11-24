package jp.co.applibot.abc.pages

import japgolly.scalajs.react._
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.vdom.html_<^._
import jp.co.applibot.abc.components.FormItem
import jp.co.applibot.abc.models.Props
import jp.co.applibot.abc.shared.styles

import scalacss.ScalaCssReact._

object Login {

  class Backend(bs: BackendScope[Props, Unit]) {
    def render(props: Props) = {
      <.div(
        styles.Login.container,
        <.div(
          <.div(
            styles.Login.form,
            FormItem(FormItem.Props("ユーザーID", props.state.login.id, "user_id", props.actions.setLoginId)),
            FormItem(FormItem.Props("パスワード", props.state.login.password, "password", props.actions.setLoginPassword, isCredential = true)),
            <.div(
              styles.Login.loginButtonRow,
              <.div(
                styles.Login.loginButtonContainer,
                <.button(
                  styles.Login.loginButton,
                ),
                <.div(
                  "ログイン"
                ),
                ^.onClick --> props.webActions.login,
              )
            ),
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
  }

  private val login = ScalaComponent.builder[Props]("Login")
    .stateless
    .backend(new Backend(_))
    .renderBackend
    .build

  def apply(props: Props): Unmounted[Props, Unit, Backend] = login(props)
}
