package jp.co.applibot.abc.controllers.actors

import akka.actor._
import jp.co.applibot.abc.database.interface.{ChatRoomStore, UserStore}
import jp.co.applibot.abc.shared.models.{ChatRooms, NewChatRoom, ServerToClientEvent, UserPublic}

import scala.concurrent.ExecutionContext

sealed trait Target
case object Service extends Target
case class Room(id: String) extends Target

sealed trait SocketManagerEvent
case class CreateChatRoom(newChatRoom: NewChatRoom, actorRef: ActorRef) extends SocketManagerEvent
case class Join(target: Target, actorRef: ActorRef) extends SocketManagerEvent
case class Leave(target: Target, actorRef: ActorRef) extends SocketManagerEvent
case class Send(target: Target, message: String) extends SocketManagerEvent

class SocketManager(userStore: UserStore, chatRoomStore: ChatRoomStore)(implicit ec: ExecutionContext) extends Actor {
  private var everybody: Seq[ActorRef] = Seq.empty
  private var rooms: Map[String, Seq[ActorRef]] = Map.empty

  override def receive = {
    case Join(Service, actorRef) =>
      everybody = actorRef +: everybody
      actorRef ! ServerToClientEvent(userPublicOption = Some(UserPublic("testId", "testNickname")), chatRoomsOption = Some(ChatRooms(Seq())))
    case Leave(Service, actorRef) =>
      everybody = everybody.filterNot(_ == actorRef)
    case Join(Room(chatRoomId), actorRef) =>
      val actorRefs = rooms.getOrElse(chatRoomId, Seq(actorRef))
      rooms = rooms.updated(chatRoomId, actorRefs)
    case Leave(Room(chatRoomId), actorRef) =>
      rooms.get(chatRoomId).foreach{ actorRefs =>
        val left = actorRefs.filterNot(_ == actorRef)
        rooms = if (left.isEmpty) {
          rooms - chatRoomId
        } else {
          rooms.updated(chatRoomId, left)
        }
      }
    case CreateChatRoom(newChatRoom, actorRef) =>
      chatRoomStore.add(newChatRoom).foreach{ chatRoom =>
        actorRef ! ServerToClientEvent(newChatRoomOption = Some(chatRoom))
      }
    case Send(Service, message) =>

    case Send(Room(chatRoomId), message) =>
      rooms.get(chatRoomId)
  }
}
