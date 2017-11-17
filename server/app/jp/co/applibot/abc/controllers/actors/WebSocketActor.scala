package jp.co.applibot.abc.controllers.actors

import akka.actor._
import jp.co.applibot.abc.shared.models.{ClientToServerEvent, UserPublic}

class WebSocketActor(webSocketRef: ActorRef, socketManager: ActorRef, userPublic: UserPublic) extends Actor {
  override def preStart(): Unit = {
    socketManager ! Join(Service, webSocketRef, userPublic)
  }

  override def postStop(): Unit = {
    socketManager ! Leave(Service, webSocketRef, userPublic)
  }

  override def receive = { case event: ClientToServerEvent =>
    event.joinRoomOption.foreach { roomId =>
      socketManager ! Join(Room(roomId), webSocketRef, userPublic)
    }
    event.leaveRoomOption.foreach { roomId =>
      socketManager ! Leave(Room(roomId), webSocketRef, userPublic)
    }
    event.createRoomOption.foreach { newChatRoom =>
      socketManager ! CreateChatRoom(newChatRoom, webSocketRef, userPublic)
    }
    event.subscribeRoomOption.foreach { chatRoom =>
      socketManager ! Subscribe(Room(chatRoom.id), webSocketRef, userPublic)
    }
    event.unSubscribeRoomOption.foreach { chatRoom =>
      socketManager ! UnSubscribe(Room(chatRoom.id), webSocketRef, userPublic)
    }
    event.newMessageOption.foreach { newMessage =>
      socketManager ! Send(Room(newMessage.chatRoomId), newMessage.message)
    }
  }
}
