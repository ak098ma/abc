package jp.co.applibot.abc.models

import jp.co.applibot.abc.shared.models.{ChatRoom, ChatRooms, Message, UserPublic}
import org.scalajs.dom.raw.WebSocket
import play.api.libs.json.Json

case class ChatState(userPublicOption: Option[UserPublic],
                     joinedChatRoomsOption: Option[ChatRooms],
                     availableChatRoomsOption: Option[ChatRooms],
                     isCreateNewChatRoomDialogOpen: Boolean,
                     titleOfNewChatRoom: String,
                     selectedChatRoomOption: Option[ChatRoom],
                     webSocketOption: Option[WebSocket],
                     messages: Map[String, Seq[Message]],
                     editingMessage: String)
