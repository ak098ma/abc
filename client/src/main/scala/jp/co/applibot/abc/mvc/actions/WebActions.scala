package jp.co.applibot.abc.mvc.actions

import jp.co.applibot.abc.mvc.errors.{LoginError, SignUpError}
import jp.co.applibot.abc.shared.models.{User, UserCredential}
import jp.co.applibot.abc.web.APIClient
import monix.execution.Scheduler.Implicits.global

import scala.util.{Failure, Success}

object WebActions {
  def login(userCredential: UserCredential,
            onSuccess: () => Unit = () => (),
            onFailure: (LoginError) => Unit = (_) => ()): Unit = {
    APIClient.login(userCredential).onComplete {
      case Failure(error) =>
        onFailure(LoginError.APIClientError(error))
      case Success(response) =>
        response.statusCode match {
          case 200 =>
            onSuccess()
          case 401 =>
            onFailure(LoginError.Unauthorized)
          case _ =>
            onFailure(LoginError.UnexpectedState(response))
        }
    }
  }

  def signUp(user: User,
             onSuccess: () => Unit = () => (),
             onFailure: (SignUpError) => Unit = (_) => ()): Unit = {
    APIClient.signUp(user).onComplete {
      case Failure(error) =>
        onFailure(SignUpError.APIClientError(error))
      case Success(response) =>
        response.statusCode match {
          case 200 =>
            onSuccess()
          case 400 =>
            onFailure(SignUpError.InvalidPassword)
          case _ =>
            onFailure(SignUpError.UnexpectedState(response))
        }
    }
  }
}
