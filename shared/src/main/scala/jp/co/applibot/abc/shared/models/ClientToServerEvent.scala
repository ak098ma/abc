package jp.co.applibot.abc.shared.models

import play.api.libs.json.Json

case class ClientToServerEvent(message: String)

object ClientToServerEvent {
  implicit val format = Json.format[ClientToServerEvent]
}
