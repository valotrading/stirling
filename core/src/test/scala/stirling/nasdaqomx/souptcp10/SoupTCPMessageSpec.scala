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

import java.nio.ByteBuffer
import org.scalatest.WordSpec
import org.scalatest.matchers.MustMatchers

class SoupTCPMessageSpec extends WordSpec with MustMatchers {
  "SoupTCP" should {
    "format and parse a Debug packet" in {
      val buffer = ByteBuffer.allocate(Debug.minLength + 4)

      Debug.format(
        buffer = buffer,
        text   = "HELO"
      )

      parse(buffer) must equal(Debug(
        text = "HELO"
      ))
    }
    "format and parse a Login Accepted packet" in {
      val buffer = ByteBuffer.allocate(LoginAccepted.length)

      LoginAccepted.format(
        buffer         = buffer,
        session        = "ABC",
        sequenceNumber = 3
      )

      parse(buffer) must equal(LoginAccepted(
        session        = "ABC",
        sequenceNumber = 3
      ))
    }

    "format and parse a Login Rejected packet" in {
      val buffer = ByteBuffer.allocate(LoginRejected.length)

      LoginRejected.format(
        buffer           = buffer,
        rejectReasonCode = 'A'
      )

      parse(buffer) must equal(LoginRejected(
        rejectReasonCode = LoginRejectCode.NotAuthorized
      ))
    }
    "format and parse a Sequenced Data packet" in {
      val buffer = ByteBuffer.allocate(SequencedData.minLength + 8)

      SequencedData.format(buffer) { buffer =>
        buffer.putShort(64)
      }

      parse(buffer) match {
        case SequencedData(buffer) =>
          buffer must equal(Array(0x00, 0x40))
        case _ =>
          fail
      }
    }
    "format and parse Server Heartbeat packet" in {
      val buffer = ByteBuffer.allocate(ServerHeartbeat.length)

      ServerHeartbeat.format(buffer)

      parse(buffer) must equal(ServerHeartbeat)
    }
    "format and parse a Login Request packet" in {
      val buffer = ByteBuffer.allocate(LoginRequest.length)

      LoginRequest.format(
        buffer                  = buffer,
        username                = "Donald",
        password                = "PASSWORD",
        requestedSession        = "A",
        requestedSequenceNumber = 3
      )

      parse(buffer) must equal(LoginRequest(
        username                = "Donald",
        password                = "PASSWORD",
        requestedSession        = "A",
        requestedSequenceNumber = 3
      ))
    }
    "format and parse an Unsequenced Data packet" in {
      val buffer = ByteBuffer.allocate(UnsequencedData.minLength + 8)

      UnsequencedData.format(buffer) { buffer =>
        buffer.putShort(64)
      }

      parse(buffer) match {
        case UnsequencedData(buffer) =>
          buffer must equal(Array(0x00, 0x40))
        case _ =>
          fail
      }
    }
    "format and parse a Client Heartbeat packet" in {
      val buffer = ByteBuffer.allocate(ClientHeartbeat.length)

      ClientHeartbeat.format(buffer)

      parse(buffer) must equal(ClientHeartbeat)
    }
    "format and parse a Logout Request packet" in {
      val buffer = ByteBuffer.allocate(LogoutRequest.length)

      LogoutRequest.format(buffer)

      parse(buffer) must equal(LogoutRequest)
    }
  }

  private def parse(buffer: ByteBuffer): Packet = {
    buffer.flip()
    SoupTCPParser.parse(buffer)
  }
}
