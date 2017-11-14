package jp.co.applibot.abc.pages

import japgolly.scalajs.react._
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._
import jp.co.applibot.abc.Page

trait Chat {
  type Props = RouterCtl[Page]

  private val chat = ScalaComponent.builder[Props]("Chat")
    .stateless
    .renderStatic(<.div("Chat"))
    .build

  def apply(props: Props): Unmounted[Props, Unit, Unit] = chat(props)
}
