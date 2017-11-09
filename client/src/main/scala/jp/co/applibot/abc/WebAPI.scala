package jp.co.applibot.abc

import fr.hmil.roshttp.HttpRequest
import japgolly.scalajs.react.extra.router.BaseUrl
import monix.execution.Scheduler

object WebAPI {
  private def requestFromBaseURL(resolve: BaseUrl => BaseUrl) = HttpRequest(resolve(Configuration.baseUrl).value)

  def getTest(implicit scheduler: Scheduler) = {
    requestFromBaseURL(_ / "sign-up")
      .send()
      .map(_.body)
      .foreach(println)
  }
}
