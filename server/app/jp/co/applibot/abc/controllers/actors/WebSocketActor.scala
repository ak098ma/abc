package jp.co.applibot.abc.controllers.actors

import akka.actor._
import jp.co.applibot.abc.shared.models.ClientToServerEvent

class WebSocketActor(val webSocketRef: ActorRef, val socketManager: ActorRef) extends Actor {
  override def preStart(): Unit = {
    socketManager ! Join(Service, webSocketRef)
  }

  override def postStop(): Unit = {
    socketManager ! Leave(Service, webSocketRef)
  }

  override def receive = { case event: ClientToServerEvent =>
      socketManager ! Send(Service, event.message)
  }
}
