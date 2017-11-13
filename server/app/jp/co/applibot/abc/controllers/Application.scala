package jp.co.applibot.abc.controllers

import javax.inject._

import jp.co.applibot.abc.database.interface.UserStore
import play.api.mvc._

import scala.concurrent.ExecutionContext

@Singleton
class Application @Inject()(cc: ControllerComponents, store: UserStore)(implicit executor: ExecutionContext) extends AbstractController(cc) {
  def index = Action {
    Ok(views.html.index())
  }
}
