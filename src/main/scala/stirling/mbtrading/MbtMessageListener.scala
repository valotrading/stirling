package stirling.mbtrading

trait MbtMessageListener {
  def login
  def receive(message: MbtMessage)
}
