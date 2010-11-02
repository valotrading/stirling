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

import java.nio.ByteBuffer;

import org.junit.Assert;
import org.junit.Test;

import silvertip.GarbledMessageException;
import silvertip.Message;
import silvertip.PartialMessageException;

public class FixMessageParserSpec {
  private static final int RECEIVE_BUFFER_SIZE = 4096;
  private static final char DELIMITER = '\001';

  private final ByteBuffer rxBuffer = ByteBuffer.allocate(RECEIVE_BUFFER_SIZE);
  private FixMessageParser parser = new FixMessageParser();

  @Test(expected = PartialMessageException.class)
  public void empty() throws Exception {
    parse("");
  }

  @Test(expected = PartialMessageException.class)
  public void partialHeader() throws Exception {
    String partialHeader = "8=FIX.4.2" + DELIMITER + "9=153";

    parse(partialHeader);
  }

  @Test public void firstFieldIsNotBeginString() throws Exception {
    String garbled = "X=FIX.4.2" + DELIMITER + "9=5" + DELIMITER;

    assertGarbledMessage(garbled, "Expected tag not found: BeginString(8)");
  }

  @Test public void emptyBeginString() throws Exception {
    String garbled = "8=" + DELIMITER + "9=5" + DELIMITER;

    assertGarbledMessage(garbled, "BeginString(8) is empty");
  }

  @Test public void invalidBeginString() throws Exception {
    String garbled = "8=XIF.10.1" + DELIMITER + "9=5" + DELIMITER;

    assertGarbledMessage(garbled, "BeginString(8) is invalid, expected format FIX.m.n: XIF.10.1");
  }

  @Test public void secondFieldIsNotBodyLength() throws Exception {
    String garbled = "8=FIX.4.2" + DELIMITER + "X=5" + DELIMITER;

    assertGarbledMessage(garbled, "Expected tag not found: BodyLength(9)");
  }

  @Test public void secondFieldIsNotBodyLengthAndIsMissing() throws Exception {
    String garbled = "8=FIX.4.2" + DELIMITER;

    assertGarbledMessage(garbled, "Expected tag not found: BodyLength(9)");
  }

  @Test public void emptyBodyLength() throws Exception {
    String garbled = "8=FIX.4.2" + DELIMITER + "9=" + DELIMITER + "108=XXX" + DELIMITER;

    assertGarbledMessage(garbled, "BodyLength(9) is empty");
  }

  @Test public void invalidBodyLengthFormat() throws Exception {
    String garbled = "8=FIX.4.2" + DELIMITER + "9=XXX" + DELIMITER + "108=XXX" + DELIMITER;

    assertGarbledMessage(garbled, "BodyLength(9) has invalid format: XXX");
  }

  @Test public void bodyLengthDoesNotPointToStandardTrailer() throws Exception {
    String header = "8=FIX.4.2" + DELIMITER + "9=5" + DELIMITER;
    String payload = "35=E" + DELIMITER;
    String trailer = "10=XXX" + DELIMITER;
    String messages = header + payload + header + payload + trailer;
    String garbled = header + payload;

    assertGarbledMessage(messages, garbled, "Expected tag not found: CheckSum(10)");
  }

  @Test public void thirdFieldIsNotMsgType() throws Exception {
    String garbled = "8=FIX.4.2" + DELIMITER + "9=5" + DELIMITER + "X=A" + DELIMITER;

    assertGarbledMessage(garbled, "Expected tag not found: MsgType(35)");
  }

  @Test public void thirdFieldIsNotMsgTypeAndIsMissing() throws Exception {
    String garbled = "8=FIX.4.2" + DELIMITER + "9=5" + DELIMITER;

    assertGarbledMessage(garbled, "Expected tag not found: MsgType(35)");
  }

  @Test public void emptyMsgType() throws Exception {
    String garbled = "8=FIX.4.2" + DELIMITER + "9=5" + DELIMITER + "35=" + DELIMITER + "108=XXX" + DELIMITER;

    assertGarbledMessage(garbled, "MsgType(35) is empty");
  }

  @Test public void missingCheckSum() throws Exception {
    String header = "8=FIX.4.2" + DELIMITER + "9=5" + DELIMITER;
    String payload = "35=E" + DELIMITER;
    String trailer = "11=XXX" + DELIMITER;
    String message = header + payload + trailer;

    assertGarbledMessage(message, "Expected tag not found: CheckSum(10)");
  }

  @Test public void emptyCheckSum() throws Exception {
    String header = "8=FIX.4.2" + DELIMITER + "9=5" + DELIMITER;
    String payload = "35=E" + DELIMITER;
    String trailer = "10=" + DELIMITER;
    String message = header + payload + trailer;

    assertGarbledMessage(message, "CheckSum(10) is empty");
  }

  @Test public void invalidCheckSumFormat() throws Exception {
    String garbled = "8=FIX.4.2" + DELIMITER + "9=5" + DELIMITER + "35=E" + DELIMITER + "10=XYZ" + DELIMITER;

    assertGarbledMessage(garbled, "CheckSum(10) has invalid format: XYZ");
  }

  @Test public void invalidCheckSumLength() throws Exception {
    String garbled = "8=FIX.4.2" + DELIMITER + "9=5" + DELIMITER + "35=E" + DELIMITER + "10=12" + DELIMITER;

    assertGarbledMessage(garbled, "CheckSum(10) has invalid length: 12");
  }

  @Test public void invalidCheckSum() throws Exception {
    String garbled = "8=FIX.4.2" + DELIMITER + "9=5" + DELIMITER + "35=E" + DELIMITER + "10=123" + DELIMITER;

    assertGarbledMessage(garbled, "CheckSum(10) mismatch, expected=182, actual=123");
  }

  @Test public void garbledMessageEndingWith8SOH() throws Exception {
    String garbled = "8=FIX.4.2" + DELIMITER + "9=5" + DELIMITER + "8" + DELIMITER;

    assertGarbledMessage(garbled, "Expected tag not found: MsgType(35)");
  }

  @Test public void garbledMessageEndingWith8SOHSOH() throws Exception {
    String garbled = "8=FIX.4.2" + DELIMITER + "9=5" + DELIMITER + "8" + DELIMITER + DELIMITER;

    assertGarbledMessage(garbled, "Expected tag not found: MsgType(35)");
  }

  @Test(expected = PartialMessageException.class)
  public void partialMessage() throws Exception {
    String header = "8=FIX.4.2" + DELIMITER + "9=153" + DELIMITER + "";

    parse(header);
  }

  @Test
  public void fullMessage() throws Exception {
    String header = "8=FIX.4.2" + DELIMITER + "9=153" + DELIMITER + "";
    String payload = "35=E" + DELIMITER + "49=INST" + DELIMITER + "56=BROK" + DELIMITER + "52=20050908-15:51:22"
        + DELIMITER + "34=200" + DELIMITER + "66=14" + DELIMITER + "394=1" + DELIMITER + "68=2" + DELIMITER + "73=2"
        + DELIMITER + "11=order-1" + DELIMITER + "67=1" + DELIMITER + "55=IBM" + DELIMITER + "54=2" + DELIMITER
        + "38=2000" + DELIMITER + "40=1" + DELIMITER + "11=order-2" + DELIMITER + "67=2" + DELIMITER + "55=AOL"
        + DELIMITER + "54=2" + DELIMITER + "38=1000" + DELIMITER + "40=1" + DELIMITER + "";
    String trailer = "10=020" + DELIMITER;

    Message message = parse(header + payload + trailer);
    Assert.assertEquals(header + payload + trailer, message.toString());
  }

  private void assertGarbledMessage(String garbledMessage, String expectedReason) throws Exception {
    String valid = "8=FIX.4.2" + DELIMITER + "9=5" + DELIMITER + "35=E" + DELIMITER + "10=191" + DELIMITER;
    assertGarbledMessage(garbledMessage + valid, garbledMessage, expectedReason);
  }

  private void assertGarbledMessage(String messages, String expectedGarbledMessage, String expectedReason) throws Exception {
    try {
      parse(messages);
      Assert.fail("GarbledMessageException was not thrown for message: " + expectedGarbledMessage);
    } catch (GarbledMessageException e) {
      Assert.assertEquals(expectedGarbledMessage, new String(e.getMessageData()));
      Assert.assertEquals(expectedReason, e.getMessage());
    }
  }

  private Message parse(String message) throws PartialMessageException, GarbledMessageException {
    rxBuffer.put(message.getBytes());
    rxBuffer.flip();
    return parse(rxBuffer);
  }

  private Message parse(ByteBuffer buffer) throws PartialMessageException, GarbledMessageException {
    buffer.mark();
    return parser.parse(buffer);
  }
}
