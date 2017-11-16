package jp.co.applibot.abc.database.interface

import jp.co.applibot.abc.shared.models.{User, UserCredential, UserPublic}

import scala.concurrent.{ExecutionContext, Future}

trait UserStore {
  def add(user: User)(implicit executor: ExecutionContext): Future[User]
  def get(userCredential: UserCredential)(implicit executor: ExecutionContext): Future[Option[User]]
  def get(id: String)(implicit executor: ExecutionContext): Future[Option[UserPublic]]
  def delete(userCredential: UserCredential)(implicit executor: ExecutionContext): Future[Option[User]]
}
