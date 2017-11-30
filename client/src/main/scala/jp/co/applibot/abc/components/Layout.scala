package jp.co.applibot.abc.components

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import jp.co.applibot.abc.actions.Actions
import jp.co.applibot.abc.models.State
import jp.co.applibot.abc.shared.styles
import jp.co.applibot.react.Router.Router

import scalacss.ScalaCssReact._

object Layout {

  case class Props(state: State, actions: Actions, content: VdomElement, router: Router)
  case class AnimationState(modalOption: Option[VdomElement], isModalClosing: Boolean = false)

  class Backend(bs: BackendScope[Props, AnimationState]) {
    def render(props: Props, animationState: AnimationState) = {
      def isHomePage = props.router.pathname == "/"
      def isModalOpen = props.state.modal.modalOption.nonEmpty
      def isModalClosing = animationState.isModalClosing

      <.div(
        styles.Layout.container,
        <.header(
          styles.Layout.header,
          <.img(
            styles.Layout.logo,
            if (!isHomePage) styles.Layout.logoActive else EmptyVdom,
            ^.src := "/assets/images/favicon.png",
            ^.onClick --> Callback.when(!isHomePage)(props.router.push("/"))
          ),
        ),
        <.div(
          styles.Layout.content,
          props.content,
        ),
        <.div(
          if (isModalOpen) styles.Layout.modal else if (isModalClosing) styles.Layout.modalClosing else styles.Layout.modalClosed,
          animationState.modalOption.map(vdom => <.div(vdom, styles.Layout.modalContent, ^.onClick ==> ((event: ReactMouseEvent) => event.stopPropagationCB))).getOrElse(EmptyVdom),
          ^.onClick --> props.actions.closeModal,
        ),
      )
    }

    def componentWillReceiveProps(currentProps: Props, nextProps: Props) = {
      if (currentProps.state.modal.modalOption.nonEmpty && nextProps.state.modal.modalOption.isEmpty) {
        org.scalajs.dom.window.setTimeout(() => {
          bs.modState(_.copy(modalOption = None, isModalClosing = false)).runNow()
        }, styles.Layout.closingDurationMillis.toMillis)
        bs.modState(_.copy(isModalClosing = true))
      } else if (currentProps.state.modal.modalOption.isEmpty && nextProps.state.modal.modalOption.nonEmpty) {
        bs.modState(_.copy(modalOption = nextProps.state.modal.modalOption))
      } else {
        Callback.empty
      }
    }
  }

  private val header = ScalaComponent.builder[Props]("Header")
    .initialStateFromProps(props => AnimationState(modalOption = props.state.modal.modalOption))
    .backend(new Backend(_))
    .renderBackend
    .componentWillReceiveProps(cwrp => cwrp.backend.componentWillReceiveProps(cwrp.currentProps, cwrp.nextProps))
    .build

  def apply(props: Props) = header(props)
}
