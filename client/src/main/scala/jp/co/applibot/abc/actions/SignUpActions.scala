package jp.co.applibot.abc.actions

import jp.co.applibot.abc.Store
import jp.co.applibot.abc.models.SignUpState

object SignUpActions {
  def updateSignUpState(modify: SignUpState => SignUpState): Unit = Store.update(s => s.copy(signUp = modify(s.signUp)))

  def setUserId(id: String): Unit = {
    updateSignUpState(_.copy(id = id))
  }

  def setNickname(nickname: String): Unit = {
    updateSignUpState(_.copy(nickname = nickname))
  }

  def setPassword(password: String): Unit = {
    updateSignUpState(_.copy(password = password))
  }
}
