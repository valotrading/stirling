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
package xtch.templates;

import java.nio.ByteBuffer;

import xtch.elements.Field;
import xtch.messages.Message;

public abstract class MessageTemplate<T extends Message> extends Template<T> {
  private final String packetType;

  public MessageTemplate(String packetType) {
    this.packetType = packetType;
  }

  public final String getPacketType() {
    return packetType;
  }
}
