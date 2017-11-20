package jp.co.applibot.abc

import japgolly.scalajs.react.vdom.html_<^._
import jp.co.applibot.abc.models.{ChatState, LoginState, SignUpState, State}
import jp.co.applibot.abc.pages._
import jp.co.applibot.react.Router
import org.scalajs.dom

object Main {
  val reactRootElement: dom.Element = dom.document.getElementById("react-root")

  val initialState = State(
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
  )

  def main(args: Array[String]): Unit = {
    <.div(
      Router { router =>
        router.pathname match {
          case "/" => Home(router)
          case "/sign-up" => SignUp(router)
          case "/login" => Login(router)
          case "/chat" => Chat(router)
          case _ => NotFound(router)
        }
      }
    ).renderIntoDOM(reactRootElement)
  }
}
