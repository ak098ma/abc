package jp.co.applibot.abc.web

import java.nio.ByteBuffer

import fr.hmil.roshttp.body.BulkBodyPart
import play.api.libs.json.JsValue

class PlayJsonBody private(value: JsValue) extends BulkBodyPart {
  override def contentType: String = s"application/json; charset=utf-8"

  override def contentData: ByteBuffer = ByteBuffer.wrap(value.toString().getBytes("utf-8"))
}

object PlayJsonBody {
  def apply(value: JsValue) = new PlayJsonBody(value)
}
