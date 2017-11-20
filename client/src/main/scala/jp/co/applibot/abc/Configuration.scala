package jp.co.applibot.abc

import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.extra.router._
import org.scalajs.dom
import org.scalajs.dom.raw.Element
import Page._

object Configuration {
  val reactRootElement: Element = dom.document.getElementById("react-root")

  val baseUrl: BaseUrl = BaseUrl.fromWindowOrigin

  val routerConfig: RouterConfig[Page] = RouterConfigDsl[Page].buildConfig { dsl =>
    import dsl._

    (removeTrailingSlashes
      | staticRoute(root, Home) ~> renderR(Home(_))
      | staticRoute(root / "sign-up", SignUp) ~> renderR(SignUp(_))
      | staticRoute(root / "login", Login) ~> renderR(Login(_))
      | staticRoute(root / "chat", Chat) ~> renderR(Login(_))
      | staticRoute(root / "not-found", NotFound) ~> renderR(NotFound(_))
      ).notFound(redirectToPage(NotFound)(Redirect.Replace))
       .logToConsole
  }
}
