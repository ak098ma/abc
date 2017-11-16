package jp.co.applibot.abc.database.memory

import java.util.UUID
import javax.inject.Singleton

import jp.co.applibot.abc.database.interface.ChatRoomStore
import jp.co.applibot.abc.shared.models.{ChatRoom, NewChatRoom}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ChatRoomMemoryStore extends ChatRoomStore {
  private var chatRooms: Seq[ChatRoom] = Seq.empty[ChatRoom]

  override def get(userId: String)(implicit ec: ExecutionContext): Future[Seq[ChatRoom]] = Future {
    chatRooms.filter(_.users.exists(_ == userId))
  }

  override def add(newChatRoom: NewChatRoom)(implicit ec: ExecutionContext): Future[ChatRoom] = Future {
    val chatRoom = ChatRoom(id = UUID.randomUUID().toString, title = newChatRoom.title, users = newChatRoom.users)
    chatRooms = chatRoom +: chatRooms
    chatRoom
  }

  override def delete(chatRoomId: String)(implicit ec: ExecutionContext): Future[Option[ChatRoom]] = Future {
    val (toDelete, toPersist) = chatRooms.partition(_.id == chatRoomId)
    chatRooms = toPersist
    toDelete.headOption
  }
}
