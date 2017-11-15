package jp.co.applibot.abc.mvc.errors

sealed abstract class LoginError(val message: String)

object LoginError {

  case object Unauthorized extends LoginError("ユーザーIDもしくはパスワードのどちらかが間違っています。")

  case class APIClientError(throwable: Throwable) extends LoginError(s"APIClient側でエラー ($throwable) が発生しました。")

}
