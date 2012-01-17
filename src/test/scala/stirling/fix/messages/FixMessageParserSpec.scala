/*
 * Copyright 2010 the original author or authors.
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
package stirling.fix.messages

import java.nio.ByteBuffer
import org.scalatest.{Assertions, WordSpec}
import org.scalatest.matchers.MustMatchers
import silvertip.{GarbledMessageException, PartialMessageException}

class FixMessageParserSpec extends WordSpec with MustMatchers with FixMessageParserFixtures {
  "FixMessageParser" should {
    val parser = new FixMessageParser()
    "throw an exception on an empty buffer" in {
      intercept[PartialMessageException] {
        parser.parse("")
      }
    }
    "throw an exception on a partial header" in {
      intercept[PartialMessageException] {
        parser.parse("8=FIX.4.2" + delimiter + "9=153")
      }
    }
    "throw an exception if the first field is not BeginString(8)" in {
      val message = "X=FIX.4.2" + delimiter + "9=5" + delimiter
      assertGarbledMessage(parser, message, "Expected tag not found: BeginString(8)")
    }
    "throw an exception on an empty BeginString(8)" in {
      val message = "8=" + delimiter + "9=5" + delimiter
      assertGarbledMessage(parser, message, "BeginString(8) is empty")
    }
    "throw an exception on an invalid BeginString(8)" in {
      val message = "8=XIF.10.1" + delimiter + "9=5" + delimiter
      assertGarbledMessage(parser, message, "BeginString(8) is invalid, expected format FIX.m.n: XIF.10.1")
    }
    "throw an exception if second field is missing" in {
      val message = "8=FIX.4.2" + delimiter
      assertGarbledMessage(parser, message, "Expected tag not found: BodyLength(9)")
    }
    "throw an exception if second field is not BodyLength(9)" in {
      val message = "8=FIX.4.2" + delimiter + "X=5" + delimiter
      assertGarbledMessage(parser, message, "Expected tag not found: BodyLength(9)")
    }
    "throw an exception on an empty BodyLength(9)" in {
      val message = "8=FIX.4.2" + delimiter + "9=" + delimiter + "108=XXX" + delimiter
      assertGarbledMessage(parser, message, "BodyLength(9) is empty")
    }
    "throw an exception on an invalid format in BodyLength(9)" in {
      val message = "8=FIX.4.2" + delimiter + "9=XXX" + delimiter + "108=XXX" + delimiter
      assertGarbledMessage(parser, message, "BodyLength(9) has invalid format: XXX")
    }
    "throw an exception if BodyLength(9) does not point to standard trailer" in {
      val header = "8=FIX.4.2" + delimiter + "9=5" + delimiter
      val payload = "35=E" + delimiter
      val trailer = "10=XXX" + delimiter
      val messages = header + payload + header + payload + trailer
      val garbled = header + payload
      assertGarbledMessage(parser, messages, garbled, "Expected tag not found: CheckSum(10)")
    }
    "throw an exception if the third field is not MsgType(35)" in {
      val message = "8=FIX.4.2" + delimiter + "9=5" + delimiter + "X=A" + delimiter
      assertGarbledMessage(parser, message, "Expected tag not found: MsgType(35)")
    }
    "throw an exception if the third field is missing" in {
      val message = "8=FIX.4.2" + delimiter + "9=5" + delimiter
      assertGarbledMessage(parser, message, "Expected tag not found: MsgType(35)")
    }
    "throw an exception on an empty MsgType(35)" in {
      val message = "8=FIX.4.2" + delimiter + "9=5" + delimiter + "35=" + delimiter + "108=XXX" + delimiter
      assertGarbledMessage(parser, message, "MsgType(35) is empty")
    }
    "throw an exception if CheckSum(10) is missing" in {
      val header = "8=FIX.4.2" + delimiter + "9=5" + delimiter
      val payload = "35=E" + delimiter
      val trailer = "11=XXX" + delimiter
      val message = header + payload + trailer
      assertGarbledMessage(parser, message, "Expected tag not found: CheckSum(10)")
    }
    "throw an exception on an empty CheckSum(10)" in {
      val header = "8=FIX.4.2" + delimiter + "9=5" + delimiter
      val payload = "35=E" + delimiter
      val trailer = "10=" + delimiter
      val message = header + payload + trailer
      assertGarbledMessage(parser, message, "CheckSum(10) is empty")
    }
    "throw an exception on an invalid CheckSum(10)" in {
      val message = "8=FIX.4.2" + delimiter + "9=5" + delimiter + "35=E" + delimiter + "10=XYZ" + delimiter
      assertGarbledMessage(parser, message, "CheckSum(10) has invalid format: XYZ")
    }
    "throw an exception on an invalid length on CheckSum(10)" in {
      val message = "8=FIX.4.2" + delimiter + "9=5" + delimiter + "35=E" + delimiter + "10=12" + delimiter
      assertGarbledMessage(parser, message, "CheckSum(10) has invalid length: 12")
    }
    "throw an exception on invalid CheckSum(10)" in {
      val message = "8=FIX.4.2" + delimiter + "9=5" + delimiter + "35=E" + delimiter + "10=123" + delimiter
      assertGarbledMessage(parser, message, "CheckSum(10) mismatch, expected=182, actual=123")
    }
    "throw an exception on a garbled message ending with 8SOH" in {
      val message = "8=FIX.4.2" + delimiter + "9=5" + delimiter + "8" + delimiter
      assertGarbledMessage(parser, message, "Expected tag not found: MsgType(35)")
    }
    "throw an exception on a garbled message ending with 8SOHSOH" in {
      val message = "8=FIX.4.2" + delimiter + "9=5" + delimiter + "8" + delimiter + delimiter
      assertGarbledMessage(parser, message, "Expected tag not found: MsgType(35)")
    }
    "throw an exception on a partial message" in {
      intercept[PartialMessageException] {
        parser.parse("8=FIX.4.2" + delimiter + "9=153" + delimiter)
      }
    }
    "accept a valid message" in {
      val header = "8=FIX.4.2" + delimiter + "9=153" + delimiter
      val payload = "35=E" + delimiter + "49=INST" + delimiter + "56=BROK" + delimiter + "52=20050908-15:51:22" +
        delimiter + "34=200" + delimiter + "66=14" + delimiter + "394=1" + delimiter + "68=2" + delimiter + "73=2" +
        delimiter + "11=order-1" + delimiter + "67=1" + delimiter + "55=IBM" + delimiter + "54=2" + delimiter +
        "38=2000" + delimiter + "40=1" + delimiter + "11=order-2" + delimiter + "67=2" + delimiter + "55=AOL" +
        delimiter + "54=2" + delimiter + "38=1000" + delimiter + "40=1" + delimiter
      val trailer = "10=020" + delimiter
      val message = parser.parse(header + payload + trailer)
      message.toString must equal(header + payload + trailer)
    }
  }
}

trait FixMessageParserFixtures extends Assertions with MustMatchers {
  def delimiter = '\001'
  def bufferSize = 4096
  implicit def stringToByteBuffer(string: String): ByteBuffer = {
    val buffer = ByteBuffer.allocate(bufferSize)
    buffer.put(string.getBytes)
    buffer.flip
    buffer.mark
    buffer
  }
  def assertGarbledMessage(parser: FixMessageParser, garbledMessage: String, expectedReason: String) {
    val validMessage = "8=FIX.4.2" + delimiter + "9=5" + delimiter + "35=E" + delimiter + "10=191" + delimiter
    assertGarbledMessage(parser, garbledMessage + validMessage, garbledMessage, expectedReason)
  }
  def assertGarbledMessage(parser: FixMessageParser, messages: String, expectedGarbledMessage: String, expectedReason: String) {
    val exception = intercept[GarbledMessageException] {
      parser.parse(messages)
    }
    new String(exception.getMessageData) must equal(expectedGarbledMessage)
    exception.getMessage must equal(expectedReason)
  }
}
