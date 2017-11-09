package jp.co.applibot.flux

import scala.scalajs.js

class Store[S](private var state: S) {
  type Callback = js.Function1[S, Unit]

  private var subscribers: Set[Callback] = Set.empty[Callback]

  def getState: S = state

  def subscribe(callback: Callback): Unit = subscribers = subscribers + callback

  def unsubscribe(callback: Callback): Unit = subscribers = subscribers - callback

  def update(modify: S => S): Unit = {
    state = modify(state)
    subscribers.foreach(_(state))
  }
}
