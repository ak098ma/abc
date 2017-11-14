package jp.co.applibot.abc.mvc.requests

import jp.co.applibot.abc.shared.models.UserPublic
import play.api.mvc._

class SecureRequest[A](val userPublic: UserPublic, request: Request[A]) extends WrappedRequest[A](request)
