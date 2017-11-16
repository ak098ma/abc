package jp.co.applibot.abc.database.memory

import java.util.{Date, UUID}
import javax.inject.Singleton

import jp.co.applibot.abc.database.interface.MessageStore
import jp.co.applibot.abc.shared.models.{Message, NewMessage}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class MessageMemoryStore extends MessageStore {
  private var messages: Seq[Message] = Seq.empty[Message]

  override def get(from: Int, until: Int)(implicit ec: ExecutionContext): Future[Seq[Message]] = Future {
    messages.slice(from, until)
  }

  override def add(message: NewMessage)(implicit ec: ExecutionContext): Future[Boolean] = Future {
    messages = Message(UUID.randomUUID().toString, message.message, new Date) +: messages
    true
  }

  override def delete(message: Message)(implicit ec: ExecutionContext): Future[Option[Message]] = Future {
    val (toDelete, toPersist) = messages.partition(_ == message)
    messages = toPersist
    toDelete.headOption
  }
}
