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

import java.lang.Integer
import org.hamcrest.{BaseMatcher, Description}
import org.mockito.Mockito._
import org.mockito.Matchers._
import org.scalatest.{OneInstancePerTest, WordSpec}
import org.scalatest.matchers.MustMatchers
import org.scalatest.mock.MockitoSugar
import stirling.fix.tags.fix43.SessionRejectReason

class ParserSpec extends WordSpec with MustMatchers with MockitoSugar with ParserFixtures {
  "Parser" when {
    val msgType = "0"
    "parsing a full message" should {
      val raw = message("57", msgType)
        .field(MsgSeqNum, "1")
        .field(SendingTime, "20100701-12:09:40")
        .field(TestReqID, "1")
        .field(CheckSum, "206")
        .toString
      "invoke callback" in {
        val callback = mock[Parser.Callback]
        Parser.parse(FixMessage.fromString(raw, msgType), callback)
        verify(callback).message(argThat(matches(raw)))
      }
      "parse MsgSeqNum" in {
        Parser.parseMsgSeqNum(FixMessage.fromString(raw, msgType)) must equal(1)
      }
    }
    "MsgSeqNum(34) is missing" should {
      val raw = message
        .field(BeginString, "FIX.4.2")
        .field(BodyLength, "46")
        .field(MsgType, msgType)
        .field(SenderCompID, "Sender")
        .field(TargetCompID, "Target")
        .field(SendingTime, "20100701-12:09:40")
        .field(CheckSum, "243")
        .toString
      "throw an exception" in {
        val exception = intercept[ParseException] {
          Parser.parseMsgSeqNum(FixMessage.fromString(raw, msgType))
        }
        exception.getMessage must equal("MsgSeqNum(34) is missing")
      }
    }
    "MsgSeqNum(34) has invalid format" should {
      val raw = message("57", msgType)
        .field(MsgSeqNum, "X")
        .field(SendingTime, "20100701-12:09:40")
        .field(TestReqID, "1")
        .field(CheckSum, "206")
        .toString
      "throw an exception" in {
        val exception = intercept[InvalidValueFormatException] {
          Parser.parseMsgSeqNum(FixMessage.fromString(raw, msgType))
        }
        exception.getMessage must equal("MsgSeqNum(34) has invalid value format: X")
      }
    }
    "optional field is missing" should {
      val raw = message("51", msgType)
        .field(MsgSeqNum, "1")
        .field(SendingTime, "20100701-12:09:40")
        .field(CheckSum, "197")
        .toString
      "invoke the callback" in {
        val callback = mock[Parser.Callback]
        Parser.parse(FixMessage.fromString(raw, msgType), callback)
        verify(callback).message(argThat(matches(raw)))
      }
    }
    "parsing an empty tag" should {
      val raw = message("56", msgType)
        .field(MsgSeqNum, "1")
        .field(SendingTime, "20100701-12:09:40")
        .field(TestReqID, "")
        .field(CheckSum, "156")
        .toString
      "invoke the invalid message callback" in {
        expectInvalidMessage(raw, msgType, SessionRejectReason.EmptyTag, "TestReqID(112): Empty tag")
      }
    }
    "parsing an empty tag before MsgSeqNum(34)" should {
      val raw = message
        .field(BeginString, "FIX.4.2")
        .field(BodyLength, "50")
        .field(MsgType, msgType)
        .field(SenderCompID, "")
        .field(TargetCompID, "Target")
        .field(MsgSeqNum, "1")
        .field(SendingTime, "20100701-12:09:40")
        .field(TestReqID, "")
        .field(CheckSum, "053")
        .toString
      "invoke the invalid message callback" in {
        expectInvalidMessage(raw, msgType, SessionRejectReason.EmptyTag, "SenderCompID(49): Empty tag")
      }
    }
    "parsing a tag with invalid value format" should {
      val raw = message("52", msgType)
        .field(MsgSeqNum, "1")
        .field(SendingTime, "WRONG FORMAT")
        .field(TestReqID, "1")
        .field(CheckSum, "227")
        .toString
      "invoke the invalid message callback" in {
        expectInvalidMessage(raw, msgType, SessionRejectReason.InvalidValueFormat, "SendingTime(52): Invalid value format")
      }
    }
    "parsing a tag with invalid value" should {
      val msgType = "A"
      val raw = message("63", msgType)
        .field(MsgSeqNum, "1")
        .field(SendingTime, "20100701-12:09:40")
        .field(EncryptMethod, "7")
        .field(HeartBtInt, "30")
        .field(CheckSum, "249")
        .toString
      "invoke the invalid message callback" in {
        expectInvalidMessage(raw, msgType, SessionRejectReason.InvalidValue, "EncryptMethod(98): Invalid value")
      }
    }
    "parsing a tag multiple times" should {
      val raw = message("63", msgType)
        .field(MsgSeqNum, "1")
        .field(SendingTime, "20100701-12:09:40")
        .field(TestReqID, "1")
        .field(TestReqID, "1")
        .field(CheckSum, "206")
        .toString
      "invoke the invalid message callback" in {
        expectInvalidMessage(raw, msgType, SessionRejectReason.TagMultipleTimes, "TestReqID(112): Tag multiple times")
      }
    }
    "parsing an invalid message type" should {
      val msgType = "ZZ"
      val raw = message("52", msgType)
        .field(MsgSeqNum, "1")
        .field(SendingTime, "20100701-12:09:40")
        .field(CheckSum, "074")
        .toString
      "invoke the invalid message type callback" in {
        val callback = mock[Parser.Callback]
        Parser.parse(FixMessage.fromString(raw, msgType), callback)
        verify(callback).invalidMsgType(msgType, 1)
      }
    }
    "parsing an unsupported message type" should {
      val msgType = "P"
      val raw = message("51", msgType)
        .field(MsgSeqNum, "1")
        .field(SendingTime, "20100701-12:09:40")
        .field(CheckSum, "229")
        .toString
      "invoke the unsupported message type callback" in {
        val callback = mock[Parser.Callback]
        Parser.parse(FixMessage.fromString(raw, msgType), callback)
        verify(callback).unsupportedMsgType(msgType, 1)
      }
    }
    "parsing an invalid tag number" should {
      val raw = message("68", msgType)
        .field(MsgSeqNum, "1")
        .field(SendingTime, "20100701-12:09:40")
        .field(TestReqID, "1")
        .field(9898, "value")
        .field(CheckSum, "013")
        .toString
      "invoke the invalid message callback" in {
        expectInvalidMessage(raw, msgType, SessionRejectReason.InvalidTagNumber, "Invalid tag number: 9898")
      }
    }
    "parsing an invalid tag" should {
      val raw = message("62", msgType)
        .field(MsgSeqNum, "1")
        .field(SendingTime, "20100701-12:09:40")
        .field(88, "0")
        .field(TestReqID, "1")
        .field(CheckSum, "168")
        .toString
      "invoke the invalid message callback" in {
        expectInvalidMessage(raw, msgType, SessionRejectReason.InvalidTag, "Tag not defined for this message: 88")
      }
    }
    "parsing a header field within body" should {
      val raw = message("60", msgType)
        .field(MsgSeqNum, "1")
        .field(TestReqID, "1000")
        .field(SendingTime, "20100701-12:09:40")
        .field(CheckSum, "088")
        .toString
      "invoke the invalid message callback" in {
        expectInvalidMessage(raw, msgType, SessionRejectReason.OutOfOrderTag, "SendingTime(52): Out of order tag")
      }
    }
    "parsing a trailer field within body" should {
      val raw = message("64", msgType)
        .field(MsgSeqNum, "1")
        .field(SendingTime, "20100701-12:09:40")
        .field(CheckSum, "206")
        .field(TestReqID, "1")
        .field(CheckSum, "003")
        .toString
      "invoke the invalid message callback" in {
        expectInvalidMessage(raw, msgType, SessionRejectReason.OutOfOrderTag, "CheckSum(10): Out of order tag")
      }
    }
    "parsing a SOH in a value" should {
      val raw = message("61", msgType)
        .field(MsgSeqNum, "1")
        .field(SendingTime, "20100701-12:09:40")
        .field(TestReqID, "1" + Field.DELIMITER + "000")
        .field(CheckSum, "090")
        .toString
      "invoke the invalid message callback" in {
        expectInvalidMessage(raw, msgType, SessionRejectReason.FieldDelimiterInValue, "Non-data value includes field delimiter (SOH character)")
      }
    }
    "parsing a message with an optional repeating group present" should {
      val msgType = "J"
      val raw = message("210", msgType)
        .field(MsgSeqNum, "1")
        .field(43, "Y")
        .field(SendingTime, "20100701-12:09:40")
        .field(122, "20100701-12:09:40")
        .field(AllocID, "12807331319411")
        .field(AllocTransType, "0")
        .field(NoOrders, "1")
        .field(ClOrdID, "12807331319412")
        .field(Side, "2")
        .field(Symbol, "GOOG")
        .field(Shares, "1000.00")
        .field(AvgPx, "370.00")
        .field(TradeDate, "20011004")
        .field(NoAllocs, "2")
        .field(AllocAccount, "1234")
        .field(AllocShares, "900.00")
        .field(AllocAccount, "2345")
        .field(AllocShares, "100.00")
        .field(CheckSum, "155")
        .toString
      "invoke the callback" in {
        val callback = mock[Parser.Callback]
        Parser.parse(FixMessage.fromString(raw, msgType), callback)
        verify(callback).message(argThat(matches(raw)))
      }
    }
    "parsing a message with an optional repeating group absent" should {
      val msgType = "J"
      val raw = message("169", msgType)
        .field(MsgSeqNum, "1")
        .field(43, "Y")
        .field(SendingTime, "20100701-12:09:40")
        .field(122, "20100701-12:09:40")
        .field(AllocID, "12807331319411")
        .field(AllocTransType, "0")
        .field(NoOrders, "1")
        .field(ClOrdID, "12807331319412")
        .field(Side, "2")
        .field(Symbol, "GOOG")
        .field(Shares, "1000.00")
        .field(AvgPx, "370.00")
        .field(TradeDate, "20011004")
        .field(CheckSum, "067")
        .toString
      "invoke the callback" in {
        val callback = mock[Parser.Callback]
        Parser.parse(FixMessage.fromString(raw, msgType), callback)
        verify(callback).message(argThat(matches(raw)))
      }
    }
    "parsing a message with a repeating group out of order" should {
      val msgType = "J"
      val raw = message("183", msgType)
        .field(MsgSeqNum, "1")
        .field(SendingTime, "20100701-12:09:40")
        .field(AllocID, "12807331319411")
        .field(AllocTransType, "0")
        .field(NoOrders, "1")
        .field(ClOrdID, "12807331319412")
        .field(Side, "2")
        .field(Symbol, "GOOG")
        .field(Shares, "1000.00")
        .field(AvgPx, "370.00")
        .field(TradeDate, "20011004")
        .field(NoAllocs, "2")
        .field(AllocShares, "900.00")
        .field(AllocAccount, "1234")
        .field(AllocShares, "100.00")
        .field(AllocAccount, "2345")
        .field(CheckSum, "119")
        .toString
      "invoke the invalid message callback" in {
        expectInvalidMessage(raw, msgType, SessionRejectReason.OutOfOrderGroupField, "AllocShares(80): Repeating group fields out of order")
      }
    }
    "parsing a message with too few instances within a repeating group" should {
      val msgType = "J"
      val raw = message("183", msgType)
        .field(MsgSeqNum, "1")
        .field(SendingTime, "20100701-12:09:40")
        .field(AllocID, "12807331319411")
        .field(AllocTransType, "0")
        .field(NoOrders, "1")
        .field(ClOrdID, "12807331319412")
        .field(Side, "2")
        .field(Symbol, "GOOG")
        .field(Shares, "1000.00")
        .field(AvgPx, "370.00")
        .field(TradeDate, "20011004")
        .field(NoAllocs, "3")
        .field(AllocAccount, "1234")
        .field(AllocShares, "900.00")
        .field(AllocAccount, "2345")
        .field(AllocShares, "100.00")
        .field(CheckSum, "120")
        .toString
      "invoke the invalid message callback" in {
        expectInvalidMessage(raw, msgType, SessionRejectReason.NumInGroupMismatch, "NoAllocs(78): Incorrect NumInGroup count for repeating group. Expected: 3, but was: 2")
      }
    }
    "parsing a message with too many instances within a repeating group" should {
      val msgType = "J"
      val raw = message("183", msgType)
        .field(MsgSeqNum, "1")
        .field(SendingTime, "20100701-12:09:40")
        .field(AllocID, "12807331319411")
        .field(AllocTransType, "0")
        .field(NoOrders, "1")
        .field(ClOrdID, "12807331319412")
        .field(Side, "2")
        .field(Symbol, "GOOG")
        .field(Shares, "1000.00")
        .field(AvgPx, "370.00")
        .field(TradeDate, "20011004")
        .field(NoAllocs, "1")
        .field(AllocAccount, "1234")
        .field(AllocShares, "900.00")
        .field(AllocAccount, "2345")
        .field(AllocShares, "100.00")
        .field(CheckSum, "118")
        .toString
      "invoke the invalid message callback" in {
        expectInvalidMessage(raw, msgType, SessionRejectReason.NumInGroupMismatch, "NoAllocs(78): Incorrect NumInGroup count for repeating group. Expected: 1, but was: 2")
      }
    }
  }
  def expectInvalidMessage(raw: String, msgType: String, reason: Value[Integer], text: String) {
    val callback = mock[Parser.Callback]
    Parser.parse(FixMessage.fromString(raw, msgType), callback)
    verify(callback).invalidMessage(1, reason, text)
  }
}

trait ParserFixtures extends Tags {
  def matches(raw: String) = new BaseMatcher[Message]() {
    def describeTo(description: Description) {
      description.appendValue(raw)
    }
    def matches(item: AnyRef) = {
      raw == item.asInstanceOf[Message].format
    }
  }
  def message: RawMessageBuilder = new RawMessageBuilder
  def message(bodyLength: String, msgType: String): RawMessageBuilder = {
    message
      .field(BeginString, "FIX.4.2")
      .field(BodyLength, bodyLength)
      .field(MsgType, msgType)
      .field(SenderCompID, "Sender")
      .field(TargetCompID, "Target")
  }
}

trait Tags extends HeaderAndTrailerTags with HeartbeatMessageTags with AllocationMessageTags

trait HeaderAndTrailerTags {
  def BeginString = 8
  def BodyLength = 9
  def CheckSum = 10
  def EncryptMethod = 98
  def HeartBtInt = 108
  def MsgSeqNum = 34
  def MsgType = 35
  def SenderCompID = 49
  def SendingTime = 52
  def TargetCompID = 56
}

trait HeartbeatMessageTags {
  def TestReqID = 112
}

trait AllocationMessageTags  {
  def AllocAccount = 79
  def AllocID = 70
  def AllocShares = 80
  def AllocTransType = 71
  def AvgPx = 6
  def ClOrdID = 11
  def NoAllocs = 78
  def NoOrders = 73
  def Shares = 53
  def Side = 54
  def Symbol = 55
  def TradeDate = 75
}
