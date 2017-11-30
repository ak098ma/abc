package jp.co.applibot.abc.shared.styles

import scalacss.DevDefaults._
import scala.concurrent.duration._

object Layout extends StyleSheet.Inline {
  import dsl._

  private val headerHeight = 80
  private val max = 100.%%
  private val logoSize = 64.px

  val container = style(
    display.flex,
    flexDirection.column,
    width(max),
    minWidth(768.px),
    height(max),
    backgroundColor(Color(Colors.grey50)),
  )

  val header = style(
    flex := s"0 0 ${headerHeight}px",
    borderWidth(2.px, 0.px, 0.px, 0.px),
    borderStyle.solid,
    borderColor(Color(Colors.lightBlue500)),
    backgroundColor(Color(Colors.white)),
    width(max),
    textAlign.center,
  )

  val logo = style(
    margin(8.px, 0.px),
    maxWidth(logoSize),
    maxHeight(logoSize),
  )

  val logoActive = style(cursor.pointer)

  val content = style(
    display.flex,
    flex := "1 0 0",
    width(max),
  )

  val closingDurationMillis = 0.3.seconds
  val modalBackground = Color("rgba(0,0,0,0.4)")

  val modalMixin = mixin(
    position.absolute,
    width(max),
    height(max),
    animationDuration(closingDurationMillis),
  )

  val openingAnimation = keyframes(
    0.%% -> keyframe(
      backgroundColor(Color(Colors.transparent)),
      opacity(0),
    ),
    100.%% -> keyframe(
      backgroundColor(modalBackground),
      opacity(1),
    )
  )

  val closingAnimation = keyframes(
    0.%% -> keyframe(
      backgroundColor(modalBackground),
      opacity(1),
    ),
    100.%% -> keyframe(
      backgroundColor(Color(Colors.transparent)),
      opacity(0),
    )
  )

  val modal = style(
    modalMixin,
    backgroundColor(modalBackground),
    animationName(openingAnimation),
    animationIterationCount.count(1),
    display.flex,
    justifyContent.center,
    alignItems.center,
    opacity(1),
  )

  val modalClosing = style(
    modalMixin,
    display.flex,
    justifyContent.center,
    alignItems.center,
    opacity(0),
    backgroundColor(Color(Colors.transparent)),
    animationName(closingAnimation),
    animationIterationCount.count(1),
  )

  val modalClosed = style(
    modalMixin,
    display.none,
    backgroundColor(Color(Colors.transparent)),
    opacity(0),
  )

  val modalContent = style(
    backgroundColor(Color(Colors.white)),
  )
}
