package jp.co.applibot.abc

import japgolly.scalajs.react.vdom.html_<^._
import jp.co.applibot.abc.actions.WebActions
import jp.co.applibot.abc.components.Layout
import jp.co.applibot.abc.models._
import jp.co.applibot.abc.pages._
import jp.co.applibot.react.Router
import org.scalajs.dom

object Main {
  val reactRootElement: dom.Element = dom.document.getElementById("react-root")

  def main(args: Array[String]): Unit = {
    Store { (state, actions) =>
      Router { router =>
        val webActions = new WebActions(state, actions, router)(scala.concurrent.ExecutionContext.Implicits.global)
        val props = Props(
          state = state,
          actions = actions,
          webActions = webActions,
          router = router,
        )
        val content = router.pathname match {
          case "/" => Home(props)
          case "/sign-up" => SignUp(props)
          case "/login" => Login(props)
          case "/chat" => Chat(props)
          case _ => NotFound(props)
        }
        val layoutProps = Layout.Props(
          state = props,
          content = content,
        )
        Layout(layoutProps)
      }
    }.renderIntoDOM(reactRootElement)
  }
}
