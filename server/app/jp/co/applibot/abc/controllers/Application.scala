package jp.co.applibot.abc.controllers

import javax.inject._

import play.api.mvc._

@Singleton
class Application @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  def index = Action {
    Ok(views.html.index())
  }
}
