package jp.co.applibot.abc.controllers

import javax.inject.{Inject, Singleton}

import akka.actor._
import akka.stream.Materializer
import jp.co.applibot.abc.controllers.actors._
import jp.co.applibot.abc.database.interface.{ChatRoomStore, MessageStore, UserStore}
import jp.co.applibot.abc.shared.models.{ClientToServerEvent, ServerToClientEvent}
import jp.co.applibot.abc.utils.UserIdClaim
import play.api.http.{SecretConfiguration, SessionConfiguration}
import play.api.libs.streams.ActorFlow
import play.api.mvc.WebSocket.MessageFlowTransformer
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ChatController @Inject()(cc: ControllerComponents,
                               userStore: UserStore,
                               chatRoomStore: ChatRoomStore,
                               messageStore: MessageStore,
                               secretConfiguration: SecretConfiguration,
                               sessionConfiguration: SessionConfiguration)(implicit system: ActorSystem, mat: Materializer, ec: ExecutionContext) extends AbstractController(cc) {
  private val jwtCodec = DefaultJWTCookieDataCodec(secretConfiguration, sessionConfiguration.jwt)
  implicit val messageFlowTransformer = MessageFlowTransformer.jsonMessageFlowTransformer[ClientToServerEvent, ServerToClientEvent]
  val socketManager: ActorRef = system.actorOf(Props(new SocketManager(userStore, chatRoomStore, messageStore)))

  def socket = WebSocket.acceptOrResult[ClientToServerEvent, ServerToClientEvent] { request =>
    request.getQueryString("token") match {
      case None =>
          Future.successful(Left(Unauthorized("unauthorized")))
      case Some(token) =>
        jwtCodec
          .decode(token)
          .get(UserIdClaim.key)
          .map(userStore.get)
          .map(_.map(_.map{ userPublic =>
            Right(ActorFlow.actorRef(out => Props(new WebSocketActor(out, socketManager, userPublic))))
          }.getOrElse(Left(Unauthorized("unauthorized")))))
          .getOrElse(Future.successful(Left(Unauthorized("unauthorized"))))
    }
  }
}
