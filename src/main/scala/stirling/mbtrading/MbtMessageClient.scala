package stirling.mbtrading

import com.twitter.logging.Logger
import java.nio.ByteBuffer
import java.util.Iterator
import scala.collection.JavaConversions._
import silvertip.Connection.Callback
import silvertip.{Connection, Events}
import stirling.mbtrading.config.MbtMessageClientConfig

class MbtMessageClient(val application: MbtMessageListener, val config: MbtMessageClientConfig) {
  def start = {
    try {
      log.info("Connecting to %s".format(config.address.toString))
      connection = Connection.connect(config.address, new MbtMessageParser, new Callback[MbtMessage] {
        def connected(connection: Connection[MbtMessage]) {
          log.info("Connected")
        }
        def closed(connection: Connection[MbtMessage]) {
          log.info("Connection closed")
        }
        def garbledMessage(connection: Connection[MbtMessage], message: String, data: Array[Byte]) {
          log.error("Received garbled message '%s'", message)
        }
        def idle(connection: Connection[MbtMessage]) {
        }
        def messages(connection: Connection[MbtMessage], messages: Iterator[MbtMessage]) {
          messages.foreach { message =>
            log.debug("Received '%s'", message)
            application.receive(message)
          }
        }
        def sent(buffer: ByteBuffer) {
        }
      })
      events.register(connection)
      log.info("Connected, logging in")
      application.login
      events.dispatch(idleEventTimeoutMillis)
    } catch {
      case e: Throwable =>
        log.fatal(e, "Error while establishing connection")
    }
  }
  def send(message: MbtMessage) = connection.send(message.format.getBytes)
  private def idleEventTimeoutMillis = 50
  private var connection: Connection[MbtMessage] = null
  private val events = Events.open()
  private val log = Logger.get
}
