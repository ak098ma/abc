package jp.co.applibot.abc.actions

import jp.co.applibot.abc.Store
import jp.co.applibot.abc.models.LoginState

object LoginActions {
  private def updateLoginState(modify: LoginState => LoginState): Unit = {
    Store.update(s => s.copy(login = modify(s.login)))
  }

  def setUserId(id: String): Unit = {
    updateLoginState(_.copy(id = id))
  }

  def setPassword(password: String): Unit = {
    updateLoginState(_.copy(password = password))
  }
}
