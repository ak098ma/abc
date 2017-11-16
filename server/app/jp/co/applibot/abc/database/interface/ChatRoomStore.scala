package jp.co.applibot.abc.database.interface

import jp.co.applibot.abc.shared.models.{ChatRoom, NewChatRoom}

import scala.concurrent.{ExecutionContext, Future}

trait ChatRoomStore {
  def get(userId: String)(implicit ec: ExecutionContext): Future[Seq[ChatRoom]]
  def add(newChatRoom: NewChatRoom)(implicit ec: ExecutionContext): Future[ChatRoom]
  def delete(chatRoomId: String)(implicit ec: ExecutionContext): Future[Option[ChatRoom]]
}
