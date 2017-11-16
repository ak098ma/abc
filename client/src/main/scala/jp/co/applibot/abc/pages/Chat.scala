package jp.co.applibot.abc.pages

import japgolly.scalajs.react._
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._
import jp.co.applibot.abc.models.State
import jp.co.applibot.abc.mvc.actions.WebActions
import jp.co.applibot.abc.{Page, Store}
import jp.co.applibot.abc.react.BackendUtils

trait Chat {
  type Props = RouterCtl[Page]

  class Backend(override val bs: BackendScope[Props, State]) extends BackendUtils[Props, State] {
    def render(props: Props) = {
      <.div(
        "Chat"
      )
    }

    def componentWillMount: Callback = callbackWithPS { (props, state) =>
      Store.subscribe(update)
      Store.update(_.copy(router = Some(props)))
      WebActions.fetchUser()
      WebActions.fetchChatRooms()
    }

    def componentWillUnmount: Callback = Callback {
      Store.unsubscribe(update)
    }
  }

  private val chat = ScalaComponent.builder[Props]("Chat")
    .initialState(Store.getState)
    .backend(new Backend(_))
    .renderBackend
    .componentWillMount(_.backend.componentWillMount)
    .componentWillUnmount(_.backend.componentWillUnmount)
    .build

  def apply(props: Props): Unmounted[Props, State, Backend] = chat(props)
}
