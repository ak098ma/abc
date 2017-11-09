package jp.co.applibot.abc.controllers

import javax.inject._

import jp.co.applibot.abc.shared.models.{User, UserCredential}
import play.api.libs.json._
import play.api.mvc._

@Singleton
class RestAPI @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def signUp: Action[JsValue] = Action(parse.json) { implicit request =>
    Json.fromJson[User](request.body) match {
      case jsSuccess: JsSuccess[User] =>
        val user = jsSuccess.get
        Ok("")
      case jsError: JsError =>
        BadRequest(jsError.errors.mkString(", "))
    }
  }

  def login: Action[JsValue] = Action(parse.json) { implicit request =>
    Json.fromJson[UserCredential](request.body) match {
      case jsSuccess: JsSuccess[UserCredential] =>
        val userCredential = jsSuccess.get
        Ok("")
      case jsError: JsError =>
        BadRequest(jsError.errors.mkString(", "))
    }
  }
}
