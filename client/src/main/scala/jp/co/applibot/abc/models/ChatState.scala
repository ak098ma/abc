package jp.co.applibot.abc.models

import jp.co.applibot.abc.shared.models._

case class ChatState(users: Map[String, UserPublic],
                     joinedChatRoomsOption: Option[ChatRooms],
                     availableChatRoomsOption: Option[ChatRooms],
                     isCreateNewChatRoomDialogOpen: Boolean,
                     titleOfNewChatRoom: String,
                     selectedChatRoomOption: Option[ChatRoom],
                     messages: Map[String, Seq[ReceivedMessage]],
                     editingMessage: String)
