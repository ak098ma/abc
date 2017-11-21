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
    private val actions = new Actions((dispatch: State => State) => bs.modState(state => dispatch(state)).runNow())
    def render(props: Props, state: State) = props(state, actions)
  }

  private val store = ScalaComponent.builder[Props]("Store")
    .initialState(initialState)
    .backend(new Backend(_))
    .renderBackend
    .build

  def apply(connect: Props) = store(connect)
}

class Actions(private[this] val update: (State => State) => Unit) {

}
