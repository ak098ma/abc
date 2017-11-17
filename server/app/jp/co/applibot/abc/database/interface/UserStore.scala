package jp.co.applibot.abc.database.interface

import jp.co.applibot.abc.shared.models.{User, UserCredential, UserPublic}

import scala.concurrent.{ExecutionContext, Future}

trait UserStore {
  def add(user: User)(implicit executor: ExecutionContext): Future[User]
  def get(userCredential: UserCredential)(implicit executor: ExecutionContext): Future[Option[User]]
  def get(id: String)(implicit executor: ExecutionContext): Future[Option[UserPublic]]
  def update(id: String, modify: UserPublic => UserPublic)(implicit executionContext: ExecutionContext): Future[Option[UserPublic]]
  def update(userCredential: UserCredential, modify: User => User)(implicit executionContext: ExecutionContext): Future[Option[User]]
  def delete(userCredential: UserCredential)(implicit executor: ExecutionContext): Future[Option[User]]
}
