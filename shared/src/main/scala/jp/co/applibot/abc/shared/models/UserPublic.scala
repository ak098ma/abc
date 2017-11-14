package jp.co.applibot.abc.shared.models

import play.api.libs.json.{Json, OFormat}

case class UserPublic(id: String, nickname: String)

object UserPublic {
  implicit val format: OFormat[UserPublic] = Json.format[UserPublic]
}
