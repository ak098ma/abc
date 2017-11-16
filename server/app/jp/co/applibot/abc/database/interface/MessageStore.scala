package jp.co.applibot.abc.database.interface

import jp.co.applibot.abc.shared.models.{Message, NewMessage}

import scala.concurrent.{ExecutionContext, Future}

trait MessageStore {

  /**
    * 任意のメッセージのリストを返す。
    *
    * @param from  メッセージのindex。このindexのメッセージを含む。
    * @param until メッセージのindex。このindexのメッセージは含まない。
    * @return fromから、until未満のindexのメッセージのリスト。
    */
  def get(from: Int, until: Int)(implicit ec: ExecutionContext): Future[Seq[Message]]

  def add(message: NewMessage)(implicit ec: ExecutionContext): Future[Boolean]

  def delete(message: Message)(implicit ec: ExecutionContext): Future[Option[Message]]
}
