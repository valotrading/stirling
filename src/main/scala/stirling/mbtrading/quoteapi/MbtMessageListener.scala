package stirling.mbtrading.quoteapi

trait MbtMessageListener {
  def login
  def receive(message: MbtMessage)
}
