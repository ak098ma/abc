package jp.co.applibot.abc.web

import fr.hmil.roshttp.HttpRequest
import fr.hmil.roshttp.Method.POST
import fr.hmil.roshttp.response.SimpleHttpResponse
import japgolly.scalajs.react.extra.router.BaseUrl
import jp.co.applibot.abc.Configuration
import monix.execution.Scheduler
import play.api.libs.json.Json
import Implicits._
import jp.co.applibot.abc.shared.models._

import scala.concurrent.Future

object APIClient {
  private def httpRequest(resolve: BaseUrl => BaseUrl) = HttpRequest(resolve(Configuration.baseUrl).value)
  private def request(resolve: BaseUrl => BaseUrl) = httpRequest(baseUrl => resolve(baseUrl / "rest" / "v1"))

  def signUp(user: User)(implicit scheduler: Scheduler): Future[SimpleHttpResponse] = {
    request(_ / "sign-up")
      .withMethod(POST)
      .withBody(Json.toJson(user))
      .send()
  }

  def login(userCredential: UserCredential)(implicit scheduler: Scheduler): Future[SimpleHttpResponse] = {
    request(_ / "login")
      .withMethod(POST)
      .withBody(Json.toJson(userCredential))
      .send()
  }

  def test(implicit scheduler: Scheduler): Future[SimpleHttpResponse] = {
    request(_ / "test")
      .withMethod(POST)
      .withHeader("Authorization", "JWT-TEST")
      .send()
  }
}
