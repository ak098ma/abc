package jp.co.applibot.abc.mvc.actions

import jp.co.applibot.abc.{Page, Store}
import jp.co.applibot.abc.shared.models.{User, UserCredential}
import jp.co.applibot.abc.web.APIClient

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object WebActions {
  def login(userCredential: UserCredential): Unit = {
    APIClient.login(userCredential).onComplete {
      case Failure(error) =>
        throw error
      case Success(response) =>
        response.status match {
          case 200 =>
            Store.getState.router.foreach(_.set(Page.Chat).runNow())
          case 401 =>
          case _ =>
        }
    }
  }

  def signUp(user: User): Unit = {
    APIClient.signUp(user).onComplete {
      case Failure(error) =>
        throw error
      case Success(response) =>
        response.status match {
          case 200 =>
          case 400 =>
          case _ =>
        }
    }
  }

  def fetchUser(): Unit = {
    APIClient.getUser.onComplete {
      case Failure(error) =>
        throw error
      case Success(response) =>
        response.status match {
          case 200 =>
          case 401 =>
            Store.getState.router.foreach(_.set(Page.Login).runNow())
          case _ =>
        }
    }
  }
}
