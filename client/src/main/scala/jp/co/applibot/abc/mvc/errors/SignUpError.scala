package jp.co.applibot.abc.mvc.errors

import fr.hmil.roshttp.response.SimpleHttpResponse

sealed abstract class SignUpError(val message: String)

object SignUpError {

  case class APIClientError(error: Throwable) extends SignUpError(s"APIClient側でエラー ($error) が発生しました。")

  case object InvalidUserId extends SignUpError("userIDの形式が間違っています。")

  case object InvalidNickname extends SignUpError("nicknameの形式が間違っています。")

  case object InvalidPassword extends SignUpError("passwordの形式が間違っています。")

  case class UnexpectedState(response: SimpleHttpResponse) extends SignUpError(response.toString)

}
