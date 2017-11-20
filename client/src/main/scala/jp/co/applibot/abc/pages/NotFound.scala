package jp.co.applibot.abc.pages

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import jp.co.applibot.react.Router.Router

object NotFound {
  type Props = Router

  val notFound = ScalaComponent.builder[Props]("NotFound")
    .stateless
    .renderStatic{
      <.div(
        <.h1("Page not found.")
      )
    }
    .build

  def apply(props: Props) = notFound(props)
}
