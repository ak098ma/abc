package jp.co.applibot.abc.shared.models

import play.api.libs.json.{Json, OFormat}

case class User(id: String, nickname: String, password: String)

object User {
  implicit val format: OFormat[User] = Json.format[User]
}
