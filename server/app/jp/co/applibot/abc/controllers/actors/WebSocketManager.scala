package jp.co.applibot.abc.controllers.actors

import akka.actor._
import jp.co.applibot.abc.database.interface.{ChatRoomStore, MessageStore, UserStore}
import jp.co.applibot.abc.shared.models._

import scala.concurrent.{ExecutionContext, Future}

sealed trait Target

case object Service extends Target

case class Room(id: String) extends Target

sealed trait SocketManagerEvent

case class CreateChatRoom(newChatRoom: NewChatRoom, actorRef: ActorRef, userPublic: UserPublic) extends SocketManagerEvent

case class Join(target: Target, actorRef: ActorRef, userPublic: UserPublic) extends SocketManagerEvent

case class Leave(target: Target, actorRef: ActorRef, userPublic: UserPublic) extends SocketManagerEvent

case class Subscribe(room: Room, actorRef: ActorRef, userPublic: UserPublic) extends SocketManagerEvent

case class UnSubscribe(room: Room, actorRef: ActorRef, userPublic: UserPublic) extends SocketManagerEvent

case class Send(target: Target, message: String, userId: String) extends SocketManagerEvent

case class SocketUser(actorRef: ActorRef, userPublic: UserPublic)

class SocketManager(userStore: UserStore, chatRoomStore: ChatRoomStore, messageStore: MessageStore)(implicit ec: ExecutionContext) extends Actor {
  private var everybody: Seq[SocketUser] = Seq.empty
  private var rooms: Map[String, Seq[SocketUser]] = Map.empty

  override def receive = {
    case Join(Service, actorRef, userPublic) =>
      everybody = SocketUser(actorRef, userPublic) +: everybody.filterNot(_.userPublic.id == userPublic.id)
      Future.sequence(userPublic.joiningChatRooms.map(chatRoomStore.get))
        .map(_.flatten)
        .map(ChatRooms(_))
        .flatMap(joined => chatRoomStore.list(userPublic.id).map(available => (joined, ChatRooms(available))))
        .foreach { args =>
          val (joined, available) = args
          actorRef ! ServerToClientEvent(
            userPublicOption = Some(userPublic),
            joinedRoomsOption = Some(joined),
            availableRoomsOption = Some(available)
          )
        }

    case Leave(Service, actorRef, userPublic) =>
      everybody = everybody.filterNot(_.userPublic.id == userPublic.id)

    case Join(Room(chatRoomId), actorRef, userPublic) =>
      val socketUsers = rooms.getOrElse(chatRoomId, Seq.empty)
      rooms = rooms.updated(chatRoomId, SocketUser(actorRef, userPublic) +: socketUsers.filterNot(_.userPublic.id == userPublic.id))
      userStore.update(userPublic.id, { u: UserPublic =>
        u.copy(joiningChatRooms = (chatRoomId +: u.joiningChatRooms).distinct)
      }).foreach { _ =>
        chatRoomStore
          .update(chatRoomId, { chatRoom => chatRoom.copy(users = (userPublic.id +: chatRoom.users).distinct) })
          .foreach(chatRoomOption => actorRef ! ServerToClientEvent(newChatRoomOption = chatRoomOption))
      }

    case Leave(Room(chatRoomId), actorRef, _) =>
    // TODO: チャットルームから退出する処理
    //      rooms.get(chatRoomId).foreach { socketUsers =>
    //        val left = socketUsers.filterNot(_.actorRef == actorRef)
    //        rooms = if (left.isEmpty) {
    //          rooms - chatRoomId
    //        } else {
    //          rooms.updated(chatRoomId, left)
    //        }
    //      }

    case Subscribe(Room(chatRoomId), actorRef, userPublic) =>
      val socketUsers = rooms.getOrElse(chatRoomId, Seq.empty)
      rooms = rooms.updated(chatRoomId, SocketUser(actorRef, userPublic) +: socketUsers.filterNot(_.userPublic.id == userPublic.id))
      messageStore.get(chatRoomId, 0, 7).map(_.map { message =>
        ReceivedMessage(chatRoomId = chatRoomId, messageId = message.id, userId = message.userId, message = message.message, timestamp = message.timestamp)
      }).foreach(messages => actorRef ! ServerToClientEvent(receivedMessagesOption = Some(messages)))

    case UnSubscribe(Room(chatRoomId), _, userPublic) =>
      rooms.get(chatRoomId).foreach { socketUsers =>
        val left = socketUsers.filterNot(_.userPublic.id == userPublic.id)
        rooms = rooms.updated(chatRoomId, left)
        if (left.isEmpty) {
          rooms = rooms - chatRoomId
        }
      }

    case CreateChatRoom(newChatRoom, _, userPublic) =>
      chatRoomStore
        .add(newChatRoom.copy(users = (userPublic.id +: newChatRoom.users).distinct))
        .foreach { chatRoom =>
          chatRoom.users.foreach { userId =>
            userStore.update(userId, { userPublic: UserPublic =>
              userPublic.copy(joiningChatRooms = chatRoom.id +: userPublic.joiningChatRooms)
            }).foreach(_.foreach { u =>
              chatRoomStore.list(u.id).map(ChatRooms(_)).foreach { chatRooms =>
                val available = if (chatRoom.isPrivate) Some(chatRooms) else None
                everybody.filter(_.userPublic.id == u.id).foreach { socketUser =>
                  Future.sequence(u.joiningChatRooms.map(chatRoomStore.get))
                    .map(_.flatten)
                    .map(ChatRooms(_))
                    .foreach { joined =>
                      socketUser.actorRef ! ServerToClientEvent(
                        joinedRoomsOption = Some(joined),
                        availableRoomsOption = available
                      )
                    }
                }
              }
            })
          }
          if (!chatRoom.isPrivate) {
            everybody.foreach { socketUser =>
              chatRoomStore.list(socketUser.userPublic.id).map(ChatRooms(_)).foreach { chatRooms =>
                socketUser.actorRef ! ServerToClientEvent(availableRoomsOption = Some(chatRooms))
              }
            }
          }
        }

    case Send(Service, message, userId) =>

    case Send(Room(chatRoomId), message, userId) =>
      messageStore.add(chatRoomId, message, userId).foreach { message =>
        rooms.get(chatRoomId).foreach(_.foreach(_.actorRef ! ServerToClientEvent(receivedMessageOption = Some(ReceivedMessage(chatRoomId = chatRoomId, messageId = message.id, userId = message.userId, message = message.message, timestamp = message.timestamp)))))
      }
  }
}
