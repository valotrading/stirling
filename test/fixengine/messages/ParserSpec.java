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
package fixengine.messages;

import jdave.Block;
import jdave.Specification;
import jdave.junit4.JDaveRunner;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.jmock.Expectations;
import org.junit.runner.RunWith;

@RunWith(JDaveRunner.class)
public class ParserSpec extends Specification<String> {
    private Parser.Callback callback = mock(Parser.Callback.class);
    private String raw;

    public class FullMessage {
        public String create() {
            return raw = message("57", "0")
                .field(MsgSeqNum, "1")
                .field(SendingTime, "20100701-12:09:40")
                .field(TestReqID, "1")
                .field(CheckSum, "206")
                .toString();
        }

        public void parse() {
            checking(new Expectations() {{
                one(callback).message(with(new MessageMatcher(raw)));
            }});
            Parser.parse(silvertip.Message.fromString(raw), callback);
        }

        public void parseMsgSeqNum() {
            specify(Parser.parseMsgSeqNum(silvertip.Message.fromString(raw)), 1);
        }
    }

    public class MsgSeqNumMissing {
        public String create() {
            return raw = message()
                .field(BeginString, "FIX.4.2")
                .field(BodyLength, "46")
                .field(MsgType, "0")
                .field(SenderCompID, "Sender")
                .field(TargetCompID, "Target")
                .field(SendingTime, "20100701-12:09:40")
                .field(CheckSum, "243")
                .toString();
        }

        public void parse() {
            checking(new Expectations() {{
                one(callback).msgSeqNumMissing("MsgSeqNum(34) is missing");
            }});
            Parser.parse(silvertip.Message.fromString(raw), callback);
        }

        public void parseMsgSeqNum() {
            specify(new Block() {
                @Override public void run() {
                    Parser.parseMsgSeqNum(silvertip.Message.fromString(raw));
                }
            }, must.raise(ParseException.class, "MsgSeqNum(34) is missing"));
        }
    }

    public class MsgSeqNumHasInvalidFormat {
        public String create() {
            return raw = message("57", "0")
                .field(MsgSeqNum, "X")
                .field(SendingTime, "20100701-12:09:40")
                .field(TestReqID, "1")
                .field(CheckSum, "206")
                .toString();
        }

        public void parseMsgSeqNum() {
            specify(new Block() {
                @Override public void run() {
                    Parser.parseMsgSeqNum(silvertip.Message.fromString(raw));
                }
            }, must.raise(InvalidValueFormatException.class, "MsgSeqNum(34) has invalid value format: X"));
        }
    }

    public class OptionalFieldMissing {
        public String create() {
            return raw = message("51", "0")
                .field(MsgSeqNum, "1")
                .field(SendingTime, "20100701-12:09:40")
                .field(CheckSum, "197")
                .toString();
        }

        public void parse() {
            checking(new Expectations() {{
                one(callback).message(with(new MessageMatcher(raw)));
            }});
            Parser.parse(silvertip.Message.fromString(raw), callback);
        }
    }

    public class EmptyTag {
        public String create() {
            return raw = message("56", "0")
                .field(MsgSeqNum, "1")
                .field(SendingTime, "20100701-12:09:40")
                .field(TestReqID, "")
                .field(CheckSum, "156")
                .toString();
        }

        public void parse() {
            expectInvalidMessage(SessionRejectReasonValue.EMPTY_TAG, "TestReqID(112): Empty tag");
        }
    }

    public class EmptyTagBeforeMsgSeqNum {
        public String create() {
            return raw = message()
                .field(BeginString, "FIX.4.2")
                .field(BodyLength, "50")
                .field(MsgType, "0")
                .field(SenderCompID, "")
                .field(TargetCompID, "Target")
                .field(MsgSeqNum, "1")
                .field(SendingTime, "20100701-12:09:40")
                .field(TestReqID, "")
                .field(CheckSum, "053")
                .toString();
        }

        public void parse() {
            expectInvalidMessage(SessionRejectReasonValue.EMPTY_TAG, "SenderCompID(49): Empty tag");
        }
    }

    public class InvalidValueFormat {
        public String create() {
            return raw = message("52", "0")
                .field(MsgSeqNum, "1")
                .field(SendingTime, "WRONG FORMAT")
                .field(TestReqID, "1")
                .field(CheckSum, "227")
                .toString();
        }

        public void parse() {
            expectInvalidMessage(SessionRejectReasonValue.INVALID_VALUE_FORMAT, "SendingTime(52): Invalid value format");
        }
    }

    public class InvalidValue {
        public String create() {
            return raw = message("63", "A")
                .field(MsgSeqNum, "1")
                .field(SendingTime, "20100701-12:09:40")
                .field(EncryptMethod, "7")
                .field(HeartBtInt, "30")
                .field(CheckSum, "249")
                .toString();
        }

        public void parse() {
            expectInvalidMessage(SessionRejectReasonValue.INVALID_VALUE, "EncryptMethod(98): Invalid value");
        }
    }

    public class TagMultipleTimes {
        public String create() {
            return raw = message("63", "0")
                .field(MsgSeqNum, "1")
                .field(SendingTime, "20100701-12:09:40")
                .field(TestReqID, "1")
                .field(TestReqID, "1")
                .field(CheckSum, "206")
                .toString();
        }

        public void parse() {
            expectInvalidMessage(SessionRejectReasonValue.TAG_MULTIPLE_TIMES, "TestReqID(112): Tag multiple times");
        }
    }

    public class InvalidMsgType {
        public String create() {
            return raw = message("52", "ZZ")
                .field(MsgSeqNum, "1")
                .field(SendingTime, "20100701-12:09:40")
                .field(CheckSum, "074")
                .toString();
        }

        public void parse() {
            checking(new Expectations() {{
                one(callback).invalidMsgType("ZZ", 1);
            }});
            Parser.parse(silvertip.Message.fromString(raw), callback);
        }
    }

    public class UnsupportedMsgType {
        public String create() {
            return raw = message("51", "P")
                .field(MsgSeqNum, "1")
                .field(SendingTime, "20100701-12:09:40")
                .field(CheckSum, "229")
                .toString();
        }

        public void parse() {
            checking(new Expectations() {{
                one(callback).unsupportedMsgType("P", 1);
            }});
            Parser.parse(silvertip.Message.fromString(raw), callback);
        }
    }

    public class InvalidTagNumber {
        public String create() {
            return raw = message("68", "0")
                .field(MsgSeqNum, "1")
                .field(SendingTime, "20100701-12:09:40")
                .field(TestReqID, "1")
                .field(9898, "value")
                .field(CheckSum, "013")
                .toString();
        }

        public void parse() {
            expectInvalidMessage(SessionRejectReasonValue.INVALID_TAG_NUMBER, "Invalid tag number: 9898");
        }
    }

    public class InvalidTag {
        public String create() {
            return raw = message("62", "0")
                .field(MsgSeqNum, "1")
                .field(SendingTime, "20100701-12:09:40")
                .field(88, "0")
                .field(TestReqID, "1")
                .field(CheckSum, "168")
                .toString();
        }

        public void parse() {
            expectInvalidMessage(SessionRejectReasonValue.INVALID_TAG, "Tag not defined for this message: 88");
        }
    }

    public class HeaderFieldInBody {
        public String create() {
            return raw = message("60", "0")
                .field(MsgSeqNum, "1")
                .field(TestReqID, "1000")
                .field(SendingTime, "20100701-12:09:40")
                .field(CheckSum, "088")
                .toString();
        }

        public void parse() {
            expectInvalidMessage(SessionRejectReasonValue.OUT_OF_ORDER_TAG, "SendingTime(52): Out of order tag");
        }
    }

    public class TrailerFieldInBody {
        public String create() {
            return raw = message("64", "0")
                .field(MsgSeqNum, "1")
                .field(SendingTime, "20100701-12:09:40")
                .field(CheckSum, "206")
                .field(TestReqID, "1")
                .field(CheckSum, "003")
                .toString();
        }

        public void parse() {
            expectInvalidMessage(SessionRejectReasonValue.OUT_OF_ORDER_TAG, "CheckSum(10): Out of order tag");
        }
    }

    public class SOHInValue {
        public String create() {
            return raw = message("61", "0")
                .field(MsgSeqNum, "1")
                .field(SendingTime, "20100701-12:09:40")
                .field(TestReqID, "1" + Field.DELIMITER + "000")
                .field(CheckSum, "090")
                .toString();
        }

        public void parse() {
            expectInvalidMessage(SessionRejectReasonValue.FIELD_DELIMITER_IN_VALUE, "Non-data value includes field delimiter (SOH character)");
        }
    }

    public class RepeatingGroup {
        public String create() {
            return raw = message("210", "J")
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
                .toString();
        }

        public void parse() {
            checking(new Expectations() {{
                one(callback).message(with(new MessageMatcher(raw)));
            }});
            Parser.parse(silvertip.Message.fromString(raw), callback);
        }
    }

    public class RepeatingGroupOutOfOrder {
        public String create() {
            return raw = message("183", "J")
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
                .toString();
        }

        public void parse() {
            expectInvalidMessage(SessionRejectReasonValue.OUT_OF_ORDER_GROUP_FIELD, "AllocShares(80): Repeating group fields out of order");
        }
    }

    public class TooFewInstancesInRepeatingGroup {
        public String create() {
            return raw = message("183", "J")
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
                .toString();
        }

        public void parse() {
            expectInvalidMessage(SessionRejectReasonValue.NUM_IN_GROUP_MISMATCH, "NoAllocs(78): Incorrect NumInGroup count for repeating group. Expected: 3, but was: 2");
        }
    }

    public class TooManyInstancesInRepeatingGroup {
        public String create() {
            return raw = message("183", "J")
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
                .toString();
        }

        public void parse() {
            expectInvalidMessage(SessionRejectReasonValue.NUM_IN_GROUP_MISMATCH, "NoAllocs(78): Incorrect NumInGroup count for repeating group. Expected: 1, but was: 2");
        }
    }

    static RawMessageBuilder message() {
        return new RawMessageBuilder();
    }

    static RawMessageBuilder message(String bodyLength, String msgType) {
        return message()
                .field(BeginString, "FIX.4.2")
                .field(BodyLength, bodyLength)
                .field(MsgType, msgType)
                .field(SenderCompID, "Sender")
                .field(TargetCompID, "Target");
    }

    private void expectInvalidMessage(final SessionRejectReasonValue reason, final String text) {
        checking(new Expectations() {{
            one(callback).invalidMessage(1, reason, text);
        }});
        Parser.parse(silvertip.Message.fromString(raw), callback);
    }

    class MessageMatcher extends BaseMatcher<Message> {
      private final String raw;

      public MessageMatcher(String raw) {
        this.raw = raw;
      }

      @Override public boolean matches(Object item) {
        Message m = (Message) item;
        return raw.equals(m.format());
      }

      @Override public void describeTo(Description description) {
        description.appendValue(raw);
      }
    }

    // Header and trailer tags
    private static final int BeginString    = 8;
    private static final int BodyLength     = 9;
    private static final int CheckSum       = 10;
    private static final int EncryptMethod  = 98;
    private static final int HeartBtInt     = 108;
    private static final int MsgSeqNum      = 34;
    private static final int MsgType        = 35;
    private static final int SenderCompID   = 49;
    private static final int SendingTime    = 52;
    private static final int TargetCompID   = 56;

    // Heartbeat message
    private static final int TestReqID      = 112;

    // Allocation message
    private static final int AllocAccount   = 79;
    private static final int AllocID        = 70;
    private static final int AllocShares    = 80;
    private static final int AllocTransType = 71;
    private static final int AvgPx          = 6;
    private static final int ClOrdID        = 11;
    private static final int NoAllocs       = 78;
    private static final int NoOrders       = 73;
    private static final int Shares         = 53;
    private static final int Side           = 54;
    private static final int Symbol         = 55;
    private static final int TradeDate      = 75;
}
