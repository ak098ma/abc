package jp.co.applibot.abc.web

import jp.co.applibot.abc.shared.models._
import org.scalajs.dom.experimental.Fetch.fetch
import org.scalajs.dom.experimental.HttpMethod._
import org.scalajs.dom.experimental._
import play.api.libs.json.Json

import scala.concurrent.Future

object APIClient {
  private def request(path: String, options: RequestInit): Future[Response] = fetch(path, options).toFuture

  def signUp(user: User): Future[Response] = {
    val path = "/rest/v1/sign-up"
    val headers = new Headers()
    headers.append("Content-Type", "application/json")
    val options = RequestInit(
      method = POST,
      body = Json.toJson(user).toString(),
      headers = headers,
    )
    request(path, options)
  }

  def login(userCredential: UserCredential): Future[Response] = {
    val path = "/rest/v1/login"
    val headers = new Headers()
    headers.append("Content-Type", "application/json")
    val options = RequestInit(
      method = POST,
      credentials = RequestCredentials.include,
      body = Json.toJson(userCredential).toString(),
      headers = headers,
    )
    request(path, options)
  }
}
