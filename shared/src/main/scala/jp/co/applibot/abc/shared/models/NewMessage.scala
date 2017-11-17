package jp.co.applibot.abc.shared.models

import play.api.libs.json.Json

case class NewMessage(chatRoomId: String, message: String)

object NewMessage {
  implicit val format = Json.format[NewMessage]
}
