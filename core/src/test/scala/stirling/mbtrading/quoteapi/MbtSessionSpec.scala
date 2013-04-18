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
package stirling.mbtrading.quoteapi

import org.scalatest.WordSpec
import org.scalatest.matchers.MustMatchers
import stirling.test.{Conductor, Event}
import stirling.test.mbtrading.quoteapi.{MbtTestClient, MbtTestServer}

class MbtSessionSpec extends WordSpec with MustMatchers {
  import MbtMessage.Tag

  "MBT Quote API" should {
    "maintain level 1 state per symbol" in {
      val server = newServer()
      val client = newClient()

      client.expect(Event.Connected)
      server.expect(Event.Connected)

      server.send(Level1Update()
        .updated(Tag.Symbol,  "SPY")
        .updated(Tag.LastBid, 154.0)
        .updated(Tag.LastAsk, 155.0)
      )

      client.expect(Level1Update()
        .updated(Tag.Symbol,  "SPY")
        .updated(Tag.LastBid, 154.0)
        .updated(Tag.LastAsk, 155.0)
      )

      server.send(Level1Update()
        .updated(Tag.Symbol,  "DIA")
        .updated(Tag.LastBid, 144.0)
        .updated(Tag.LastAsk, 145.0)
      )

      client.expect(Level1Update()
        .updated(Tag.Symbol,  "DIA")
        .updated(Tag.LastBid, 144.0)
        .updated(Tag.LastAsk, 145.0)
      )

      server.send(Level1Update()
        .updated(Tag.Symbol,  "SPY")
        .updated(Tag.Price,   155.0)
        .updated(Tag.LastAsk, 155.5)
      )

      client.expect(Level1Update()
        .updated(Tag.Symbol,  "SPY")
        .updated(Tag.Price,   155.0)
        .updated(Tag.LastBid, 154.0)
        .updated(Tag.LastAsk, 155.5)
      )

      Conductor.conduct(Seq(server, client))
    }

    "send and receive heartbeats" in {
      val server = newServer()
      val client = newClient()

      client.expect(Event.Connected)
      server.expect(Event.Connected)

      client.send(Heartbeat())
      server.expect(Heartbeat())

      server.send(Heartbeat())
      client.expect(Heartbeat())

      Conductor.conduct(Seq(server, client))
    }
  }

  def newServer() = new MbtTestServer(port = 6666)
  def newClient() = new MbtTestClient(hostname = "localhost", port = 6666)
}
