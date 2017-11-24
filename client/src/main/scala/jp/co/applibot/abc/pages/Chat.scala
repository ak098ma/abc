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
              <.div(
                styles.Chat.root,
                <.nav(
                  styles.Chat.localNavigation,
                  "local-navigation",
                ),
                <.div(
                  styles.Chat.container,
                  <.section(
                    styles.Chat.rooms,
                    <.div(
                      styles.Chat.roomsController,
                      <.button(
                        styles.Chat.addRoomButton,
                      ),
                    ),
                  ),
                  <.section(
                    styles.Chat.chat,
                    <.div(
                      styles.Chat.items,
                      "items",
                    ),
                    <.div(
                      styles.Chat.chatController,
                      "controller",
                    ),
                  )
                )
              )
            },
            messageHandler = (messageEvent) => Callback.empty
          ))
      }
    }
  }

  private val chat = ScalaComponent.builder[Props]("Chat")
    .stateless
    .backend(new Backend(_))
    .renderBackend
    .build

  def apply(props: Props): Unmounted[Props, Unit, Backend] = chat(props)
}
