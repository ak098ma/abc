package jp.co.applibot.abc.shared.models

import play.api.libs.json.Json

case class ServerToClientEvent(userPublicOption: Option[UserPublic] = None,
                               joinedRoomsOption: Option[ChatRooms] = None,
                               availableRoomsOption: Option[ChatRooms] = None,
                               newChatRoomOption: Option[ChatRoom] = None,
                               receivedMessagesOption: Option[Seq[ReceivedMessage]] = None,
                               receivedMessageOption: Option[ReceivedMessage] = None)

object ServerToClientEvent {
  implicit val format = Json.format[ServerToClientEvent]
}
