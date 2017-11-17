package jp.co.applibot.abc.controllers

import javax.inject._

import jp.co.applibot.abc.database.interface.{ChatRoomStore, MessageStore, UserStore}
import jp.co.applibot.abc.mvc.actions.SecureAction
import jp.co.applibot.abc.mvc.requests.SecureRequest
import jp.co.applibot.abc.shared.models._
import jp.co.applibot.abc.utils.UserIdClaim
import play.api.http.{SecretConfiguration, SessionConfiguration}
import play.api.libs.json._
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class RestAPI @Inject()(secureAction: SecureAction, cc: ControllerComponents,
                        userStore: UserStore,
                        chatRoomStore: ChatRoomStore,
                        messageStore: MessageStore,
                        secretConfiguration: SecretConfiguration,
                        sessionConfiguration: SessionConfiguration)(implicit executor: ExecutionContext) extends AbstractController(cc) {

  private val jwtCodec = DefaultJWTCookieDataCodec(secretConfiguration, sessionConfiguration.jwt)
  implicit class SessionUtil(result: Result) {
    def refreshSession[A](implicit secureRequest: SecureRequest[A]): Result = result.withSession(UserIdClaim(secureRequest.userPublic.id))
  }

  def signUp: Action[JsValue] = Action(parse.json).async { implicit request =>
    Json.fromJson[User](request.body) match {
      case jsSuccess: JsSuccess[User] =>
        val user = jsSuccess.get
        userStore.get(UserCredential(id = user.id, password = user.password)).flatMap{
          case Some(_) =>
            Future { Conflict("this user is already registered.") }
          case None =>
            userStore.add(user).map(_ => Ok(""))
        }
      case jsError: JsError =>
        Future {BadRequest(jsError.errors.mkString(", ")) }
    }
  }

  def login: Action[JsValue] = Action(parse.json).async { implicit request =>
    Json.fromJson[UserCredential](request.body) match {
      case jsSuccess: JsSuccess[UserCredential] =>
        val userCredential = jsSuccess.get
        userStore.get(userCredential).map {
          case Some(user) =>
            val jwt = jwtCodec.encode(Map(UserIdClaim(user.id)))
            Ok(JsonWebToken(jwt).stringify)
          case None =>
            Unauthorized(s"id or password may be wrong.")
        }
      case jsError: JsError =>
        Future { BadRequest(jsError.errors.mkString(", ")) }
    }
  }

  def refresh: Action[AnyContent] = secureAction { implicit request =>
    Ok(JsonWebToken(jwtCodec.encode(Map(UserIdClaim(request.userPublic.id)))).stringify)
  }

  def getUser: Action[AnyContent] = secureAction.async { implicit request =>
    Future.successful(Ok(Json.prettyPrint(Json.toJson(request.userPublic))).refreshSession)
  }

  def getChatRooms: Action[AnyContent] = secureAction.async { implicit request =>
    chatRoomStore.list(request.userPublic.id).map{ chatRooms =>
      Ok(Json.toJson(ChatRooms(chatRooms)).toString()).refreshSession
    }
  }

  def createChatRoom: Action[JsValue] = secureAction(parse.json).async { implicit request =>
    Json.fromJson[NewChatRoom](request.body) match {
      case JsSuccess(newChatRoom, _) =>
        chatRoomStore.add(newChatRoom).map(chatRoom => Ok(Json.toJson(chatRoom).toString()).refreshSession)
      case JsError(errors) =>
        Future.successful(BadRequest(errors.mkString(", ")))
    }
  }
}
