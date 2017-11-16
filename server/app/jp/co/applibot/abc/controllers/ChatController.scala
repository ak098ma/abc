package jp.co.applibot.abc.controllers

import javax.inject.{Inject, Singleton}

import akka.actor._
import akka.stream.Materializer
import jp.co.applibot.abc.controllers.actors._
import jp.co.applibot.abc.database.interface.{ChatRoomStore, UserStore}
import jp.co.applibot.abc.shared.models.{ClientToServerEvent, ServerToClientEvent}
import play.api.libs.streams.ActorFlow
import play.api.mvc.WebSocket.MessageFlowTransformer
import play.api.mvc._

import scala.concurrent.ExecutionContext

@Singleton
class ChatController @Inject()(cc: ControllerComponents, userStore: UserStore, chatRoomStore: ChatRoomStore)(implicit system: ActorSystem, mat: Materializer, ec: ExecutionContext) extends AbstractController(cc) {
  implicit val messageFlowTransformer = MessageFlowTransformer.jsonMessageFlowTransformer[ClientToServerEvent, ServerToClientEvent]
  val socketManager: ActorRef = system.actorOf(Props(new SocketManager(userStore, chatRoomStore)))

  def socket = WebSocket.accept[ClientToServerEvent, ServerToClientEvent] { _ =>  // TODO: request.sessionを処理して認証かけること
    ActorFlow.actorRef(out => Props(new WebSocketActor(out, socketManager)))
  }
}
