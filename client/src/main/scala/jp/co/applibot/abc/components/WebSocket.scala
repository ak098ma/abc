package jp.co.applibot.abc.components

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import jp.co.applibot.abc.actions.WebSocketActions
import org.scalajs.dom._

import scala.scalajs.js

object WebSocket {

  case class Props(url: String,
                   renderer: WebSocketState => VdomElement,
                   messageHandler: MessageEvent => Callback,
                   closeHandler: CloseEvent => Callback = (_) => Callback.empty)

  case class State(webSocketOption: Option[WebSocket] = None,
                   isOpen: Boolean = false)

  case class WebSocketState(isConnected: Boolean,
                            isOpen: Boolean,
                            webSocketActions: WebSocketActions)

  class Backend(bs: BackendScope[Props, State]) {
    private val webSocketActions = new WebSocketActions({ json =>
      bs.state.map { state =>
        state.webSocketOption.flatMap { ws =>
          if (state.isOpen) Some(ws)
          else None
        }.foreach(_.send(json))
      }
    })

    def render(props: Props, state: State) = props.renderer(WebSocketState(
      isConnected = state.webSocketOption.nonEmpty,
      isOpen = state.isOpen,
      webSocketActions,
    ))

    private def create = bs.props.flatMap { props =>
      bs.modState { state =>
        val webSocket = new WebSocket(props.url)
        webSocket.addEventListener("open", onOpen)
        webSocket.addEventListener("message", onMessage)
        webSocket.addEventListener("error", onError)
        webSocket.addEventListener("close", onClose)
        state.copy(webSocketOption = Some(webSocket))
      }
    }

    private val onError: js.Function1[Event, Unit] = (event) => console.error(event)

    private val onMessage: js.Function1[MessageEvent, Unit] = (event) => bs.props.flatMap(_.messageHandler(event)).runNow()

    private val onOpen: js.Function1[Event, Unit] = (_) => bs.modState(_.copy(isOpen = true)).runNow()

    private val onClose: js.Function1[CloseEvent, Unit] = (closeEvent) => bs.props.flatMap( props => bs.modState { state =>
      state.webSocketOption.foreach(removeAllListeners)
      props.closeHandler(closeEvent)
      state.copy(webSocketOption = None, isOpen = false)
    }).runNow()

    private def removeAllListeners(webSocket: WebSocket): Unit = {
      webSocket.removeEventListener("open", onOpen)
      webSocket.removeEventListener("error", onError)
      webSocket.removeEventListener("message", onMessage)
      webSocket.removeEventListener("close", onClose)
    }

    private def close(code: Int) = bs.state.map { state =>
      state.webSocketOption.foreach(removeAllListeners)
      state.webSocketOption.foreach(_.close(1000, "componentWillUnmount"))
    }

    def componentWillMount: Callback = create >> Callback{
      window.setTimeout(() => {
        close(1000).runNow()
      }, 3000)
    }

    def componentWillUnmount: Callback = close(1000)
  }

  private val webSocket = ScalaComponent.builder[Props]("WebSocket")
    .initialState(State())
    .backend(new Backend(_))
    .renderBackend
    .componentWillMount(_.backend.componentWillMount)
    .componentWillUnmount(_.backend.componentWillUnmount)
    .build

  def apply(props: Props) = webSocket(props)
}
