package jp.co.applibot.abc.controllers.actors

import akka.actor._
import jp.co.applibot.abc.shared.models.ServerToClientEvent

sealed trait Target
case object Service extends Target
case class ChatRoom(id: String) extends Target

sealed trait ActorManagerEvent
case class Join(target: Target, actorRef: ActorRef) extends ActorManagerEvent
case class Leave(target: Target, actorRef: ActorRef) extends ActorManagerEvent
case class Send(target: Target, message: String) extends ActorManagerEvent

class SocketManager extends Actor {
  private var everybody: Seq[ActorRef] = Seq.empty
  private var rooms: Map[String, Seq[ActorRef]] = Map.empty

  override def receive = {
    case Join(Service, actorRef) =>
      everybody = actorRef +: everybody
    case Leave(Service, actorRef) =>
      everybody = everybody.filterNot(_ == actorRef)
    case Join(ChatRoom(chatRoomId), actorRef) =>
      val actorRefs = rooms.getOrElse(chatRoomId, Seq(actorRef))
      rooms = rooms.updated(chatRoomId, actorRefs)
    case Leave(ChatRoom(chatRoomId), actorRef) =>
      rooms.get(chatRoomId).foreach{ actorRefs =>
        val left = actorRefs.filterNot(_ == actorRef)
        rooms = if (left.isEmpty) {
          rooms - chatRoomId
        } else {
          rooms.updated(chatRoomId, left)
        }
      }
    case Send(Service, message) =>
      everybody.foreach(_ ! ServerToClientEvent(message))
    case Send(ChatRoom(chatRoomId), message) =>
      rooms.get(chatRoomId).foreach(_.foreach(_ ! ServerToClientEvent(message)))
  }
}
