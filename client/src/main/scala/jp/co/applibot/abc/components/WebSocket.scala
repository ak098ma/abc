package jp.co.applibot.abc.components

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom._

import scala.scalajs.js

object WebSocket {

  case class Props(url: String, renderer: WebSocketState => VdomElement, messageHandler: MessageEvent => Callback)

  case class State(webSocketOption: Option[WebSocket] = None,
                   isOpen: Boolean = false,
                   closeEventOption: Option[CloseEvent] = None)

  case class WebSocketState(isConnected: Boolean,
                            isOpen: Boolean,
                            closeEventOption: Option[CloseEvent],
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
      closeEventOption = state.closeEventOption,
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

    private val onClose: js.Function1[CloseEvent, Unit] = (closeEvent) => bs.state.map { state =>
      state.webSocketOption.foreach { webSocket =>
        webSocket.removeEventListener("open", onOpen)
        webSocket.removeEventListener("error", onError)
        webSocket.removeEventListener("message", onMessage)
        webSocket.removeEventListener("close", onClose)
      }
      closeEvent.code match {
        case 1000 => // normal closure
        case 1006 => // closed abnormally ( unauthorized reaches here )
        // TODO: handle abnormal closure
        case _ =>
          throw new IllegalStateException(
            s"""WebSocket was closed with unexpected status:
               |code=${closeEvent.code},
               |reason=${closeEvent.reason}
               |wasClean=${closeEvent.wasClean}""".stripMargin)
      }
    }.runNow()

    private def close(code: Int) = bs.state.map(_.webSocketOption.foreach(_.close(code, "正常終了")))

    def componentWillMount: Callback = create

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
