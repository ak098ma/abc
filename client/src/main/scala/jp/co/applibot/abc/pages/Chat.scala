package jp.co.applibot.abc.pages

import japgolly.scalajs.react._
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.vdom.html_<^._
import jp.co.applibot.abc.actions.ChatActions
import jp.co.applibot.abc.components.WebSocket
import jp.co.applibot.abc.models.Props
import jp.co.applibot.abc.shared.styles
import org.scalajs.dom.window

import scalacss.ScalaCssReact._

object Chat {

  class Backend(bs: BackendScope[Props, Unit]) {

    def render(props: Props): VdomElement = {
      props.state.user.tokenOption match {
        case None =>
          <.div("ログインしてください。")
        case Some(token) =>
          WebSocket(WebSocket.Props(
            url = s"ws://${window.location.hostname}:${window.location.port}/chat/socket?token=$token",
            renderer = (rendererProps) => {
              val chatActions = new ChatActions(props, rendererProps)
              renderContent(props, rendererProps, chatActions)
            },
            messageHandler = (messageEvent) => Callback.empty
          ))
      }
    }

    def renderContent(props: Props, state: WebSocket.WebSocketState, actions: ChatActions): VdomElement = {
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
            "your info"
          ),
          <.div(
            styles.Chat.members,
          ),
        )
      )
    }
  }

  private val chat = ScalaComponent.builder[Props]("Chat")
    .stateless
    .backend(new Backend(_))
    .renderBackend
    .build

  def apply(props: Props): Unmounted[Props, Unit, Backend] = chat(props)
}
