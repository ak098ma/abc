package jp.co.applibot.abc

import japgolly.scalajs.react.Callback
import japgolly.scalajs.react.vdom.html_<^._
import jp.co.applibot.abc.components.Layout
import jp.co.applibot.abc.models._
import jp.co.applibot.abc.pages._
import jp.co.applibot.abc.shared.models.{JsonWebToken, User, UserCredential}
import jp.co.applibot.abc.web.APIClient
import jp.co.applibot.react.Router
import org.scalajs.dom
import org.scalajs.dom.experimental.Response
import play.api.libs.json.{JsValue, Json}

import scala.concurrent.{ExecutionContext, Future}

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
          content = content,
          router = router,
        )
        Layout(layoutProps)
      }
    }.renderIntoDOM(reactRootElement)
  }
}

class WebActions(state: State, actions: Actions, router: Router.Router)(implicit executionContext: ExecutionContext) {
  implicit class ResponseUtil(response: Response) {
    def getJson: Future[JsValue] = response
      .json()
      .toFuture
      .map(any => Json.parse(scala.scalajs.js.JSON.stringify(any)))

    def getText: Future[String] = response.text().toFuture
  }

  def signUp: Callback = {
    val user = User(
      id = state.signUp.id,
      nickname = state.signUp.nickname,
      password = state.signUp.password,
      joiningChatRooms = Seq.empty[String],
    )
    Callback.future(APIClient.signUp(user).map { response =>
      response.status match {
        case 200 =>
          router.push("/login")
        case 400 =>
          Callback.warn("bad request")
        case 409 =>
          Callback.warn("すでに登録されているアカウントです。")
        case _ =>
          Callback.warn("unhandled")
      }
    })
  }

  def login: Callback = {
    val userCredential = UserCredential(
      id = state.login.id,
      password = state.login.password,
    )
    Callback.future(APIClient.login(userCredential).map{ response =>
      response.status match {
        case 200 =>
          Callback.future(response.getJson.map(Json.fromJson[JsonWebToken]).map(_.get).map { jwt =>
            actions.setToken(jwt.token) >> router.push("/chat")
          })
        case 401 =>
          Callback.warn("ユーザーIDもしくはパスワードが間違っています。")
        case _ =>
          Callback.empty
      }
    })
  }
}
