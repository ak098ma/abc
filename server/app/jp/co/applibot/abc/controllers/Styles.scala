package jp.co.applibot.abc.controllers

import javax.inject._

import play.api.mvc._
import views.styles.{Home, Index}

import scalacss.DevDefaults._

@Singleton
class Styles @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  def index = Action {
    Ok(Index.render).as(CSS)
  }

  def components = Action {
    Ok(List(
      Home.render,
    ).mkString("\n")).as(CSS)
  }
}
