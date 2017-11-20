package jp.co.applibot.abc.mvc.actions

import jp.co.applibot.abc.shared.models._
import jp.co.applibot.abc.web.APIClient
import jp.co.applibot.abc.{Store, TokenManager}
import org.scalajs.dom.experimental.Response
import org.scalajs.dom.raw.{CloseEvent, ErrorEvent, MessageEvent, WebSocket}
import org.scalajs.dom.{Event, window}
import play.api.libs.json.{JsValue, Json}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.util.{Failure, Success}

object WebActions {
  implicit class ResponseUtil(response: Response) {
    def getJson: Future[JsValue] = response.json().toFuture.map(any => Json.parse(scala.scalajs.js.JSON.stringify(any)))

    def getText: Future[String] = response.text().toFuture
  }

  def login(userCredential: UserCredential): Unit = {
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
            }
          case _ =>
        }
    }
  }

  def logout(): Unit = {
    TokenManager.delete()
  }

  def signUp(user: User): Unit = {
    APIClient.signUp(user).onComplete {
      case Failure(error) =>
        throw error
      case Success(response) =>
        response.status match {
          case 200 =>
            ???
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

  def joinChatRoom(chatRoom: ChatRoom): Unit = {
    Store.getState.chat.webSocketOption.foreach { socket =>
      socket.send(ClientToServerEvent(joinRoomOption = Some(chatRoom.id)).stringify)
    }
  }

  def subscribeChatRoom(chatRoom: ChatRoom): Unit = {
    Store.getState.chat.webSocketOption.foreach { socket =>
      socket.send(ClientToServerEvent(subscribeRoomOption = Some(chatRoom)).stringify)
    }
  }

  def sendMessage(chatRoom: ChatRoom, message: String): Unit = {
    Store.getState.chat.webSocketOption.foreach { socket =>
      socket.send(ClientToServerEvent(newMessageOption = Some(NewMessage(chatRoomId = chatRoom.id, message = message))).stringify)
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
        serverToClientEvent.joinedRoomsOption.foreach { chatRooms =>
          Store.updateChatState(_.copy(joinedChatRoomsOption = Some(chatRooms)))
        }
        serverToClientEvent.newChatRoomOption.foreach { chatRoom =>
          Store.updateChatState { state =>
            state.copy(
              joinedChatRoomsOption = state.joinedChatRoomsOption.map(_.add(chatRoom)),
              availableChatRoomsOption = state.availableChatRoomsOption.map(_.remove(chatRoom))
            )
          }
          subscribeChatRoom(chatRoom)
        }
        serverToClientEvent.availableRoomsOption.foreach { chatRooms =>
          Store.updateChatState(_.copy(availableChatRoomsOption = Some(chatRooms)))
        }
        serverToClientEvent.userPublicOption.foreach { userPublic =>
          Store.updateChatState(_.copy(userPublicOption = Some(userPublic)))
        }
        serverToClientEvent.receivedMessagesOption.foreach { receivedMessages =>
          receivedMessages.foreach{ receivedMessage =>
            Store.updateChatState{ state =>
              val messages = state.messages.getOrElse(receivedMessage.chatRoomId, Seq.empty)
              val received = receivedMessages.map(msg => Message(id = msg.messageId, message = msg.message, timestamp = msg.timestamp))
              state.copy(messages = state.messages.updated(receivedMessage.chatRoomId, (received ++ messages).distinct))
            }
          }
        }
        serverToClientEvent.receivedMessageOption.foreach { receivedMessage =>
          Store.updateChatState{ state =>
            val messages = state.messages.getOrElse(receivedMessage.chatRoomId, Seq.empty)
            val message = Message(id = receivedMessage.messageId, message = receivedMessage.message, timestamp = receivedMessage.timestamp)
            state.copy(messages = state.messages.updated(receivedMessage.chatRoomId, (message +: messages).distinct))
          }
        }
      })
      socket.addEventListener("error", { _: ErrorEvent =>
        ???
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
