package jp.co.applibot.abc.actions

import jp.co.applibot.abc.WebAPI
import jp.co.applibot.abc.shared.{User, UserCredential}

import scala.util.{Failure, Success}
import monix.execution.Scheduler.Implicits.global

object WebActions {
  def login(userCredential: UserCredential): Unit = {
    WebAPI.login(userCredential).onComplete {
      case Success(response) =>
        println(response)
      case Failure(error) =>
        println(error)
    }
  }

  def signUp(user: User): Unit = {
    WebAPI.signUp(user).onComplete {
      case Success(response) =>
        println(response)
      case Failure(error) =>
        println(error)
    }
  }
}
