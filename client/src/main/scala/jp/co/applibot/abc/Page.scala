package jp.co.applibot.abc

sealed trait Page
case object Home extends Page with pages.Home
case object SignUp extends Page with pages.SignUp
case object Login extends Page with pages.Login
case object Chat extends Page with pages.Chat
