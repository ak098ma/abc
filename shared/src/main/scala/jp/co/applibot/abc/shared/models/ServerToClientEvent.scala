package jp.co.applibot.abc.shared.models

import play.api.libs.json.Json

case class ServerToClientEvent(message: String)

object ServerToClientEvent {
  implicit val format = Json.format[ServerToClientEvent]
}
