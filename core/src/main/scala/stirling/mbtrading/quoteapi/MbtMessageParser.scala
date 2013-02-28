package stirling.mbtrading.quoteapi

import java.nio.ByteBuffer
import silvertip.MessageParser

object MbtMessageParser extends MessageParser[MbtMessage] {
  def parse(buffer: ByteBuffer) = MbtMessage.parse(buffer)
}
