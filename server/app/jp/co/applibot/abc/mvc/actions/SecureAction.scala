package jp.co.applibot.abc.mvc.actions

import javax.inject._

import jp.co.applibot.abc.database.interface.UserStore
import jp.co.applibot.abc.mvc.requests.SecureRequest
import jp.co.applibot.abc.utils.UserIdClaim
import play.api.http.{SecretConfiguration, SessionConfiguration}
import play.api.mvc.Results._
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

class SecureAction @Inject()(val parser: BodyParsers.Default,
                             store: UserStore,
                             secretConfiguration: SecretConfiguration,
                             sessionConfiguration: SessionConfiguration)
                            (implicit val executionContext: ExecutionContext)
  extends ActionBuilder[SecureRequest, AnyContent] {
  private val jwtCodec = DefaultJWTCookieDataCodec(secretConfiguration, sessionConfiguration.jwt)

  override def invokeBlock[A](request: Request[A], block: SecureRequest[A] => Future[Result]): Future[Result] = {
    request.headers.get("Authorization").map(jwtCodec.decode).flatMap(_.get(UserIdClaim.key)) match {
      case None =>
        Future.successful(Unauthorized("ログインしてください。"))
      case Some(userId) =>
        store.get(userId).flatMap {
          case None =>
            Future.successful(Unauthorized("無効なセッションです。"))
          case Some(userPublic) =>
            block(new SecureRequest(userPublic, request))
        }
    }
  }
}
