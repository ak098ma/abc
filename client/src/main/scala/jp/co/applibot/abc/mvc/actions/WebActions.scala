package jp.co.applibot.abc.mvc.actions

import jp.co.applibot.abc.shared.models._
import jp.co.applibot.abc.web.APIClient
import jp.co.applibot.abc.{Page, Store}
import org.scalajs.dom.experimental.{ReadableStream, Response}
import org.scalajs.dom.window
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.Promise
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

  implicit class ResponseUtil(response: Response) {
    def getJson: Future[JsValue] = response.json().toFuture.map(any => Json.parse(scala.scalajs.js.JSON.stringify(any)))
    def getText: Future[String] = response.text().toFuture
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
            response.getJson.onComplete {
              case Success(jsValue) =>
                Json.fromJson[UserPublic](jsValue) match {
                  case JsSuccess(userPublic, _) =>
                    Store.updateChatState(_.copy(userPublicOption = Some(userPublic)))
                  case JsError(errors) =>
                    window.console.error(errors)
                }
              case Failure(error) =>
                throw error
            }
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
            response.getJson.onComplete {
              case Success(jsValue) =>
                Json.fromJson[ChatRooms](jsValue) match {
                  case JsSuccess(chatRooms, _) =>
                    Store.updateChatState(_.copy(chatRoomsOption = Some(chatRooms)))
                  case JsError(errors) =>
                    window.console.error(errors)
                }
              case Failure(error) =>
                throw error
            }
          case _ =>
        }
    }
  }

  def createChatRoom(newChatRoom: NewChatRoom): Unit = {
    APIClient.createChatRoom(newChatRoom).map(handleUnauthorized).onComplete {
      case Failure(error) =>
        throw error
      case Success(response) =>
        response.status match {
          case 200 =>
            response.getJson.onComplete {
              case Success(jsValue) =>
                Json.fromJson[ChatRoom](jsValue) match {
                  case JsSuccess(chatRoom, _) =>
                    Store.updateChatState(chatState => chatState.copy(chatRoomsOption = chatState.chatRoomsOption.map{ chatRooms =>
                      chatRooms.copy(rooms = chatRoom +: chatRooms.rooms)
                    }))
                  case JsError(errors) =>
                    window.console.error(errors)
                }
              case Failure(error) =>
                throw error
            }
          case _ =>
        }
    }
  }
}
