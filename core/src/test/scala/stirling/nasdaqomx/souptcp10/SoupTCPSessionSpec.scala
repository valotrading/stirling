/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package stirling.nasdaqomx.souptcp10

import org.scalatest.WordSpec
import org.scalatest.matchers.MustMatchers
import stirling.test.{Conductor, Event}
import stirling.test.nasdaqomx.souptcp10.{SoupTCPTestClient, SoupTCPTestServer}

class SoupTCPSessionSpec extends WordSpec with MustMatchers {
  "SoupTCP" should {
    "handle login request" in {
      val server = newServer()
      val client = newClient()

      client.expect(Event.Connected)
      server.expect(Event.Connected)

      client.login(
        username                = "Bob",
        password                = "ABCD",
        requestedSession        = "EFGH",
        requestedSequenceNumber = 1234
      )
      server.expect(SoupTCPServer.Event.LoginRequest(
        username                = "Bob",
        password                = "ABCD",
        requestedSession        = "EFGH",
        requestedSequenceNumber = 1234
      ))

      Conductor.conduct(Seq(server, client))
    }
    "handle login rejection" in {
      val server = newServer()
      val client = newClient()

      client.expect(Event.Connected)
      server.expect(Event.Connected)

      client.login(
        username                = "Bob",
        password                = "ABCD",
        requestedSession        = "EFGH",
        requestedSequenceNumber = 1234
      )
      server.expectOne()

      server.rejectLogin(LoginRejectCode.NotAuthorized)
      client.expect(SoupTCPClient.Event.LoginRejected(
        LoginRejectCode.NotAuthorized
      ))

      Conductor.conduct(Seq(server, client))
    }
    "handle login acceptance" in {
      val server = newServer()
      val client = newClient()

      client.expect(Event.Connected)
      server.expect(Event.Connected)

      client.login(
        username                = "Bob",
        password                = "ABCD",
        requestedSession        = "EFGH",
        requestedSequenceNumber = 1234
      )
      server.expectOne()

      server.acceptLogin(
        session        = "EFGH",
        sequenceNumber = 1234
      )
      client.expect(SoupTCPClient.Event.LoginAccepted(
        session        = "EFGH",
        sequenceNumber = 1234
      ))

      Conductor.conduct(Seq(server, client))
    }
    "handle uplink debug message" in {
      val server = newServer()
      val client = newClient()

      login(client, server)

      client.debug("Hello")
      server.expect(SoupTCPServer.Event.Debug("Hello"))

      Conductor.conduct(Seq(server, client))
    }
    "handle downlink debug message" in {
      val server = newServer()
      val client = newClient()

      login(client, server)

      server.debug("Hello")
      client.expect(SoupTCPClient.Event.Debug("Hello"))

      Conductor.conduct(Seq(server, client))
    }
    "handle heartbeat" in {
      val server = newServer(heartbeatInterval = 100)
      val client = newClient(heartbeatInterval = 100)

      login(client, server)

      client.expect(SoupTCPClient.Event.ServerHeartbeat)
      server.expect(SoupTCPServer.Event.ClientHeartbeat)

      Conductor.conduct(Seq(server, client))
    }
    "handle uplink going down" in {
      val server = newServer(heartbeatInterval = 5000)
      val client = newClient(heartbeatInterval = 500)

      login(client, server)

      server.expect(SoupTCPServer.Event.ClientHeartbeat)
      server.expect(SoupTCPServer.Event.ClientHeartbeat)
      client.expect(SoupTCPClient.Event.LinkDown)

      Conductor.conduct(Seq(server, client), timeout = 4000)
    }
    "handle downlink going down" in {
      val server = newServer(heartbeatInterval = 500)
      val client = newClient(heartbeatInterval = 5000)

      login(client, server)

      client.expect(SoupTCPClient.Event.ServerHeartbeat)
      client.expect(SoupTCPClient.Event.ServerHeartbeat)
      server.expect(SoupTCPServer.Event.LinkDown)

      Conductor.conduct(Seq(server, client))
    }
    "handle sequenced data" in {
      val server = newServer()
      val client = newClient()

      login(client, server)

      server.send(HelloMessage.length) { buffer =>
        buffer.put(HELO.bytes)
      }
      client.expect(SoupTCPClient.Event.SequencedData(
        message = HELO
      ))

      Conductor.conduct(Seq(server, client))
    }
    "handle unsequenced data" in {
      val server = newServer()
      val client = newClient()

      login(client, server)

      client.send(HelloMessage.length) { buffer =>
        buffer.put(EHLO.bytes)
      }
      server.expect(SoupTCPServer.Event.UnsequencedData(
        message = EHLO
      ))

      Conductor.conduct(Seq(server, client))

    }
    "handle end of session" in {
      val server = newServer()
      val client = newClient()

      login(client, server)

      server.endSession()
      client.expect(SoupTCPClient.Event.EndOfSession)

      Conductor.conduct(Seq(server, client))
    }
    "handle logout request" in {
      val server = newServer()
      val client = newClient()

      login(client, server)

      client.logout()
      server.expect(SoupTCPServer.Event.LogoutRequest)

      Conductor.conduct(Seq(server, client))
    }
  }

  def login(client: SoupTCPTestClient[HelloMessage], server: SoupTCPTestServer[HelloMessage]) {
    client.expect(Event.Connected)
    server.expect(Event.Connected)

    client.login(
      username                = "Bob",
      password                = "ABCD",
      requestedSession        = "EFGH",
      requestedSequenceNumber = 1234
    )
    server.expectOne()

    server.acceptLogin(
      session        = "EFGH",
      sequenceNumber = 1234
    )
    client.expectOne()
  }

  def newServer(heartbeatInterval: Long = 5000) = new SoupTCPTestServer(
    parser   = HelloParser,
    port     = 6666,
    settings = newSettings(heartbeatInterval)
  )

  def newClient(heartbeatInterval: Long = 5000) = new SoupTCPTestClient(
    parser   = HelloParser,
    hostname = "localhost",
    port     = 6666,
    settings = newSettings(heartbeatInterval)
  )

  def newSettings(heartbeatInterval: Long) = Settings(
    heartbeatInterval = heartbeatInterval,
    maxSilentDuration = (heartbeatInterval * 2.5).toLong
  )
}
