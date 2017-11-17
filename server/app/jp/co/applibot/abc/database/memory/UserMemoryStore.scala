package jp.co.applibot.abc.database.memory

import javax.inject._

import jp.co.applibot.abc.database.interface.UserStore
import jp.co.applibot.abc.shared.models.{User, UserCredential, UserPublic}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserMemoryStore extends UserStore {
  private var users: Seq[User] = Seq.empty[User]// :+ User("test1", "test1", "test1", Seq.empty) :+ User("test2", "test2", "test2", Seq.empty)

  override def add(user: User)(implicit executor: ExecutionContext): Future[User] = Future {
    users = user +: users
    user
  }

  override def get(userCredential: UserCredential)(implicit executor: ExecutionContext): Future[Option[User]] = Future {
    users.find(user => user.id == userCredential.id && user.password == userCredential.password)
  }

  override def get(id: String)(implicit executor: ExecutionContext): Future[Option[UserPublic]] = Future {
    users.find(_.id == id).map(user => UserPublic(id = user.id, nickname = user.nickname, joiningChatRooms = user.joiningChatRooms))
  }

  override def delete(userCredential: UserCredential)(implicit executor: ExecutionContext): Future[Option[User]] = Future {
    val (toDelete, toPersist) = users.partition(user => user.id == userCredential.id && user.password == userCredential.password)
    users = toPersist
    toDelete.headOption
  }

  override def update(id: String, modify: UserPublic => UserPublic)(implicit executionContext: ExecutionContext): Future[Option[UserPublic]] = Future {
    val (target, persist) = users.partition(_.id == id)
    target
      .headOption
      .map(user => modify(UserPublic(id = user.id, nickname = user.nickname, joiningChatRooms = user.joiningChatRooms)) -> user.password)
      .map{ arg =>
        val (userPublic, password) = arg
        users = User(id = userPublic.id, nickname = userPublic.nickname, password = password, joiningChatRooms = userPublic.joiningChatRooms) +: persist
        userPublic
      }
  }

  override def update(userCredential: UserCredential, modify: User => User)(implicit executionContext: ExecutionContext): Future[Option[User]] = Future {
    val (target, persist) = users.partition(_.id == userCredential.id)
    target
      .headOption
      .map(modify)
      .map{ user =>
        users = user +: persist
        user
      }
  }
}
