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

    // TODO:

    // - Header field in body
    // - Too few entries in repeating group
    // - Too many entries in repeating group
    // - Missing required field

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
                .field(CheckSum, "53")
                .toString();
        }

        public void parse() {
            expectInvalidMessage(SessionRejectReasonValue.EMPTY_TAG, "SenderCompID(49): Empty tag");
        }
    }

    public class InvalidValueFormat {
        public String create() {
            return raw = message("57", "0")
                .field(MsgSeqNum, "1")
                .field(SendingTime, "WRONG FORMAT")
                .field(TestReqID, "1")
                .field(CheckSum, "206")
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
                .field(CheckSum, "248")
                .toString();
        }

        public void parse() {
            expectInvalidMessage(SessionRejectReasonValue.INVALID_VALUE, "EncryptMethod(98): Invalid value");
        }
    }

    public class TagMultipleTimes {
        public String create() {
            return raw = message("57", "0")
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

    public class InvalidCheckSum {
        public String create() {
            return raw = message("57", "0")
                .field(MsgSeqNum, "1")
                .field(SendingTime, "20100701-12:09:40")
                .field(TestReqID, "1")
                .field(CheckSum, "999")
                .toString();
        }

        public void parse() {
            expectGarbledMessage("CheckSum(10): Expected: 206, but was: 999");
        }
    }

    public class BeginStringMissing {
        public String create() {
            return raw = message()
                    .field(BodyLength, "57")
                    .field(MsgType, "0")
                    .field(SenderCompID, "Sender")
                    .field(TargetCompID, "Target")
                    .field(MsgSeqNum, "1")
                    .field(SendingTime, "20100701-12:09:40")
                    .field(TestReqID, "1")
                    .field(CheckSum, "33")
                    .toString();
        }

        public void parse() {
            expectGarbledMessage("BeginString(8): is missing");
        }
    }

    public class InvalidBeginString {
        public String create() {
            return raw = message()
                    .field(BeginString, "FIX.FIX")
                    .field(BodyLength, "57")
                    .field(MsgType, "0")
                    .field(SenderCompID, "Sender")
                    .field(TargetCompID, "Target")
                    .field(MsgSeqNum, "1")
                    .field(SendingTime, "20100701-12:09:40")
                    .field(TestReqID, "1")
                    .field(CheckSum, "033")
                    .toString();
        }

        public void parse() {
            checking(new Expectations() {{
                one(callback).message(with(new MessageMatcher(raw)));
            }});
            Parser.parse(silvertip.Message.fromString(raw), callback);
        }
    }

    public class EmptyBeginString {
        public String create() {
            return raw = message()
                    .field(BeginString, "")
                    .field(BodyLength, "57")
                    .field(MsgType, "0")
                    .field(SenderCompID, "Sender")
                    .field(TargetCompID, "Target")
                    .field(MsgSeqNum, "1")
                    .field(SendingTime, "20100701-12:09:40")
                    .field(TestReqID, "1")
                    .field(CheckSum, "037")
                    .toString();
        }

        public void parse() {
            expectGarbledMessage("BeginString(8): Empty tag");
        }
    }

    public class BodyLengthMissing {
        public String create() {
            return raw = message()
                    .field(BeginString, "FIX.4.2")
                    .field(MsgType, "0")
                    .field(SenderCompID, "Sender")
                    .field(TargetCompID, "Target")
                    .field(MsgSeqNum, "1")
                    .field(SendingTime, "20100701-12:09:40")
                    .field(TestReqID, "1")
                    .field(CheckSum, "33")
                    .toString();
        }

        public void parse() {
            expectGarbledMessage("BodyLength(9): is missing");
        }
    }

    public class EmptyBodyLength {
        public String create() {
            return raw = message("", "0")
                .field(MsgSeqNum, "1")
                .field(SendingTime, "20100701-12:09:40")
                .field(TestReqID, "1")
                .field(CheckSum, "206")
                .toString();
        }

        public void parse() {
            expectGarbledMessage("BodyLength(9): Empty tag");
        }
    }

    public class InvalidFormatBodyLength {
        public String create() {
            return raw = message("XX", "0")
                .field(MsgSeqNum, "1")
                .field(SendingTime, "20100701-12:09:40")
                .field(TestReqID, "1")
                .field(CheckSum, "206")
                .toString();
        }

        public void parse() {
            expectGarbledMessage("BodyLength(9): Invalid value format");
        }
    }

    public class InvalidBodyLength {
        public String create() {
            return raw = message("75", "0")
                .field(MsgSeqNum, "1")
                .field(SendingTime, "20100701-12:09:40")
                .field(TestReqID, "1")
                .field(CheckSum, "206")
                .toString();
        }

        public void parse() {
            expectGarbledMessage("BodyLength(9): Expected: 75, but was: 57");
        }
    }

    public class MsgTypeMissing {
        public String create() {
            return raw = message()
                    .field(BeginString, "FIX.4.2")
                    .field(BodyLength, "57")
                    .field(SenderCompID, "Sender")
                    .field(TargetCompID, "Target")
                    .field(MsgSeqNum, "1")
                    .field(SendingTime, "20100701-12:09:40")
                    .field(TestReqID, "1")
                    .field(CheckSum, "33")
                    .toString();
        }

        public void parse() {
            expectGarbledMessage("MsgType(35): is missing");
        }
    }

    public class InvalidMsgType {
        public String create() {
            return raw = message("57", "ZZ")
                .field(MsgSeqNum, "1")
                .field(SendingTime, "20100701-12:09:40")
                .field(CheckSum, "206")
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
            return raw = message("57", "P")
                .field(MsgSeqNum, "1")
                .field(SendingTime, "20100701-12:09:40")
                .field(CheckSum, "206")
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
            return raw = message("57", "0")
                .field(MsgSeqNum, "1")
                .field(SendingTime, "20100701-12:09:40")
                .field(TestReqID, "1")
                .field(9898, "value")
                .field(CheckSum, "206")
                .toString();
        }

        public void parse() {
            expectInvalidMessage(SessionRejectReasonValue.INVALID_TAG_NUMBER, "Invalid tag number: 9898");
        }
    }

    public class InvalidTag {
        public String create() {
            return raw = message("57", "0")
                .field(MsgSeqNum, "1")
                .field(SendingTime, "20100701-12:09:40")
                .field(88, "0")
                .field(TestReqID, "1")
                .field(CheckSum, "206")
                .toString();
        }

        public void parse() {
            expectInvalidMessage(SessionRejectReasonValue.INVALID_TAG, "Tag not defined for this message: 88");
        }
    }

    public class OutOfOrderTag {
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

    public class SOHInValue {
        public String create() {
            return raw = message("60", "0")
                .field(MsgSeqNum, "1")
                .field(SendingTime, "20100701-12:09:40")
                .field(TestReqID, "1" + Field.DELIMITER + "000")
                .field(CheckSum, "088")
                .toString();
        }

        public void parse() {
            expectInvalidMessage(SessionRejectReasonValue.FIELD_DELIMITER_IN_VALUE, "TestReqID(112): Non-data value includes field delimiter (SOH character)");
        }
    }

    static FixMessageBuilder message() {
        return new FixMessageBuilder();
    }

    static FixMessageBuilder message(String bodyLength, String msgType) {
        return message()
                .field(BeginString, "FIX.4.2")
                .field(BodyLength, bodyLength)
                .field(MsgType, msgType)
                .field(SenderCompID, "Sender")
                .field(TargetCompID, "Target");
    }

    static class FixMessageBuilder {
        StringBuilder s = new StringBuilder();

        public FixMessageBuilder field(int tag, String value) {
            s.append(tag);
            s.append('=');
            s.append(value);
            s.append(Field.DELIMITER);
            return this;
        }

        @Override public String toString() {
            return s.toString();
        }
    }

    private void expectInvalidMessage(final SessionRejectReasonValue reason, final String text) {
        checking(new Expectations() {{
            one(callback).invalidMessage(1, reason, text);
        }});
        Parser.parse(silvertip.Message.fromString(raw), callback);
    }

    private void expectGarbledMessage(final String text) {
        checking(new Expectations() {{
            one(callback).garbledMessage(text);
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

    private static final int BeginString   = 8;
    private static final int BodyLength    = 9;
    private static final int CheckSum      = 10;
    private static final int EncryptMethod = 98;
    private static final int HeartBtInt    = 108;
    private static final int MsgSeqNum     = 34;
    private static final int MsgType       = 35;
    private static final int SenderCompID  = 49;
    private static final int SendingTime   = 52;
    private static final int TargetCompID  = 56;
    private static final int TestReqID     = 112;
}
