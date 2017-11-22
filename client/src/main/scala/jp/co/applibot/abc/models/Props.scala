package jp.co.applibot.abc.models

import jp.co.applibot.abc.{Actions, WebActions}
import jp.co.applibot.react.Router.Router

case class Props(state: State, actions: Actions, webActions: WebActions, router: Router)
