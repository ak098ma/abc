package jp.co.applibot.abc.pages

import japgolly.scalajs.react._
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._
import jp.co.applibot.abc.models.State
import jp.co.applibot.abc.mvc.actions.{ChatActions, WebActions}
import jp.co.applibot.abc.react.BackendUtils
import jp.co.applibot.abc.shared.models._
import jp.co.applibot.abc.{Page, Store, TokenManager}

import scala.scalajs.js.Date

trait Chat {
  type Props = RouterCtl[Page]

  class Backend(override val bs: BackendScope[Props, State]) extends BackendUtils[Props, State] {
    def render(state: State) = {
      <.div(
        <.header(
          state.chat.userPublicOption.map { userPublic =>
            <.label(s"ID: ${userPublic.id}, ニックネーム: ${userPublic.nickname}")
          }.getOrElse(EmptyVdom),
          <.button(
            "ログアウト",
            ^.onClick --> handleLogout,
          ),
        ),
        <.div(
          <.div(
            renderCreateChatRoom(state.chat.titleOfNewChatRoom, state.chat.isCreateNewChatRoomDialogOpen),
            <.div(
              <.div("参加しているチャットルーム"),
              renderJoinedChatRooms(state.chat.joinedChatRoomsOption)
            ),
            <.div(
              <.div("参加出来るチャットルーム"),
              renderAvailableChatRooms(state.chat.joinedChatRoomsOption, state.chat.availableChatRoomsOption)
            )
          ),
          <.div(
            state.chat.selectedChatRoomOption match {
              case None =>
                <.div("チャットルームにJoinしてください")
              case Some(chatRoom) =>
                renderMessages(state.chat.messages.get(chatRoom.id), state.chat.editingMessage)
            }
          ),
        ),
      )
    }

    def renderJoinedChatRooms(chatRoomsOption: Option[ChatRooms]) = {
      <.div(
        chatRoomsOption match {
          case Some(chatRooms) =>
            val rows = chatRooms.rooms.map { chatRoom =>
              <.div(
                ^.key := s"joined_${chatRoom.id}",
                <.button(
                  chatRoom.title,
                  ^.onClick --> handleShowRoom(chatRoom),
                )
              )
            }
            if (rows.isEmpty) {
              "チャットルームがありません。"
            } else {
              rows.toVdomArray
            }
          case None =>
            "Loading..."
        }
      )
    }

    def renderMessages(messagesOption: Option[Seq[Message]], editingMessage: String) = {
      <.div(
        <.div(
          ^.height := "300px",
          ^.overflowY := "scroll",
          messagesOption.map { messages =>
            messages.reverse.map { message =>
              <.div(
                ^.key := s"message_${message.id}",
                <.label(s"msg: ${message.message}"),
                <.label(s"date: ${new Date(message.timestamp)}"),
              )
            }.toVdomArray
          }.getOrElse(<.div("はじめての投稿をしよう！")),
        ),
        <.div(
          <.input(
            ^.value := editingMessage,
            ^.placeholder := "Type something you want...",
            ^.onChange ==> handleChangeMessage,
          ),
          <.button(
            "送信!",
            ^.onClick --> handleSendMessage
          ),
        )
      )
    }

    def renderAvailableChatRooms(joinedOption: Option[ChatRooms], availableOption: Option[ChatRooms]) = {
      (joinedOption zip availableOption).headOption.map { case (joined: ChatRooms, available: ChatRooms) =>
        val rows = available.rooms.filterNot(joined.rooms.contains).map { room =>
          <.div(
            ^.key := s"available_${room.id}",
            <.label(room.title),
            <.button("Join!", ^.onClick --> handleJoinRoom(room))
          )
        }
        if (rows.isEmpty) {
          <.div("参加出来るチャットルームがありません。")
        } else {
          rows.toVdomArray
        }
      }.getOrElse(<.div("Loading..."))
    }

    def renderCreateChatRoom(title: String, isOpen: Boolean) = {
      <.div(
        <.button(
          "チャットルームを作成する",
          ^.onClick --> handleOpenCreateChatRoomDialog
        ),
        if (isOpen) {
          <.div(
            <.div("チャットルームの作成"),
            <.div(
              <.label("タイトル"),
              <.input(
                ^.value := title,
                ^.onChange ==> handleChangeTitleOfNewChatRoom,
              ),
            ),
            <.button(
              "作成",
              ^.onClick --> handleCreateChatRoom,
            ),
            <.button(
              "キャンセル",
              ^.onClick --> handleCloseCreateChatRoomDialog
            ),
          )
        } else {
          EmptyVdom
        }
      )
    }

    def renderChatRoom(chatRoomOption: Option[ChatRoom]) = {
      chatRoomOption.map { chatRoom =>
        <.div(
          <.div(s"id - ${chatRoom.id}"),
          <.div(s"title - ${chatRoom.title}"),
          <.div(s"users - ${chatRoom.users.mkString(",")}"),
        )
      }.getOrElse {
        EmptyVdom
      }
    }

    def componentWillMount: Callback = bs.props.map { props =>
      Store.subscribe(update)
      Store.update(_.copy(router = Some(props)))
      TokenManager.getToken match {
        case None =>
          WebActions.logout()
        case Some(token) =>
          WebActions.createWebSocket(token)
      }
    }

    def componentWillUnmount: Callback = Callback {
      Store.unsubscribe(update)
      WebActions.deleteWebSocket()
    }

    def handleLogout: Callback = Callback {
      WebActions.logout()
    }

    def handleOpenCreateChatRoomDialog: Callback = Callback {
      ChatActions.openCreateChatRoomDialog()
    }

    def handleCreateChatRoom: Callback = bs.state.map { state =>
      WebActions.createChatRoom(NewChatRoom(title = state.chat.titleOfNewChatRoom, users = Seq(), isPrivate = false))
      ChatActions.closeCreateChatRoomDialog()
    }

    def handleCloseCreateChatRoomDialog: Callback = Callback {
      ChatActions.closeCreateChatRoomDialog()
    }

    def handleChangeTitleOfNewChatRoom(event: ReactEventFromInput): Callback = Callback {
      val value = event.target.value
      ChatActions.setTitleOfNewChatRoom(value)
    }

    def handleShowRoom(chatRoom: ChatRoom): Callback = Callback {
      ChatActions.showRoom(chatRoom)
      WebActions.subscribeChatRoom(chatRoom)
    }

    def handleJoinRoom(chatRoom: ChatRoom): Callback = Callback {
      WebActions.joinChatRoom(chatRoom)
    }

    def handleChangeMessage(event: ReactEventFromInput): Callback = Callback {
      val value = event.target.value
      ChatActions.setMessage(value)
    }

    def handleSendMessage: Callback = Callback {
      Store.getState.chat.selectedChatRoomOption.foreach { chatRoom =>
        WebActions.sendMessage(chatRoom, Store.getState.chat.editingMessage)
      }
    }
  }

  private val chat = ScalaComponent.builder[Props]("Chat")
    .initialState(Store.getState)
    .backend(new Backend(_))
    .renderBackend
    .componentWillMount(_.backend.componentWillMount)
    .componentWillUnmount(_.backend.componentWillUnmount)
    .build

  def apply(props: Props): Unmounted[Props, State, Backend] = chat(props)
}
