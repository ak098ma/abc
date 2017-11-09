package jp.co.applibot.abc.shared.models

import play.api.libs.json.{Json, OFormat}

case class UserCredential(id: String, password: String)

object UserCredential {
  implicit val format: OFormat[UserCredential] = Json.format[UserCredential]
}
