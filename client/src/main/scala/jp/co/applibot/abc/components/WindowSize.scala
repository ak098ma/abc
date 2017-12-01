package jp.co.applibot.abc.components

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.VdomElement
import org.scalajs.dom._

import scala.scalajs.js

object WindowSize {
  type Props = State => VdomElement
  case class State(width: Double = window.innerWidth,
                   height: Double = window.innerHeight)

  class Backend(bs: BackendScope[Props, State]) {
    def render(props: Props, state: State) = props(state)

    private val onResize: js.Function1[Event, Unit] = (event) => {
      val window = event.target.asInstanceOf[Window]
      bs.modState(_.copy(
        width = window.innerWidth,
        height = window.innerHeight,
      )).runNow()
    }

    def componentWillMount = Callback {
      window.addEventListener("resize", onResize)
    }

    def componentWillUnmount = Callback {
      window.removeEventListener("resize", onResize)
    }
  }

  private val windowSize = ScalaComponent.builder[Props]("Window")
    .initialState(State())
    .backend(new Backend(_))
    .renderBackend
    .componentWillMount(_.backend.componentWillMount)
    .componentWillUnmount(_.backend.componentWillUnmount)
    .build

  def apply(props: Props) = windowSize(props)
}
