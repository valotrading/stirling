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

import org.junit.runner.RunWith;

@RunWith(JDaveRunner.class)
public class ParserSpec extends Specification<String> {
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
            Message m = Parser.parse(silvertip.Message.fromString(raw));
            specify(m.format(), must.equal(raw));
        }
    }

    public class EmptyTag {
        public String create() {
            return raw = message("57", "0")
                .field(MsgSeqNum, "1")
                .field(SendingTime, "20100701-12:09:40")
                .field(TestReqID, "")
                .field(CheckSum, "206")
                .toString();
        }

        public void parse() {
            specify(new Block() {
                @Override public void run() throws Throwable {
                    Parser.parse(silvertip.Message.fromString(raw));
                }
            }, must.raise(EmptyTagException.class, "TestReqId(112): Empty tag"));
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
            specify(new Block() {
                @Override public void run() throws Throwable {
                    Parser.parse(silvertip.Message.fromString(raw));
                }
            }, must.raise(InvalidValueFormatException.class, "SendingTime(52): Invalid value format"));
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
            specify(new Block() {
                @Override public void run() throws Throwable {
                    Parser.parse(silvertip.Message.fromString(raw));
                }
            }, must.raise(InvalidValueException.class, "EncryptMethod(98): Invalid value"));
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
            specify(new Block() {
                @Override public void run() throws Throwable {
                    Parser.parse(silvertip.Message.fromString(raw));
                }
            }, must.raise(TagMultipleTimesException.class, "TestReqId(112): Tag multiple times"));
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
            specify(new Block() {
                @Override public void run() throws Throwable {
                    Parser.parse(silvertip.Message.fromString(raw));
                }
            }, must.raise(InvalidCheckSumException.class, "Invalid checksum: Expected: 206, but was: 999"));
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
            specify(new Block() {
                @Override public void run() throws Throwable {
                    Parser.parse(silvertip.Message.fromString(raw));
                }
            }, must.raise(BeginStringMissingException.class, "BeginString(8): is missing"));
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
                    .field(CheckSum, "33")
                    .toString();
        }

        public void parse() {
            specify(new Block() {
                @Override public void run() throws Throwable {
                    Parser.parse(silvertip.Message.fromString(raw));
                }
            }, must.raise(InvalidBeginStringException.class, "BeginString(8): 'FIX.FIX' is not supported"));
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
            specify(new Block() {
                @Override public void run() throws Throwable {
                    Parser.parse(silvertip.Message.fromString(raw));
                }
            }, must.raise(BodyLengthMissingException.class, "BodyLength(9): is missing"));
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
            specify(new Block() {
                @Override public void run() throws Throwable {
                    Parser.parse(silvertip.Message.fromString(raw));
                }
            }, must.raise(InvalidBodyLengthException.class));
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
            specify(new Block() {
                @Override public void run() throws Throwable {
                    Parser.parse(silvertip.Message.fromString(raw));
                }
            }, must.raise(MsgTypeMissingException.class, "MsgType(35): is missing"));
        }
    }

    public class InvalidMsgType {
        public String create() {
            return raw = message("57", "XX")
                .field(MsgSeqNum, "1")
                .field(SendingTime, "20100701-12:09:40")
                .field(CheckSum, "206")
                .toString();
        }

        public void parse() {
            specify(Parser.parse(silvertip.Message.fromString(raw)) instanceof UnknownMessage);
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
            specify(new Block() {
                @Override public void run() throws Throwable {
                    Parser.parse(silvertip.Message.fromString(raw));
                }
            }, must.raise(InvalidTagNumberException.class, "Invalid tag number: 9898"));
        }
    }

    public class InvalidTag {
        public String create() {
            return raw = message("57", "0")
                .field(MsgSeqNum, "1")
                .field(SendingTime, "20100701-12:09:40")
                .field(TestReqID, "1")
                .field(88, "0")
                .field(CheckSum, "206")
                .toString();
        }

        public void parse() {
            specify(new Block() {
                @Override public void run() throws Throwable {
                    Parser.parse(silvertip.Message.fromString(raw));
                }
            }, must.raise(InvalidTagException.class, "Tag not defined for this message: 88"));
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
            specify(new Block() {
                @Override public void run() throws Throwable {
                    Parser.parse(silvertip.Message.fromString(raw));
                }
            }, must.raise(NonDataValueIncludesFieldDelimiterException.class, "TestReqId(112): Non-data value includes field delimiter (SOH character)"));
        }
    }

    public static FixMessageBuilder message() {
        return new FixMessageBuilder();
    }

    public static FixMessageBuilder message(String bodyLength, String msgType) {
        return message()
                .field(BeginString, "FIX.4.2")
                .field(BodyLength, bodyLength)
                .field(MsgType, msgType)
                .field(SenderCompID, "Sender")
                .field(TargetCompID, "Target");
    }

    public static class FixMessageBuilder {
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
