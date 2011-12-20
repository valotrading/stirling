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
package stirling.fix.messages;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import silvertip.GarbledMessageException;
import stirling.fix.messages.FixMessage;
import silvertip.MessageParser;
import silvertip.PartialMessageException;

public class FixMessageParser implements MessageParser<FixMessage> {
  public static final char DELIMITER = '\001';

  @Override final public FixMessage parse(ByteBuffer buffer) throws GarbledMessageException, PartialMessageException {
    try {
      FixMessageHeader header = header(buffer);
      String msgType = parseField(buffer, Tag.MSG_TYPE);
      int trailerLength = trailer(buffer, header);
      byte[] message = new byte[header.getHeaderLength() + header.getBodyLength() + trailerLength];
      buffer.reset();
      buffer.get(message);
      return new FixMessage(message, msgType);
    } catch (GarbledMessageException e) {
      int nextMessagePosition = nextMessagePosition(buffer);
      buffer.reset();
      byte[] data = new byte[nextMessagePosition - buffer.position()];
      buffer.get(data);
      e.setMessageData(data);
      throw e;
    } catch (BufferUnderflowException e) {
      throw new PartialMessageException();
    }
  }

  private FixMessageHeader header(ByteBuffer buffer) throws GarbledMessageException {
    int start = buffer.position();
    String beginString = parseField(buffer, Tag.BEGIN_STRING);
    if (!isBeginStringValid(beginString))
      throw new GarbledMessageException(Tag.BEGIN_STRING + " is invalid, expected format FIX.m.n: " + beginString);
    int bodyLength = bodyLength(buffer);
    return new FixMessageHeader(buffer.position() - start, bodyLength, buffer.position());
  }

  private int bodyLength(ByteBuffer buffer) throws GarbledMessageException {
    String bodyLength = parseField(buffer, Tag.BODY_LENGTH);
    try {
      return Integer.parseInt(bodyLength);
    } catch (NumberFormatException e) {
      throw new GarbledMessageException(Tag.BODY_LENGTH + " has invalid format: " + bodyLength);
    }
  }

  private boolean isBeginStringValid(String beginString) {
    return beginString.startsWith("FIX.") && Character.isDigit(beginString.charAt(4)) &&
      Character.isDigit(beginString.charAt(6));
  }

  private int trailer(ByteBuffer buffer, FixMessageHeader header) throws GarbledMessageException, PartialMessageException {
    int trailerStart = header.getBodyStart() + header.getBodyLength();
    if (trailerStart > buffer.limit()) {
      throw new PartialMessageException();
    }
    buffer.reset();
    int expectedCheckSum = checksum(buffer, header.getHeaderLength() + header.getBodyLength());
    buffer.position(trailerStart);
    try {
      int checksum = parseChecksum(buffer);
      if (expectedCheckSum != checksum)
        throw new GarbledMessageException(Tag.CHECKSUM + " mismatch, expected=" + expectedCheckSum + ", actual=" + checksum);
    } catch (GarbledMessageException e) {
      buffer.position(header.getBodyStart());
      throw e;
    }
    return buffer.position() - trailerStart;
  }

  private int parseChecksum(ByteBuffer buffer) throws GarbledMessageException {
    String checksum = parseField(buffer, Tag.CHECKSUM);
    if (checksum.length() != 3)
      throw new GarbledMessageException(Tag.CHECKSUM + " has invalid length: " + checksum);
    try {
      return Integer.parseInt(checksum);
    } catch (NumberFormatException e) {
      throw new GarbledMessageException(Tag.CHECKSUM + " has invalid format: " + checksum);
    }
  }

  private String parseField(ByteBuffer buffer, Tag tag) throws GarbledMessageException {
    match(buffer, tag);
    return value(buffer, tag);
  }

  private void match(ByteBuffer buffer, Tag tag) throws GarbledMessageException {
    String expected = tag.number() + "=";
    int start = buffer.position();
    for (int i = 0; i < expected.length(); i++) {
      int c = buffer.get();
      if (c != expected.charAt(i)) {
        /* BeginString(8) is always expected to be the first field of the
         * message, and thus, is not preceded by a SOH. */
        if (tag != Tag.BEGIN_STRING)
          buffer.position(start - 1);
        throw new GarbledMessageException("Expected tag not found: " + tag);
      }
    }
  }

  private String value(ByteBuffer buffer, Tag tag) throws GarbledMessageException {
    StringBuilder result = new StringBuilder();
    for (;;) {
      byte ch = buffer.get();
      if (ch == DELIMITER)
        break;
      result.append((char) ch);
    }
    if (result.length() == 0)
      throw new GarbledMessageException(tag + " is empty");
    return result.toString();
  }

  private int nextMessagePosition(ByteBuffer buffer) {
    /* Store the original position to which the buffer is finally rewound. */
    int start = buffer.position();
    try {
      while (buffer.hasRemaining()) {
        if (skipToBeginString(buffer))
          return buffer.position();
      }
      return buffer.position();
    } catch (BufferUnderflowException e) {
      return buffer.position();
    } finally {
      buffer.position(start);
    }
  }

  private boolean skipToBeginString(ByteBuffer buffer) {
    String beginString = DELIMITER + "8=";
    for (int i = 0; i < beginString.length(); i++) {
      if (buffer.get() != beginString.charAt(i)) {
        /* Rewind to the previous position when SOH was found but the
         * subsequent character does not match with the expected character.
         * Rewinding to the previous position is needed for making sure that no
         * part of the character sequence that would otherwise match is
         * consumed. */
        if (i > 0)
          buffer.position(buffer.position() - 1);
        return false;
      }
    }
    /* Set the buffer position to the beginning of BeginString(8) */
    buffer.position(buffer.position() - beginString.length() + 1);
    return true;
  }

  private int checksum(ByteBuffer b, int end) {
    int checksum = 0;
    for (int i = 0; i < end; i++) {
      checksum += b.get();
    }
    return checksum % 256;
  }

  private static class FixMessageHeader {
    private final int headerLength;
    private final int bodyLength;
    private final int bodyStart;

    public FixMessageHeader(int headerLength, int bodyLength, int bodyStart) {
      this.headerLength = headerLength;
      this.bodyLength = bodyLength;
      this.bodyStart = bodyStart;
    }

    public int getHeaderLength() {
      return headerLength;
    }

    public int getBodyLength() {
      return bodyLength;
    }

    public int getBodyStart() {
      return bodyStart;
    }
  }

  private enum Tag {
    BEGIN_STRING(8, "BeginString"),
    BODY_LENGTH(9, "BodyLength"),
    MSG_TYPE(35, "MsgType"),
    CHECKSUM(10, "CheckSum");

    private int tagNumber;
    private String name;

    private Tag(int tagNumber, String name) {
      this.tagNumber = tagNumber;
      this.name = name;
    }

    public int number() {
      return tagNumber;
    }

    public String toString() {
      return String.format("%s(%d)", name, tagNumber);
    }
  }
}
