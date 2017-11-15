package jp.co.applibot.abc.mvc.errors

sealed abstract class GetUserError(val message: String)

object GetUserError {

  case object Unauthorized extends GetUserError("ログインしてください。")

  case class APIClientError(throwable: Throwable) extends GetUserError(throwable.getMessage)

  case class JsonParseError(cause: String) extends GetUserError(cause)

}
