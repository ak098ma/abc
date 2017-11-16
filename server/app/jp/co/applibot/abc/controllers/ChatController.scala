package jp.co.applibot.abc.controllers

import javax.inject.{Inject, Singleton}

import akka.actor._
import akka.stream.Materializer
import jp.co.applibot.abc.controllers.actors._
import jp.co.applibot.abc.shared.models.{ClientToServerEvent, ServerToClientEvent}
import play.api.libs.streams.ActorFlow
import play.api.mvc.WebSocket.MessageFlowTransformer
import play.api.mvc._

@Singleton
class ChatController @Inject()(cc: ControllerComponents)(implicit system: ActorSystem, mat: Materializer) extends AbstractController(cc) {
  import ClientToServerEvent.format
  import ServerToClientEvent.format
  implicit val messageFlowTransformer = MessageFlowTransformer.jsonMessageFlowTransformer[ClientToServerEvent, ServerToClientEvent]
  val socketManager: ActorRef = system.actorOf(Props[SocketManager])

  def socket = WebSocket.accept[ClientToServerEvent, ServerToClientEvent] { _ =>  // TODO: request.sessionを処理して認証かけること
    ActorFlow.actorRef(out => Props(new WebSocketActor(out, socketManager)))
  }
}
