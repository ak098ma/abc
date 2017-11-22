package jp.co.applibot.abc

import japgolly.scalajs.react.vdom.html_<^._
import jp.co.applibot.abc.components.Layout
import jp.co.applibot.abc.models._
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
      messages = Map.empty,
      editingMessage = "",
    ),
  )

  def main(args: Array[String]): Unit = {
    Store { (state, actions) =>
      Router { router =>
        val props = Props(
          state = state,
          actions = actions,
          router = router,
        )
        val content = router.pathname match {
          case "/" => Home(props)
          case "/sign-up" => SignUp(props)
          case "/login" => Login(props)
          case "/chat" => Chat(props)
          case _ => NotFound(props)
        }
        val layoutProps = Layout.Props(
          content = content,
          router = router,
        )
        Layout(layoutProps)
      }
    }.renderIntoDOM(reactRootElement)
  }
}
