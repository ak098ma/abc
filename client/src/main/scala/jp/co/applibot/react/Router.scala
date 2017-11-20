package jp.co.applibot.react

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom
import org.scalajs.dom.raw.PopStateEvent

import scala.scalajs.js

object Router {
  private val popState = "popstate"
  private val hashChange = "hashchange"

  case class Router(pathname: String,
                    hash: String,
                    search: String,
                    push: String => Callback,
                    replace: String => Callback,
                    back: () => Callback,
                    forward: () => Callback)

  type Props = Router => VdomElement

  case class State(pathname: String = dom.window.location.pathname,
                   hash: String = dom.window.location.hash,
                   search: String = dom.window.location.search)

  class Backend(bs: BackendScope[Props, State]) {
    private def updateState = bs.modState { _ =>
      State(pathname = dom.window.location.pathname,
            hash = dom.window.location.hash,
            search = dom.window.location.search)
    }
    private val handleChangeURL: js.Function1[PopStateEvent, Unit] = (_) => updateState.runNow()

    def componentWillMount = Callback {
      dom.window.addEventListener(popState, handleChangeURL)
      dom.window.addEventListener(hashChange, handleChangeURL)
    }

    def render(props: Props, state: State) = props(Router(
      pathname = state.pathname,
      hash = state.hash,
      search = state.search,
      push = push,
      replace = replace,
      back = back,
      forward = forward,
    ))

    def componentWillUnmount = Callback {
      dom.window.removeEventListener(popState, handleChangeURL)
      dom.window.removeEventListener(hashChange, handleChangeURL)
    }

    private def push(url: String): Callback = {
      dom.window.history.pushState(null, null, url)
      updateState
    }

    private def replace(url: String): Callback = {
      dom.window.history.replaceState(null, null, url)
      updateState
    }

    private def back(): Callback = {
      dom.window.history.back()
      updateState
    }

    private def forward(): Callback = {
      dom.window.history.forward()
      updateState
    }
  }

  private val router = ScalaComponent.builder[Props]("Router")
    .initialState(State())
    .backend(new Backend(_))
    .renderBackend
    .componentWillMount(_.backend.componentWillMount)
    .componentWillUnmount(_.backend.componentWillUnmount)
    .build

  def apply(props: Props) = router(props)
}
