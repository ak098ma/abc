package jp.co.applibot.abc

sealed trait Page

object Page {
  case object Home extends Page with pages.Home
  case object SignUp extends Page with pages.SignUp
  case object Login extends Page with pages.Login
  case object Chat extends Page with pages.Chat
  case object NotFound extends Page with pages.NotFound
}
