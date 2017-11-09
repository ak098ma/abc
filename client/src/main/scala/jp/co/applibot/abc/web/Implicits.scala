package jp.co.applibot.abc.web

import scala.language.implicitConversions
import play.api.libs.json.JsValue

object Implicits {
  implicit def toPlayJsonBody(jsValue: JsValue): PlayJsonBody = {
    PlayJsonBody(jsValue)
  }
}
