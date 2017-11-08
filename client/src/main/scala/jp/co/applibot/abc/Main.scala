package jp.co.applibot.abc

import japgolly.scalajs.react.extra.router._
import Configuration._

object Main {
  def main(args: Array[String]): Unit = {
    val router = Router(baseUrl, routerConfig)
    router().renderIntoDOM(reactRootElement)

    WebAPI.getTest
  }
}
