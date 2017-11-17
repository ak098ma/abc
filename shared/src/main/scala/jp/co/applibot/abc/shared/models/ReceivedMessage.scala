package jp.co.applibot.abc.shared.models

import play.api.libs.json.Json

case class ReceivedMessage(chatRoomId: String, messageId: String, message: String, timestamp: Long)

object ReceivedMessage {
  implicit val format = Json.format[ReceivedMessage]
}
