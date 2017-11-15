package jp.co.applibot.abc.mvc.actions

import javax.inject._

import jp.co.applibot.abc.database.interface.UserStore
import jp.co.applibot.abc.mvc.requests.SecureRequest
import jp.co.applibot.abc.utils.UserIdSession
import play.api.mvc._
import play.api.mvc.Results._

import scala.concurrent.{ExecutionContext, Future}

class SecureAction @Inject()(val parser: BodyParsers.Default, store: UserStore)
                            (implicit val executionContext: ExecutionContext)
  extends ActionBuilder[SecureRequest, AnyContent] {

  override def invokeBlock[A](request: Request[A], block: SecureRequest[A] => Future[Result]): Future[Result] = {
    request.session.get(UserIdSession.key) match {
      case None =>
        println(request.session)
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
