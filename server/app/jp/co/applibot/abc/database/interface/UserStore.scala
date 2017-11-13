package jp.co.applibot.abc.database.interface

import jp.co.applibot.abc.shared.models.{User, UserCredential}

import scala.concurrent.{ExecutionContext, Future}

trait UserStore {
  def add(user: User)(implicit executor: ExecutionContext): Future[Boolean]
  def get(userCredential: UserCredential)(implicit executor: ExecutionContext): Future[Option[User]]
  def delete(userCredential: UserCredential)(implicit executor: ExecutionContext): Future[Boolean]
}
