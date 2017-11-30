package jp.co.applibot.abc.pages

import japgolly.scalajs.react._
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.vdom.html_<^._
import jp.co.applibot.abc.actions.ChatActions
import jp.co.applibot.abc.models.Props
import jp.co.applibot.abc.shared.models.{ChatRoom, ChatRooms}
import jp.co.applibot.abc.shared.styles
import org.scalajs.dom._

import scala.scalajs.js
import scalacss.ScalaCssReact._

object Chat {

  case class State(webSocketOption: Option[WebSocket] = None,
                   isOpen: Boolean = false)

  class Backend(bs: BackendScope[Props, State]) {

    val onOpen: js.Function1[Event, Unit] = (_) => {
      bs.modState(_.copy(isOpen = false)).runNow()
    }
    val onMessage: js.Function1[MessageEvent, Unit] = (messageEvent) => {
      bs.props.flatMap { props => bs.state.map { state =>
        new ChatActions(props, state).handleMessage(messageEvent.data.toString)
      }}.runNow()
    }
    val onError: js.Function1[Event, Unit] = (_) => {}
    val onClose: js.Function1[CloseEvent, Unit] = (closeEvent) => {
      removeAllWebSocketListeners.runNow()
      closeEvent.code match {
        case 1000 =>
          console.log(s"successfully closed websocket. reason is: ${closeEvent.reason}")
        case 1006 =>
          console.log(s"websocket closed with reason: ${closeEvent.reason}")
          bs.modState(_.copy(webSocketOption = None), bs.props.flatMap { props =>
            props.state.user.tokenOption match {
              case None =>
                props.router.push("/login")
              case Some(token) =>
                props.webActions.refreshToken(initWebSocket(token))
            }
          }).runNow()
        case x =>
          console.error(s"unexpected websocket close code $x, reason is: ${closeEvent.reason}")
      }
    }

    def removeAllWebSocketListeners = bs.state.map { state =>
      state.webSocketOption.foreach { webSocket =>
        webSocket.removeEventListener("open", onOpen)
        webSocket.removeEventListener("message", onMessage)
        webSocket.removeEventListener("error", onError)
        webSocket.removeEventListener("close", onClose)
      }
    }

    def initWebSocket(token: String) = {
      val webSocket = new WebSocket(s"ws://${window.location.hostname}:${window.location.port}/chat/socket?token=$token")
      webSocket.addEventListener("open", onOpen)
      webSocket.addEventListener("message", onMessage)
      webSocket.addEventListener("error", onError)
      webSocket.addEventListener("close", onClose)
      bs.modState(_.copy(webSocketOption = Some(webSocket)))
    }

    def componentWillMount = bs.props.flatMap { props =>
      props.state.user.tokenOption match {
        case None =>
          props.router.push("/login")
        case Some(token) =>
          initWebSocket(token)
      }
    }

    def render(props: Props, state: State): VdomElement = {
      val chatActions = new ChatActions(props, state)
      <.div(
        styles.Chat.root,
        <.section(
          styles.Chat.rooms,
          <.nav(
            styles.Chat.roomNav,
            <.button(
              styles.Chat.addRoomButton,
            ),
          ),
          <.ul(
            styles.Chat.roomList,
            renderRooms(props.state.chat.joinedChatRoomsOption, props.state.chat.availableChatRoomsOption),
          ),
        ),
        <.section(
          styles.Chat.chat,
          <.nav(
            styles.Chat.chatTitle,
          ),
          <.div(
            styles.Chat.messages,
          ),
          <.div(
            styles.Chat.chatController,
            <.input(
              styles.Chat.inputMessage,
              ^.placeholder := "何か発言してみよう！",
            ),
            <.button(
              styles.Chat.sendButton,
            ),
          ),
        ),
        <.section(
          styles.Chat.users,
          <.nav(
            styles.Chat.you,
            props.state.user.publicOption.map(userPublic => <.label(s"${userPublic.nickname} さん")).getOrElse(EmptyVdom),
          ),
          <.div(
            styles.Chat.members,
          ),
        )
      )
    }

    def renderRooms(joinedRoomsOption: Option[ChatRooms], availableRoomsOption: Option[ChatRooms]) = {
      joinedRoomsOption.flatMap { joinedRooms =>
        availableRoomsOption.map { availableRooms =>
          joinedRooms.rooms.map((true, _)) ++ availableRooms.rooms.map((false, _))
        }
      }.map { combinedRooms =>
        if (combinedRooms.isEmpty) {
          <.li("参加可能な部屋がありません。上の + マークから部屋を作成しましょう。")
        } else {
          combinedRooms.map { case (isJoinedRoom, chatRoom) =>
            <.li(
              ^.key := s"room_${chatRoom.id}",
              chatRoom.title
            )
          }.toVdomArray
        }
      }.getOrElse(<.li("読み込み中です..."))
    }

    def componentWillUnmount = bs.state.map(_.webSocketOption.foreach(_.close(1000, "正常終了")))
  }

  private val chat = ScalaComponent.builder[Props]("Chat")
    .initialState(State())
    .backend(new Backend(_))
    .renderBackend
    .componentWillMount(_.backend.componentWillMount)
    .build

  def apply(props: Props): Unmounted[Props, State, Backend] = chat(props)
}
