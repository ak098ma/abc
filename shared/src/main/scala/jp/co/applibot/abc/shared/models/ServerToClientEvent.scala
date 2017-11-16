package jp.co.applibot.abc.shared.models

import play.api.libs.json.Json

case class ServerToClientEvent(userPublicOption: Option[UserPublic] = None,
                               chatRoomsOption: Option[ChatRooms] = None,
                               newChatRoomOption: Option[ChatRoom] = None,
                               receivedMessageOption: Option[Message] = None)

object ServerToClientEvent {
  implicit val format = Json.format[ServerToClientEvent]
}
