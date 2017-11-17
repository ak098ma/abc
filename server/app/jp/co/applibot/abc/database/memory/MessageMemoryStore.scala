package jp.co.applibot.abc.database.memory

import java.util.{Date, UUID}
import javax.inject.Singleton

import jp.co.applibot.abc.database.interface.MessageStore
import jp.co.applibot.abc.shared.models.{Message, NewMessage}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class MessageMemoryStore extends MessageStore {
  private var messages: Map[String, Seq[Message]] = Map.empty[String, Seq[Message]]

  override def get(chatRoomId: String, from: Int, until: Int)(implicit ec: ExecutionContext): Future[Seq[Message]] = Future {
    messages.get(chatRoomId).toSeq.flatten.slice(from, until)
  }

  override def add(chatRoomId: String, newMessage: String)(implicit ec: ExecutionContext): Future[Message] = Future {
    val msgs = messages.getOrElse(chatRoomId, Seq.empty)
    val message = Message(id = UUID.randomUUID().toString, message = newMessage, timestamp = (new Date).getTime)
    messages = messages.updated(chatRoomId, message +: msgs)
    message
  }

  override def delete(chatRoomId: String, message: Message)(implicit ec: ExecutionContext): Future[Option[Message]] = Future {
    messages.get(chatRoomId).flatMap { msgs =>
      val (toDelete, toPersist) = msgs.partition(_ == message)
      if (toPersist.isEmpty) {
        messages = messages - chatRoomId
      } else {
        messages = messages.updated(chatRoomId, toPersist)
      }
      toDelete.headOption
    }
  }
}
