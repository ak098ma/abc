package jp.co.applibot.abc.controllers

import javax.inject._

import jp.co.applibot.abc.database.interface.{ChatRoomStore, MessageStore, UserStore}
import jp.co.applibot.abc.mvc.actions.SecureAction
import jp.co.applibot.abc.shared.models.{ChatRooms, User, UserCredential}
import jp.co.applibot.abc.utils.UserIdSession
import play.api.libs.json._
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class RestAPI @Inject()(secureAction: SecureAction, cc: ControllerComponents, userStore: UserStore, chatRoomStore: ChatRoomStore, messageStore: MessageStore)(implicit executor: ExecutionContext) extends AbstractController(cc) {

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
            Ok("").withSession(UserIdSession(user.id))
          case None =>
            Unauthorized(s"id or password may be wrong.")
        }
      case jsError: JsError =>
        Future { BadRequest(jsError.errors.mkString(", ")) }
    }
  }

  def getUser: Action[AnyContent] = secureAction.async { implicit request =>
    Future.successful(Ok(Json.prettyPrint(Json.toJson(request.userPublic))))
  }

  def getChatRooms: Action[AnyContent] = secureAction.async { implicit request =>
    chatRoomStore.get(request.userPublic.id).map{ chatRooms =>
      Ok(Json.toJson(ChatRooms(chatRooms)).toString())
    }
  }
}
