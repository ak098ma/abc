package jp.co.applibot.abc

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.VdomElement
import jp.co.applibot.abc.actions.Actions
import jp.co.applibot.abc.models._
import jp.co.applibot.abc.shared.models.UserPublic

object Store {
  type Props = (State, Actions) => VdomElement

  private val initialState = State(
    modal = ModalState(
      renderModalOption = None,
    ),
    login = LoginState(
      id = "",
      password = "",
    ),
    signUp = SignUpState(
      id = "",
      nickname = "",
      password = "",
    ),
    chat = ChatState(
      users = Map.empty[String, UserPublic],
      joinedChatRoomsOption = None,
      availableChatRoomsOption = None,
      isCreateNewChatRoomDialogOpen = false,
      titleOfNewChatRoom = "",
      selectedChatRoomOption = None,
      messages = Map.empty,
      editingMessage = "",
    ),
    user = UserState(
      tokenOption = TokenManager.getToken,
      publicOption = None,
    ),
  )

  class Backend(bs: BackendScope[Props, State]) {
    private val actions = new Actions((update: State => State) => bs.modState(update))

    def render(props: Props, state: State) = props(state, actions)
  }

  private val store = ScalaComponent.builder[Props]("Store")
    .initialState(initialState)
    .backend(new Backend(_))
    .renderBackend
    .build

  def apply(connect: Props) = store(connect)
}
