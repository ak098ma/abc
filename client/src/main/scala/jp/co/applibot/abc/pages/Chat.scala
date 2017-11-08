package jp.co.applibot.abc.pages

import japgolly.scalajs.react._
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.vdom.html_<^._

trait Chat {
  private val chat = ScalaComponent.builder[Unit]("Chat")
    .stateless
    .renderStatic(<.div("Chat"))
    .build

  def apply(): Unmounted[Unit, Unit, Unit] = chat()
}
