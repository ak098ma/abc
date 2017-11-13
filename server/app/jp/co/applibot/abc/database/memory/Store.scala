package jp.co.applibot.abc.database.memory

import jp.co.applibot.abc.database.interface.UserStore
import jp.co.applibot.abc.shared.models.{User, UserCredential}

import scala.concurrent.{ExecutionContext, Future}

class Store extends UserStore {
  private var users: Seq[User] = Seq.empty[User]

  override def add(user: User)(implicit executor: ExecutionContext): Future[Boolean] = Future {
    users = users :+ user
    println(users)
    true
  }

  override def get(userCredential: UserCredential)(implicit executor: ExecutionContext): Future[Option[User]] = Future {
    users.find(user => user.id == userCredential.id && user.password == userCredential.password)
  }

  override def delete(userCredential: UserCredential)(implicit executor: ExecutionContext): Future[Boolean] = Future {
    val initialLength = users.length
    users = users.filterNot(user => user.id == userCredential.id && user.password == userCredential.password)
    val resultLength = users.length
    initialLength != resultLength
  }
}
