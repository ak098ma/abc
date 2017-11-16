package jp.co.applibot.abc.shared.models

import play.api.libs.json.Json

object ClientToServerEvent {
  implicit val format = Json.format[ClientToServerEvent]
}

case class ClientToServerEvent(joinRoomOption: Option[String] = None,
                               leaveRoomOption: Option[String] = None,
                               createRoomOption: Option[NewChatRoom] = None) {
  def stringify: String = Json.stringify(Json.toJson(this))
}
