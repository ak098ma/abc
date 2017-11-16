package jp.co.applibot.abc.mvc.actions

import jp.co.applibot.abc.Store

object LoginActions {
  def setUserId(id: String): Unit = {
    Store.updateLoginState(_.copy(id = id))
  }

  def setPassword(password: String): Unit = {
    Store.updateLoginState(_.copy(password = password))
  }
}
