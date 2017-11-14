package jp.co.applibot.abc.controllers

import javax.inject._

import jp.co.applibot.abc.database.memory.UserMemoryStore
import jp.co.applibot.abc.mvc.actions.SecureAction
import jp.co.applibot.abc.shared.models.{User, UserCredential, UserPublic}
import play.api.libs.json._
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class RestAPI @Inject()(secureAction: SecureAction, cc: ControllerComponents, store: UserMemoryStore)(implicit executor: ExecutionContext) extends AbstractController(cc) {

  def signUp: Action[JsValue] = Action(parse.json).async { implicit request =>
    Json.fromJson[User](request.body) match {
      case jsSuccess: JsSuccess[User] =>
        val user = jsSuccess.get
        store.get(UserCredential(id = user.id, password = user.password)).flatMap{
          case Some(_) =>
            Future { Conflict("this user is already registered.") }
          case None =>
            store.add(user).map {
              case true =>
                Ok("")
              case false =>
                InternalServerError("")
            }
        }
      case jsError: JsError =>
        Future {BadRequest(jsError.errors.mkString(", ")) }
    }
  }

  def login: Action[JsValue] = Action(parse.json).async { implicit request =>
    Json.fromJson[UserCredential](request.body) match {
      case jsSuccess: JsSuccess[UserCredential] =>
        val userCredential = jsSuccess.get
        store.get(userCredential).map {
          case Some(user) =>
            Ok(Json.toJson(UserPublic(id = user.id, nickname = user.nickname))).withSession(("id", user.id))
          case None =>
            Unauthorized(s"id or password may be wrong.")
        }
      case jsError: JsError =>
        Future { BadRequest(jsError.errors.mkString(", ")) }
    }
  }
}
