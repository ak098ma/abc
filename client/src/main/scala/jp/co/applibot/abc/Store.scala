package jp.co.applibot.abc

import jp.co.applibot.abc.models._
import jp.co.applibot.flux

object Store extends flux.Store(State(
  login = LoginState(
    id = "",
    password = ""),
  signUp = SignUpState(
    id = "",
    nickname = "",
    password = "")))
