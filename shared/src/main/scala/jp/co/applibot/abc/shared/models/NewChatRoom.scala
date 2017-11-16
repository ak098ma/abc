package jp.co.applibot.abc.shared.models

import play.api.libs.json.{Json, OFormat}

case class NewChatRoom(title: String, users: Seq[String])

object NewChatRoom {
  implicit val format: OFormat[NewChatRoom] = Json.format[NewChatRoom]
}
