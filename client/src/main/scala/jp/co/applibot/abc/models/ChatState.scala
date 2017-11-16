package jp.co.applibot.abc.models

import jp.co.applibot.abc.shared.models.{ChatRoom, ChatRooms, UserPublic}

case class ChatState(userPublicOption: Option[UserPublic],
                     chatRoomsOption: Option[ChatRooms],
                     isCreateNewChatRoomDialogOpen: Boolean,
                     titleOfNewChatRoom: String,
                     selectedChatRoomOption: Option[ChatRoom])
