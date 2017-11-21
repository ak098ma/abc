package jp.co.applibot.abc.controllers

import javax.inject._

import jp.co.applibot.abc.shared.styles
import play.api.mvc._

import scalacss.DevDefaults._

@Singleton
class Styles @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  def index = Action {
    Ok(styles.Index.render).as(CSS)
  }

  def components = Action {
    Ok(List(
      styles.Layout.render,
      styles.Home.render,
      styles.SignUp.render,
      styles.Login.render,
      styles.Chat.render,
      styles.NotFound.render,
    ).mkString("\n")).as(CSS)
  }
}
