package jp.co.applibot.abc.pages

import japgolly.scalajs.react._
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.vdom.html_<^._

trait SignUp {
  private val signUp = ScalaComponent.builder[Unit]("SignUp")
    .stateless
    .renderStatic(<.div("SignUp"))
    .build

  def apply(): Unmounted[Unit, Unit, Unit] = signUp()
}
