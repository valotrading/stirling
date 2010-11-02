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
import org.joda.time.DateTime;

public class FixMessage extends silvertip.Message implements SequencedMessage<FixMessage> {
  private String msgType;
  private int msgSeqNum;
  private DateTime receiveTime;

  public static FixMessage fromString(String s, String msgType) {
    return new FixMessage(s.getBytes(), msgType);
  }

  public FixMessage(byte[] data, String msgType) {
    super(data);

    this.msgType = msgType;
  }

  public String getMsgType() {
    return msgType;
  }

  @Override public int getMsgSeqNum() {
    return msgSeqNum;
  }

  public void setMsgSeqNum(int msgSeqNum) {
    this.msgSeqNum = msgSeqNum;
  }

  public DateTime getReceiveTime() {
    return receiveTime;
  }

  public void setReceiveTime(DateTime receiveTime) {
    this.receiveTime = receiveTime;
  }

  @Override public int compareTo(FixMessage message) {
    return getMsgSeqNum() - message.getMsgSeqNum();
  }
}
