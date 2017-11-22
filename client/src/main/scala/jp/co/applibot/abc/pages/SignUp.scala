package jp.co.applibot.abc.pages

import japgolly.scalajs.react._
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.vdom.html_<^._
import jp.co.applibot.abc.components.FormItem
import jp.co.applibot.abc.models.Props
import jp.co.applibot.abc.react.BackendUtils
import jp.co.applibot.abc.shared.styles

import scalacss.ScalaCssReact._

object SignUp {

  class Backend(override val bs: BackendScope[Props, Unit]) extends BackendUtils[Props, Unit] {
    def render(props: Props) = {
      <.div(
        styles.SignUp.container,
        <.div(
          <.div(
            styles.SignUp.form,
            FormItem(FormItem.Props("ユーザーID", props.state.signUp.id, "user id", props.actions.setSignUpId)),
            FormItem(FormItem.Props("ニックネーム", props.state.signUp.nickname, "nickname", props.actions.setSignUpNickname)),
            FormItem(FormItem.Props("パスワード", props.state.signUp.password, "password", props.actions.setSignUpPassword, isCredential = true)),
            <.div(
              styles.SignUp.signUpButtonRow,
              <.div(
                styles.SignUp.signUpButtonContainer,
                <.button(
                  styles.SignUp.signUpButton,
                  ^.onClick --> Callback.warn("not implemented yet."),
                ),
                <.div(
                  "新規登録"
                ),
              ),
            ),
          ),
          <.div(
            styles.SignUp.linkContainer,
            <.a(
              styles.SignUp.gotoLoginButton,
              "すでにアカウントをお持ちの方はこちら",
              ^.onClick --> props.router.push("/login"),
            )
          )
        )
      )
    }
  }

  private val signUp = ScalaComponent.builder[Props]("SignUp")
    .stateless
    .backend(new Backend(_))
    .renderBackend
    .build

  def apply(props: Props): Unmounted[Props, Unit, Backend] = signUp(props)
}
