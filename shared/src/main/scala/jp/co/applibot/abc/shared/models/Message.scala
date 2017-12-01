package jp.co.applibot.abc.shared.models

import play.api.libs.json.Json

case class Message(id: String, userId: String, message: String, timestamp: Long)

object Message {
  implicit val format = Json.format[Message]
}
