package jp.co.applibot.abc

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.VdomElement
import jp.co.applibot.abc.models.{ChatState, LoginState, SignUpState, State}

object Store {
  type Props = (State, Actions) => VdomElement

  private val initialState = State(
    login = LoginState(
      id = "",
      password = "",
      None,
    ),
    signUp = SignUpState(
      id = "",
      nickname = "",
      password = "",
    ),
    chat = ChatState(
      userPublicOption = None,
      joinedChatRoomsOption = None,
      availableChatRoomsOption = None,
      isCreateNewChatRoomDialogOpen = false,
      titleOfNewChatRoom = "",
      selectedChatRoomOption = None,
      messages = Map.empty,
      editingMessage = "",
    )
  )

  class Backend(bs: BackendScope[Props, State]) {
    private val actions = new Actions((dispatch: State => State) => bs.modState(state => dispatch(state)))
    def render(props: Props, state: State) = props(state, actions)
  }

  private val store = ScalaComponent.builder[Props]("Store")
    .initialState(initialState)
    .backend(new Backend(_))
    .renderBackend
    .build

  def apply(connect: Props) = store(connect)
}

class Actions(private[this] val update: (State => State) => Callback) {
  private def updateLoginState(updateLogin: LoginState => LoginState): Callback = update(state => state.copy(login = updateLogin(state.login)))
  private def updateSignUpState(updateSignUp: SignUpState => SignUpState): Callback = update(state => state.copy(signUp = updateSignUp(state.signUp)))
  private def updateChatState(updateChat: ChatState => ChatState): Callback = update(state => state.copy(chat = updateChat(state.chat)))

  def setSignUpId(id: String): Callback = updateSignUpState(_.copy(id = id))
  def setSignUpNickname(nickname: String): Callback = updateSignUpState(_.copy(nickname = nickname))
  def setSignUpPassword(password: String): Callback = updateSignUpState(_.copy(password = password))

  def setLoginId(id: String): Callback = updateLoginState(_.copy(id = id))
  def setLoginPassword(password: String): Callback = updateLoginState(_.copy(password = password))
}
