package jp.co.applibot.abc.web

import japgolly.scalajs.react.extra.router.BaseUrl
import jp.co.applibot.abc.Configuration
import jp.co.applibot.abc.shared.models._
import org.scalajs.dom.experimental.Fetch.fetch
import org.scalajs.dom.experimental.HttpMethod._
import org.scalajs.dom.experimental._
import play.api.libs.json.Json

import scala.concurrent.Future

object APIClient {
  private val baseUrl = Configuration.baseUrl

  private def restV1(resolve: BaseUrl => BaseUrl): String = resolve(baseUrl / "rest" / "v1").value

  def signUp(user: User): Future[Response] = {
    val path = restV1(_ / "sign-up")
    val headers = new Headers()
    headers.append("Content-Type", "application/json")
    val request = RequestInit(
      method = POST,
      body = Json.toJson(user).toString(),
      headers = headers,
    )
    fetch(path, request).toFuture
  }

  def login(userCredential: UserCredential): Future[Response] = {
    val path = restV1(_ / "login")
    val headers = new Headers()
    headers.append("Content-Type", "application/json")
    val request = RequestInit(
      method = POST,
      credentials = RequestCredentials.include,
      body = Json.toJson(userCredential).toString(),
      headers = headers,
    )
    fetch(path, request).toFuture
  }

  def getUser: Future[Response] = {
    val path = restV1(_ / "secure" / "user")
    val request = RequestInit(
      method = GET,
      credentials = RequestCredentials.include,
    )
    fetch(path, request).toFuture
  }

  def getChatRooms: Future[Response] = {
    val path = restV1(_ / "secure" / "rooms")
    val request = RequestInit(
      method = GET,
      credentials = RequestCredentials.include,
    )
    fetch(path, request).toFuture
  }
}
