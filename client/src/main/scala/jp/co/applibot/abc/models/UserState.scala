package jp.co.applibot.abc.models

import jp.co.applibot.abc.shared.models.UserPublic

case class UserState(tokenOption: Option[String],
                     publicOption: Option[UserPublic])
