package jp.co.applibot.abc.models

case class State(modal: ModalState,
                 login: LoginState,
                 signUp: SignUpState,
                 chat: ChatState,
                 user: UserState)
