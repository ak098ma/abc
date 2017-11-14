package jp.co.applibot.abc.database.memory

import javax.inject._

import jp.co.applibot.abc.database.interface.UserStore
import jp.co.applibot.abc.shared.models.{User, UserCredential, UserPublic}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserMemoryStore extends UserStore {
  private var users: Seq[User] = Seq.empty[User]

  override def add(user: User)(implicit executor: ExecutionContext): Future[Boolean] = Future {
    users = users :+ user
    true
  }

  override def get(userCredential: UserCredential)(implicit executor: ExecutionContext): Future[Option[User]] = Future {
    users.find(user => user.id == userCredential.id && user.password == userCredential.password)
  }

  override def get(id: String)(implicit executor: ExecutionContext): Future[Option[UserPublic]] = Future {
    users.find(_.id == id).map(user => UserPublic(id = user.id, nickname = user.nickname))
  }

  override def delete(userCredential: UserCredential)(implicit executor: ExecutionContext): Future[Boolean] = Future {
    val initialLength = users.length
    users = users.filterNot(user => user.id == userCredential.id && user.password == userCredential.password)
    val resultLength = users.length
    initialLength != resultLength
  }
}
