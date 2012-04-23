package stirling.mbtrading.config

import java.net.InetSocketAddress

trait MbtMessageClientConfig {
  def address: InetSocketAddress
}
