package jp.co.applibot.abc.actions

import jp.co.applibot.abc.models.Props
import jp.co.applibot.abc.pages.Chat
import jp.co.applibot.abc.shared.models._
import play.api.libs.json.{JsError, JsSuccess, Json}
import org.scalajs.dom.window.console

class ChatActions(props: Props, state: Chat.State) {
  def handleMessage(message: String) = {
    Json.fromJson[ServerToClientEvent](Json.parse(message)) match {
      case JsError(errors) =>
        console.error(errors)
      case JsSuccess(event, _) =>

        event.userPublicOption.foreach(props.actions.setUserInfo(_).runNow())

        event.joinedRoomsOption.foreach { joinedRooms =>
          joinedRooms.rooms.foreach { chatRoom =>
            state.webSocketOption.foreach { webSocket =>
              webSocket.send(Json.stringify(
                Json.toJson(ClientToServerEvent(subscribeRoomOption = Some(chatRoom)))
              ))
            }
          }
          props.actions.setJoinedRooms(joinedRooms).runNow()
        }

        event.availableRoomsOption.foreach(props.actions.setAvailableRooms(_).runNow())

        event.receivedMessagesOption.foreach(props.actions.setReceivedMessages(_).runNow())

        event.receivedMessageOption.foreach(props.actions.setReceivedMessage(_).runNow())
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

  def sendMessage(chatRoomId: String, message: String) = {
    val newMessage = NewMessage(chatRoomId = chatRoomId, message = message)
    state.webSocketOption.foreach { webSocket =>
      webSocket.send(Json.stringify(
        Json.toJson(ClientToServerEvent(newMessageOption = Some(newMessage)))
      ))
    }
    props.actions.setEditingMessage("")
  }
}
