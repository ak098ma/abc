package jp.co.applibot.abc

import fr.hmil.roshttp.HttpRequest
import fr.hmil.roshttp.response.SimpleHttpResponse
import japgolly.scalajs.react.extra.router.BaseUrl
import jp.co.applibot.abc.shared.{User, UserCredential}
import monix.execution.Scheduler

import scala.concurrent.Future

object WebAPI {
  private def httpRequest(resolve: BaseUrl => BaseUrl) = HttpRequest(resolve(Configuration.baseUrl).value)
  private def request(resolve: BaseUrl => BaseUrl) = httpRequest(_ / "rest" / "v1")

  def signUp(user: User)(implicit scheduler: Scheduler): Future[SimpleHttpResponse] = {
    request(_ / "sign-up")
      .send()
  }

  def login(userCredential: UserCredential)(implicit scheduler: Scheduler): Future[SimpleHttpResponse] = {
    request(_ / "login")
      .send()
  }
}
