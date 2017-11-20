package jp.co.applibot.abc.web

import jp.co.applibot.abc.shared.models._
import org.scalajs.dom.experimental.Fetch.fetch
import org.scalajs.dom.experimental.HttpMethod._
import org.scalajs.dom.experimental._
import play.api.libs.json.Json

import scala.concurrent.Future

object APIClient {
  def signUp(user: User): Future[Response] = {
    val path = "/rest/v1/sign-up"
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
    val path = "/rest/v1/login"
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
}
