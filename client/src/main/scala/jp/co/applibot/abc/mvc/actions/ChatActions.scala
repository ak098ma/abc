package jp.co.applibot.abc.mvc.actions

import jp.co.applibot.abc.Store
import jp.co.applibot.abc.shared.models.{ChatRoom, ClientToServerEvent}
import org.scalajs.dom.raw.{CloseEvent, ErrorEvent, MessageEvent, WebSocket}
import org.scalajs.dom.{Event, window}
import play.api.libs.json.Json

import scala.scalajs.js

object ChatActions {
  def openCreateChatRoomDialog(): Unit = {
    Store.updateChatState(_.copy(isCreateNewChatRoomDialogOpen = true))
  }

  def closeCreateChatRoomDialog(): Unit = {
    Store.updateChatState(_.copy(isCreateNewChatRoomDialogOpen = false))
  }

  def setTitleOfNewChatRoom(title: String): Unit = {
    Store.updateChatState(_.copy(titleOfNewChatRoom = title))
  }

  def showRoom(chatRoom: ChatRoom): Unit = {
    Store.updateChatState(_.copy(selectedChatRoomOption = Some(chatRoom)))
  }

  private def closeWebSocket(): Unit = Store.getState.chat.webSocketOption.foreach(_.close(code = 1000, reason = "unload"))
  val unload: js.Function1[Event, Unit] = (_) => closeWebSocket()

  def createWebSocket(): Unit = {
    if (Store.getState.chat.webSocketOption.isEmpty) {
      val socket = new WebSocket(s"ws://${window.location.hostname}:${window.location.port}/chat/socket")
      socket.addEventListener("open", { _: Event =>
        socket.send(Json.toJson(ClientToServerEvent("ほにゃ")).toString())
      })
      socket.addEventListener("message", { event: MessageEvent =>
        println(event)
      })
      socket.addEventListener("error", { event: ErrorEvent =>
        window.console.error(event)
      })
      socket.addEventListener("close", { event: CloseEvent =>
        window.console.warn(event)
      })
      window.addEventListener("unload", unload)
      Store.updateChatState(_.copy(webSocketOption = Some(socket)))
    }
  }

  def deleteWebSocket(): Unit = {
    window.removeEventListener("unload", unload)
    closeWebSocket()
  }
}
