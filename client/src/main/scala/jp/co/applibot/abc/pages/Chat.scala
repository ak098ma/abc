package jp.co.applibot.abc.pages

import japgolly.scalajs.react._
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._
import jp.co.applibot.abc.models.State
import jp.co.applibot.abc.mvc.actions.{ChatActions, WebActions}
import jp.co.applibot.abc.react.BackendUtils
import jp.co.applibot.abc.shared.models.{ChatRoom, ChatRooms, NewChatRoom}
import jp.co.applibot.abc.{Page, Store}

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
              <.div("ChatRooms"),
              renderChatRooms(state.chat.chatRoomsOption)
            ),
          ),
          <.div(
            renderChatRoom(state.chat.selectedChatRoomOption)
          )
        ),
      )
    }

    def renderChatRooms(chatRoomsOption: Option[ChatRooms]) = {
      <.div(
        chatRoomsOption match {
          case Some(chatRooms) =>
            val rows = chatRooms.rooms.map { chatRoom =>
              <.div(
                ^.key := chatRoom.id,
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
      chatRoomOption.map{ chatRoom =>
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
      WebActions.fetchUser()
      WebActions.fetchChatRooms()
    }

    def componentWillUnmount: Callback = Callback {
      Store.unsubscribe(update)
    }

    def handleLogout: Callback = Callback {
      WebActions.logout()
    }

    def handleOpenCreateChatRoomDialog: Callback = Callback {
      ChatActions.openCreateChatRoomDialog()
    }

    def handleCreateChatRoom: Callback = bs.state.map { state =>
      WebActions.createChatRoom(NewChatRoom(title = state.chat.titleOfNewChatRoom, users = Seq("test")))
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
