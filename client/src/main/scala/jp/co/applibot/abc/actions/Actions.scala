package jp.co.applibot.abc.actions

import japgolly.scalajs.react.Callback
import jp.co.applibot.abc.TokenManager
import jp.co.applibot.abc.models._
import jp.co.applibot.abc.shared.models.{ChatRooms, UserPublic}

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

  def setToken(token: String): Callback = Callback(TokenManager.update(token)) >> updateUserState(_.copy(tokenOption = Some(token)))

  def clearToken(): Callback = Callback(TokenManager.delete()) >> updateUserState(_.copy(tokenOption = None))

  def setUserInfo(userPublic: UserPublic): Callback = updateUserState(_.copy(publicOption = Some(userPublic)))

  def setJoinedRooms(joinedRooms: ChatRooms): Callback = updateChatState(_.copy(joinedChatRoomsOption = Some(joinedRooms)))

  def setAvailableRooms(availableRooms: ChatRooms): Callback = updateChatState(_.copy(availableChatRoomsOption = Some(availableRooms)))
}
