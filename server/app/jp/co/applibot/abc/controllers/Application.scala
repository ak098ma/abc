package jp.co.applibot.abc.controllers

import javax.inject._

import jp.co.applibot.abc.database.interface.UserStore
import play.api.mvc._
import play.filters.headers.SecurityHeadersFilter

import scala.concurrent.ExecutionContext

@Singleton
class Application @Inject()(cc: ControllerComponents, store: UserStore)(implicit executor: ExecutionContext) extends AbstractController(cc) {
  def index = Action { request =>
    Ok(views.html.index()).withHeaders(SecurityHeadersFilter.CONTENT_SECURITY_POLICY_HEADER -> List(
      "default-src 'self'",
      s"connect-src 'self' ws://${request.host}",
      "style-src 'self' 'sha256-bT59+3hG30aMU9emwfntU5zm/FeB9B0iEo0dC3RWUXw='",
    ).mkString(";"))
  }
}
