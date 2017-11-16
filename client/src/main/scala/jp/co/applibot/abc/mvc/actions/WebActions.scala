package jp.co.applibot.abc.mvc.actions

import jp.co.applibot.abc.shared.models.{ChatRooms, User, UserCredential}
import jp.co.applibot.abc.web.APIClient
import jp.co.applibot.abc.{Page, Store}
import org.scalajs.dom.experimental.{ReadableStream, Response}
import org.scalajs.dom.window
import play.api.libs.json.{JsError, JsSuccess, Json}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js.typedarray.Uint8Array
import scala.util.{Failure, Success}

object WebActions {
  private def handleUnauthorized(response: Response): Response = {
    if (response.status == 401) {
      Store.getState.router.foreach { routerCtl =>
        if (window.location.pathname != routerCtl.pathFor(Page.Login).value) {
          routerCtl.set(Page.Login).runNow()
        }
      }
    }
    response
  }

  private def gotoPage(page: Page): Unit = {
    Store.getState.router.foreach { routerCtl =>
      if (window.location.pathname != routerCtl.pathFor(page).value) {
        routerCtl.set(page).runNow()
      }
    }
  }

  implicit class BodyUtil(body: ReadableStream[Uint8Array]) {
    def text: Future[String] = body.getReader().read().toFuture.map(_.value.map(_.toChar).mkString(""))
  }

  def login(userCredential: UserCredential, nextPageOption: Option[Page] = None): Unit = {
    APIClient.login(userCredential).onComplete {
      case Failure(error) =>
        throw error
      case Success(response) =>
        response.status match {
          case 200 =>
            nextPageOption.foreach(gotoPage)
          case _ =>
        }
    }
  }

  def logout(nextPageOption: Option[Page] = Some(Page.Login)): Unit = {
    APIClient.logout.onComplete {
      case Failure(error) =>
        throw error
      case Success(response) =>
        response.status match {
          case 200 =>
            nextPageOption.foreach(gotoPage)
          case _ =>
        }
    }
  }

  def signUp(user: User, nextPageOption: Option[Page] = Some(Page.Login)): Unit = {
    APIClient.signUp(user).onComplete {
      case Failure(error) =>
        throw error
      case Success(response) =>
        response.status match {
          case 200 =>
            nextPageOption.foreach(gotoPage)
          case 400 =>
          case _ =>
        }
    }
  }

  def fetchUser(nextPageOption: Option[Page] = None): Unit = {
    APIClient.getUser.map(handleUnauthorized).onComplete {
      case Failure(error) =>
        throw error
      case Success(response) =>
        response.status match {
          case 200 =>
            nextPageOption.foreach(gotoPage)
          case _ =>
        }
    }
  }

  def fetchChatRooms(): Unit = {
    APIClient.getChatRooms.map(handleUnauthorized).onComplete {
      case Failure(error) =>
        throw error
      case Success(response) =>
        response.status match {
          case 200 =>
            response.body.text.onComplete {
              case Success(text) =>
                Json.fromJson[ChatRooms](Json.parse(text)) match {
                  case JsSuccess(chatRooms, _) =>
                    window.console.info(chatRooms.toString)
                  case JsError(error) =>
                    window.console.error(error)
                }
              case Failure(error) =>
                window.console.error(error.getMessage)
            }
          case _ =>
        }
    }
  }
}
