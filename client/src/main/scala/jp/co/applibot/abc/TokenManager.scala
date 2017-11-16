package jp.co.applibot.abc

import org.scalajs.dom.ext.SessionStorage

object TokenManager {
  private val key = "token"
  def update(token: String): Unit = SessionStorage.update(key, token)

  def getToken: Option[String] = SessionStorage(key)

  def delete(): Unit = SessionStorage.remove(key)
}
