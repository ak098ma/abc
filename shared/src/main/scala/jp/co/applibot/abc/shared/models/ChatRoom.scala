package jp.co.applibot.abc.shared.models

import play.api.libs.json.{Json, OFormat}

case class ChatRoom(id: String, title: String, users: Seq[String], isPrivate: Boolean)

object ChatRoom {
  implicit val format: OFormat[ChatRoom] = Json.format[ChatRoom]
}
