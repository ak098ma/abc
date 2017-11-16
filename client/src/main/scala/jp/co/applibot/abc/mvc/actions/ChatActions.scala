package jp.co.applibot.abc.mvc.actions

import jp.co.applibot.abc.Store
import jp.co.applibot.abc.shared.models.ChatRoom

object ChatActions {
  def openCreateChatRoomDialog(): Unit = {
    Store.updateChatState(_.copy(isCreateNewChatRoomDialogOpen = true))
  }

  def closeCreateChatRoomDialog(): Unit = {
    Store.updateChatState(_.copy(isCreateNewChatRoomDialogOpen = false))
  }

  def setTitleOfNewChatRoom(title: String): Unit = {
    Store.updateChatState(_.copy(titleOfNewChatRoom = title))
  }

  def showRoom(chatRoom: ChatRoom): Unit = {
    Store.updateChatState(_.copy(selectedChatRoomOption = Some(chatRoom)))
  }
}
