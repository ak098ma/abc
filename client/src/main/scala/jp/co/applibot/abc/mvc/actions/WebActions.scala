package jp.co.applibot.abc.mvc.actions

import jp.co.applibot.abc.shared.models._
import jp.co.applibot.abc.web.APIClient
import jp.co.applibot.abc.{Page, Store, TokenManager}
import org.scalajs.dom.experimental.Response
import org.scalajs.dom.raw.{CloseEvent, ErrorEvent, MessageEvent, WebSocket}
import org.scalajs.dom.{Event, window}
import play.api.libs.json.{JsValue, Json}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
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
            response.text().toFuture.onComplete {
              case Failure(parseError) =>
                throw parseError
              case Success(text) =>
                TokenManager.update(Json.fromJson[JsonWebToken](Json.parse(text)).get.token)
                nextPageOption.foreach(gotoPage)
            }
          case _ =>
        }
    }
  }

  def logout(nextPageOption: Option[Page] = Some(Page.Login)): Unit = {
    TokenManager.delete()
    nextPageOption.foreach(gotoPage)
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

  def createChatRoom(newChatRoom: NewChatRoom): Unit = {
    Store.getState.chat.webSocketOption.foreach { socket =>
      socket.send(ClientToServerEvent(createRoomOption = Some(newChatRoom)).stringify)
    }
  }

  private def closeWebSocket(): Unit = Store.getState.chat.webSocketOption.foreach(_.close(code = 1000, reason = "unload"))

  val unload: js.Function1[Event, Unit] = (_) => closeWebSocket()

  def createWebSocket(token: String): Unit = {
    if (Store.getState.chat.webSocketOption.isEmpty) {
      val socket = new WebSocket(s"ws://${window.location.hostname}:${window.location.port}/chat/socket?token=$token")
      socket.addEventListener("open", { _: Event =>
        println("connected.")
      })
      socket.addEventListener("message", { event: MessageEvent =>
        window.console.log(event)
        val serverToClientEvent = Json.fromJson[ServerToClientEvent](Json.parse(event.data.toString)).get
        serverToClientEvent.chatRoomsOption.foreach { chatRooms =>
          chatRooms.rooms.foreach { chatRoom =>
            socket.send(ClientToServerEvent(joinRoomOption = Some(chatRoom.id)).stringify)
          }
        }
        serverToClientEvent.newChatRoomOption.foreach { chatRoom =>
          socket.send(ClientToServerEvent(joinRoomOption = Some(chatRoom.id)).stringify)
          Store.updateChatState{ state =>
            state.copy(chatRoomsOption = state.chatRoomsOption.map(_.add(chatRoom)))
          }
        }
        serverToClientEvent.chatRoomsOption.foreach { chatRooms =>
          Store.updateChatState(_.copy(chatRoomsOption = Some(chatRooms)))
        }
        serverToClientEvent.userPublicOption.foreach { userPublic =>
          Store.updateChatState(_.copy(userPublicOption = Some(userPublic)))
        }
      })
      socket.addEventListener("error", { event: ErrorEvent =>
        window.console.error(event)
      })
      socket.addEventListener("close", { _: CloseEvent =>
        Store.updateChatState(_.copy(webSocketOption = None))
      })
      Store.updateChatState(_.copy(webSocketOption = Some(socket)))
      window.addEventListener("unload", unload)
    }
  }

  def deleteWebSocket(): Unit = {
    window.removeEventListener("unload", unload)
    closeWebSocket()
  }
}
