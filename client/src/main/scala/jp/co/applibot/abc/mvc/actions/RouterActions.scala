package jp.co.applibot.abc.mvc.actions

import jp.co.applibot.abc.{Page, Store}
import org.scalajs.dom.window

object RouterActions {
  def gotoPage(page: Page): Unit = {
    Store.getState.router.foreach { routerCtl =>
      if (window.location.pathname != routerCtl.pathFor(page).value) {
        routerCtl.set(page).runNow()
        Store.update(_.copy(currentPage = Some(page)))
      }
    }
  }
}
