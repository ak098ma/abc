package jp.co.applibot.abc.shared

import play.api.libs.json.Json

object ImplicitFormats {
  implicit val userFormat = Json.format[User]
  implicit val userCredentialFormat = Json.format[UserCredential]
}
