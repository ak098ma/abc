package jp.co.applibot.abc.pages

import japgolly.scalajs.react._
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.vdom.html_<^._
import jp.co.applibot.abc.actions.SignUpActions
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
            FormItem(FormItem.Props("ユーザーID", props.state.signUp.id, "user id", (_) => Callback.empty)),
            FormItem(FormItem.Props("ニックネーム", props.state.signUp.nickname, "nickname", (_) => Callback.empty)),
            FormItem(FormItem.Props("パスワード", props.state.signUp.password, "password", (_) => Callback.empty, true)),
            <.div(
              styles.SignUp.signUpButtonRow,
              <.div(
                styles.SignUp.signUpButtonContainer,
                <.button(
                  styles.SignUp.signUpButton,
                  ^.onClick --> handleClickSignUp
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
