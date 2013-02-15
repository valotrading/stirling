package stirling.mbtrading.quoteapi.config

import java.net.InetSocketAddress

trait MbtMessageClientConfig {
  def address: InetSocketAddress
}
