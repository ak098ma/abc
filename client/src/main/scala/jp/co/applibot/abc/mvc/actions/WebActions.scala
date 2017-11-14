package jp.co.applibot.abc.mvc.actions

import jp.co.applibot.abc.shared.models.{User, UserCredential}
import jp.co.applibot.abc.web.APIClient

import scala.util.{Failure, Success}
import monix.execution.Scheduler.Implicits.global

object WebActions {
  def login(userCredential: UserCredential): Unit = {
    APIClient.login(userCredential).onComplete {
      case Success(response) =>
        println(response)
      case Failure(error) =>
        println(error)
    }
  }

  def signUp(user: User): Unit = {
    APIClient.signUp(user).onComplete {
      case Success(response) =>
        println(response)
      case Failure(error) =>
        println(error)
    }
  }
}
