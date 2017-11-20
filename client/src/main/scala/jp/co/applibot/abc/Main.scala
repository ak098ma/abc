package jp.co.applibot.abc

import japgolly.scalajs.react._
import jp.co.applibot.abc.models.{ChatState, LoginState, SignUpState, State}
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom

object Main {
  val reactRootElement: dom.Element = dom.document.getElementById("react-root")

  val initialState = State(
    router = None,
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
    )
  )

  class Backend(bs: BackendScope[Unit, State]) {
    def render(state: State) = {
      <.div(
        "Main"
      )
    }
  }

  def main(args: Array[String]): Unit = {
    val main = ScalaComponent.builder[Unit]("Main")
      .initialState(initialState)
      .backend(new Backend(_))
      .renderBackend
      .build

    main().renderIntoDOM(reactRootElement)
  }
}
