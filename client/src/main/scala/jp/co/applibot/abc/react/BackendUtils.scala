package jp.co.applibot.abc.react

import japgolly.scalajs.react._

trait BackendUtils[P, S] {
  val bs: BackendScope[P, S]

  def callbackWithPS(f: (P, S) => Unit): Callback = bs.props.flatMap(p => bs.state.map(s => f(p, s)))
}
