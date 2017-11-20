package jp.co.applibot.abc.models

import play.api.libs.json.Json

case class LoginState(id: String,
                      password: String,
                      tokenOption: Option[String])
