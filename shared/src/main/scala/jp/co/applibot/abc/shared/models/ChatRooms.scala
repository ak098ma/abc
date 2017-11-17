package jp.co.applibot.abc.shared.models

import play.api.libs.json.{Json, OFormat}

case class ChatRooms(rooms: Seq[ChatRoom]) {
  def add(chatRoom: ChatRoom): ChatRooms = {
    ChatRooms(chatRoom +: rooms)
  }

  def remove(chatRoom: ChatRoom): ChatRooms = {
    ChatRooms(rooms.filterNot(_.id == chatRoom.id))
  }
}

object ChatRooms {
  implicit val format: OFormat[ChatRooms] = Json.format[ChatRooms]
}
