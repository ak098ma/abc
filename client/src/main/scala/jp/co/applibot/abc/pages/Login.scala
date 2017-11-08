package jp.co.applibot.abc.pages

import japgolly.scalajs.react._
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.vdom.html_<^._

trait Login {
  private val login = ScalaComponent.builder[Unit]("Login")
    .stateless
    .renderStatic(<.div("Login"))
    .build

  def apply(): Unmounted[Unit, Unit, Unit] = login()
}
