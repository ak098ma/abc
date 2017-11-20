package jp.co.applibot.abc

import jp.co.applibot.abc.models._
import jp.co.applibot.flux

object Store extends flux.Store(State(
  login = LoginState(
    id = "",
    password = "",
    tokenOption = TokenManager.getToken),
  signUp = SignUpState(
    id = "",
    nickname = "",
    password = ""),
  chat = ChatState(
    userPublicOption = None,
    joinedChatRoomsOption = None,
    availableChatRoomsOption = None,
    isCreateNewChatRoomDialogOpen = false,
    titleOfNewChatRoom = "",
    selectedChatRoomOption = None,
    webSocketOption = None,
    messages = Map.empty,
    editingMessage = "",
  ),
)) {
  def updateChatState(modify: ChatState => ChatState): Unit = update(state => state.copy(chat = modify(state.chat)))
  def updateLoginState(modify: LoginState => LoginState): Unit = update(state => state.copy(login = modify(state.login)))
  def updateSignUpState(modify: SignUpState => SignUpState): Unit = update(state => state.copy(signUp = modify(state.signUp)))
}
