package jp.co.applibot.abc.shared.models

import play.api.libs.json.Json

object JsonWebToken {
  implicit val format = Json.format[JsonWebToken]
}

case class JsonWebToken(token: String) {
  def stringify: String = Json.stringify(Json.toJson(this))
}
