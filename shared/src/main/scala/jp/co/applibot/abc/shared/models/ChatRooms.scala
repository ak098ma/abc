package jp.co.applibot.abc.shared.models

import play.api.libs.json.{Json, OFormat}

case class ChatRooms(rooms: Seq[ChatRoom])

object ChatRooms {
  implicit val format: OFormat[ChatRooms] = Json.format[ChatRooms]
}
