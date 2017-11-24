package jp.co.applibot.abc.actions

import japgolly.scalajs.react.Callback
import jp.co.applibot.abc.models.State
import jp.co.applibot.abc.shared.models.{JsonWebToken, User, UserCredential}
import jp.co.applibot.abc.web.APIClient
import jp.co.applibot.react.Router
import org.scalajs.dom.experimental.Response
import play.api.libs.json.{JsValue, Json}

import scala.concurrent.{ExecutionContext, Future}

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
    Callback.future(APIClient.login(userCredential).map { response =>
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
