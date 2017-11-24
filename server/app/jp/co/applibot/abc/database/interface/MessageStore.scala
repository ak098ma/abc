package jp.co.applibot.abc.database.interface

import jp.co.applibot.abc.shared.models.{Message, NewMessage}

import scala.concurrent.{ExecutionContext, Future}

trait MessageStore {

  /**
    * 任意のメッセージのリストを返す。
    *
    * @param chatRoomId メッセージが所属するチャットルームのID。
    * @param from  メッセージのindex。このindexのメッセージを含む。
    * @param until メッセージのindex。このindexのメッセージは含まない。
    * @return fromから、until未満のindexのメッセージのリスト。
    */
  def get(chatRoomId: String, from: Int, until: Int)(implicit ec: ExecutionContext): Future[Seq[Message]]

  def add(chatRoomId: String, newMessage: String, userId: String)(implicit ec: ExecutionContext): Future[Message]

  def delete(chatRoomId: String, message: Message)(implicit ec: ExecutionContext): Future[Option[Message]]
}
