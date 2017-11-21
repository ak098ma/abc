package jp.co.applibot.abc.pages

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import jp.co.applibot.abc.models.Props
import scalacss.ScalaCssReact._
import jp.co.applibot.abc.shared.styles

object Home {
  val home = ScalaComponent.builder[Props]("Home")
    .stateless
    .render_P((props) =>
      <.div(
        styles.Home.container,
        <.header(
          styles.Home.header,
          <.img(styles.Home.logo, ^.src := "/assets/images/favicon.png"),
        ),
        <.div(
          styles.Home.content,
          <.div(
            styles.Home.alternative,
            <.div(
              styles.Home.panel,
              <.button(
                styles.Home.signUp,
                ^.onClick --> props.router.push("/sign-up"),
              ),
            ),
            <.div(
              styles.Home.panel,
              <.button(
                styles.Home.login,
                ^.onClick --> props.router.push("/login"),
              )
            ),
          ),
        ),
      )
    )
    .build

  def apply(props: Props) = home(props)
}
