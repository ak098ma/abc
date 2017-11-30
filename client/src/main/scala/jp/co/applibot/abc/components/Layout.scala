package jp.co.applibot.abc.components

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import jp.co.applibot.abc.shared.styles

import scalacss.ScalaCssReact._

object Layout {

  case class Props(state: jp.co.applibot.abc.models.Props, content: VdomElement)
  case class AnimationState(modalOption: Option[jp.co.applibot.abc.models.Props => VdomElement], isModalClosing: Boolean = false)

  class Backend(bs: BackendScope[Props, AnimationState]) {
    def render(props: Props, animationState: AnimationState) = {
      def isHomePage = props.state.router.pathname == "/"
      def isModalOpen = props.state.state.modal.renderModalOption.nonEmpty
      def isModalClosing = animationState.isModalClosing

      <.div(
        styles.Layout.container,
        <.header(
          styles.Layout.header,
          <.img(
            styles.Layout.logo,
            if (!isHomePage) styles.Layout.logoActive else EmptyVdom,
            ^.src := "/assets/images/favicon.png",
            ^.onClick --> Callback.when(!isHomePage)(props.state.router.push("/"))
          ),
        ),
        <.div(
          styles.Layout.content,
          props.content,
        ),
        <.div(
          if (isModalOpen) styles.Layout.modal else if (isModalClosing) styles.Layout.modalClosing else styles.Layout.modalClosed,
          animationState.modalOption.map(renderer => <.div(renderer(props.state), ^.onClick ==> ((event: ReactMouseEvent) => event.stopPropagationCB))).getOrElse(EmptyVdom),
          ^.onClick --> props.state.actions.closeModal,
        ),
      )
    }

    def componentWillReceiveProps(currentProps: Props, nextProps: Props) = {
      if (currentProps.state.state.modal.renderModalOption.nonEmpty && nextProps.state.state.modal.renderModalOption.isEmpty) {
        org.scalajs.dom.window.setTimeout(() => {
          bs.modState(_.copy(modalOption = None, isModalClosing = false)).runNow()
        }, styles.Layout.closingDurationMillis.toMillis)
        bs.modState(_.copy(isModalClosing = true))
      } else if (currentProps.state.state.modal.renderModalOption != nextProps.state.state.modal.renderModalOption) {
        bs.modState(_.copy(modalOption = nextProps.state.state.modal.renderModalOption))
      } else {
        Callback.empty
      }
    }
  }

  private val header = ScalaComponent.builder[Props]("Header")
    .initialStateFromProps(props => AnimationState(modalOption = props.state.state.modal.renderModalOption))
    .backend(new Backend(_))
    .renderBackend
    .componentWillReceiveProps(cwrp => cwrp.backend.componentWillReceiveProps(cwrp.currentProps, cwrp.nextProps))
    .build

  def apply(props: Props) = header(props)
}
