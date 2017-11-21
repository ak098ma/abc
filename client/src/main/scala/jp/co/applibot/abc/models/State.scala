package jp.co.applibot.abc.models

case class State(login: LoginState,
                 signUp: SignUpState,
                 chat: ChatState)
