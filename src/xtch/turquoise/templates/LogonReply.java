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
package xtch.turquoise.templates;

import java.nio.ByteBuffer;

import xtch.templates.MessageTemplate;
import xtch.turquoise.Fields;
import xtch.turquoise.MessageType;
import xtch.turquoise.messages.TurquoiseMessage;

/** 
 * Template for LogonReply message as specified in section 7.3.2 of [2].
 */
public class LogonReply extends AbstractTemplate {
  public static final LogonReply TEMPLATE = new LogonReply();

  public LogonReply() {
    super(MessageType.LOGON_REPLY);
    add(MessageHeader.TEMPLATE);
    add(Fields.REJECT_CODE);
    add(Fields.PASSWORD_EXPIRY_DAY_COUNT);
  }

  @Override public void encode(ByteBuffer buffer, TurquoiseMessage message) {
    super.encode(buffer, message);
    buffer.put((byte) '\n');
  }

  @Override protected TurquoiseMessage newFieldContainer() { 
    return new TurquoiseMessage(this);
  }
}
