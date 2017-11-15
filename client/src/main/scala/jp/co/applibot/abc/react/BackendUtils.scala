package jp.co.applibot.abc.react

import japgolly.scalajs.react._

import scala.scalajs.js

trait BackendUtils[P, S] {
  val bs: BackendScope[P, S]

  final protected val update: js.Function1[S, Unit] = (state) => {
    bs.setState(state).runNow()
  }

  def callbackWithPS(f: (P, S) => Unit): Callback = bs.props.flatMap(p => bs.state.map(s => f(p, s)))
}
