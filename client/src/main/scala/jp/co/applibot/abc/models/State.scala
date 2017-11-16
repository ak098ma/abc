package jp.co.applibot.abc.models

import japgolly.scalajs.react.extra.router.RouterCtl
import jp.co.applibot.abc.Page

case class State(router: Option[RouterCtl[Page]],
                 login: LoginState,
                 signUp: SignUpState,
                 chat: ChatState)
