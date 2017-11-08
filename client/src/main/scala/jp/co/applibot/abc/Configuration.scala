package jp.co.applibot.abc

import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.extra.router._
import org.scalajs.dom
import org.scalajs.dom.raw.Element

object Configuration {
  val reactRootElement: Element = dom.document.getElementById("react-root")

  val baseUrl: BaseUrl = BaseUrl.fromWindowOrigin

  val routerConfig: RouterConfig[Page] = RouterConfigDsl[Page].buildConfig { dsl =>
    import dsl._

    (emptyRule
      | staticRoute(root, Home) ~> render(Home())
      | staticRoute(root / "sign-up", SignUp) ~> render(SignUp())
      | staticRoute(root / "login", Login) ~> render(Login())
      | staticRoute(root / "chat", Chat) ~> render(Chat())
      ).notFound(redirectToPage(Home)(Redirect.Replace))
  }
}
