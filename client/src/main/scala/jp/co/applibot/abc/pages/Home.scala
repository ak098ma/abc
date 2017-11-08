package jp.co.applibot.abc.pages

import japgolly.scalajs.react._
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.vdom.html_<^._

trait Home {
  private val home = ScalaComponent.builder[Unit]("Home")
    .stateless
    .renderStatic(<.div("Home"))
    .build

  def apply(): Unmounted[Unit, Unit, Unit] = home()
}
