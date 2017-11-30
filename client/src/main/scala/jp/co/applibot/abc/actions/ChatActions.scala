package jp.co.applibot.abc.actions

import jp.co.applibot.abc.models.Props
import jp.co.applibot.abc.pages.Chat
import jp.co.applibot.abc.shared.models.{ClientToServerEvent, NewChatRoom, ServerToClientEvent}
import play.api.libs.json.{JsError, JsSuccess, Json}
import org.scalajs.dom.window.console

class ChatActions(props: Props, state: Chat.State) {
  def handleMessage(message: String) = {
    Json.fromJson[ServerToClientEvent](Json.parse(message)) match {
      case JsError(errors) =>
        console.error(errors)
      case JsSuccess(event, _) =>
        event.userPublicOption.foreach(props.actions.setUserInfo(_).runNow())
        event.joinedRoomsOption.foreach(props.actions.setJoinedRooms(_).runNow())
        event.availableRoomsOption.foreach(props.actions.setAvailableRooms(_).runNow())
    }
  }

  def createRoom(title: String) = {
    state.webSocketOption.foreach { webSocket =>
      val newChatRoom = NewChatRoom(
        title = title,
        users = Seq.empty[String],
        isPrivate = false,
      )
      webSocket.send(Json.stringify(Json.toJson(ClientToServerEvent(createRoomOption = Some(newChatRoom)))))
    }
    props.actions.closeModal()
  }
}
