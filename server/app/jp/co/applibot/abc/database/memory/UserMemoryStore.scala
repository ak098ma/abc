package jp.co.applibot.abc.database.memory

import javax.inject._

import jp.co.applibot.abc.database.interface.UserStore
import jp.co.applibot.abc.shared.models.{User, UserCredential, UserPublic}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserMemoryStore extends UserStore {
  private var users: Seq[User] = Seq.empty[User] :+ User(id = "test", nickname = "test", password = "test")

  override def add(user: User)(implicit executor: ExecutionContext): Future[User] = Future {
    users = user +: users
    user
  }

  override def get(userCredential: UserCredential)(implicit executor: ExecutionContext): Future[Option[User]] = Future {
    users.find(user => user.id == userCredential.id && user.password == userCredential.password)
  }

  override def get(id: String)(implicit executor: ExecutionContext): Future[Option[UserPublic]] = Future {
    users.find(_.id == id).map(user => UserPublic(id = user.id, nickname = user.nickname))
  }

  override def delete(userCredential: UserCredential)(implicit executor: ExecutionContext): Future[Option[User]] = Future {
    val (toDelete, toPersist) = users.partition(user => user.id == userCredential.id && user.password == userCredential.password)
    users = toPersist
    toDelete.headOption
  }
}
