package jp.co.applibot.abc.actions

import japgolly.scalajs.react.Callback
import jp.co.applibot.abc.models._

class Actions(private[this] val update: (State => State) => Callback) {
  private def updateLoginState(updateLogin: LoginState => LoginState): Callback = update(state => state.copy(login = updateLogin(state.login)))

  private def updateSignUpState(updateSignUp: SignUpState => SignUpState): Callback = update(state => state.copy(signUp = updateSignUp(state.signUp)))

  private def updateChatState(updateChat: ChatState => ChatState): Callback = update(state => state.copy(chat = updateChat(state.chat)))

  private def updateUserState(updateUser: UserState => UserState): Callback = update(state => state.copy(user = updateUser(state.user)))

  def setSignUpId(id: String): Callback = updateSignUpState(_.copy(id = id))

  def setSignUpNickname(nickname: String): Callback = updateSignUpState(_.copy(nickname = nickname))

  def setSignUpPassword(password: String): Callback = updateSignUpState(_.copy(password = password))

  def setLoginId(id: String): Callback = updateLoginState(_.copy(id = id))

  def setLoginPassword(password: String): Callback = updateLoginState(_.copy(password = password))

  def setToken(token: String): Callback = updateUserState(_.copy(tokenOption = Some(token)))

  def clearToken(): Callback = updateUserState(_.copy(tokenOption = None))
}
