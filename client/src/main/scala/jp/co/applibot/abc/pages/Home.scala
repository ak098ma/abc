package jp.co.applibot.abc.pages

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.extra.router.RouterCtl
import jp.co.applibot.abc.Page

trait Home {
  type Props = RouterCtl[Page]

  val home = ScalaComponent.builder[Props]("Home")
    .stateless
    .render_P( props =>
      <.div(
        <.h1("Welcome to Applibot Chat")
      )
    )
    .build

  def apply(props: Props) = home(props)
}
