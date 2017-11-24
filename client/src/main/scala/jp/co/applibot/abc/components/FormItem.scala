package jp.co.applibot.abc.components

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import jp.co.applibot.abc.shared.styles

import scalacss.ScalaCssReact._

object FormItem {

  case class Props(label: String, value: String, placeholder: String, onChange: String => Callback, isCredential: Boolean = false)

  class Backend(bs: BackendScope[Props, Unit]) {
    def render(props: Props) = {
      <.div(
        styles.FormItem.formItem,
        <.div(
          styles.FormItem.label,
          props.label,
        ),
        <.div(
          styles.FormItem.inputContainer,
          <.input(
            styles.FormItem.input,
            ^.`type` := (if (props.isCredential) "password" else ""),
            ^.placeholder := props.placeholder,
            ^.value := props.value,
            ^.onChange ==> ((event: ReactEventFromInput) => {
              val value = event.target.value
              props.onChange(value)
            })
          )
        )
      )
    }
  }

  private val formItem = ScalaComponent.builder[Props]("FormItem")
    .stateless
    .backend(new Backend(_))
    .renderBackend
    .build

  def apply(props: Props) = formItem(props)
}
