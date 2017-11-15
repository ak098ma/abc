package jp.co.applibot.abc

import jp.co.applibot.abc.models._
import jp.co.applibot.flux

object Store extends flux.Store(State(
  router = None,
  login = LoginState(
    id = "",
    password = ""),
  signUp = SignUpState(
    id = "",
    nickname = "",
    password = "")))
