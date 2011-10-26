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
package xtch.messages;

import java.nio.ByteBuffer;

import xtch.elements.Field;
import xtch.templates.MessageTemplate;

public abstract class Message extends FieldContainer {
  private final MessageTemplate template;

  public Message(MessageTemplate template) {
    this.template = template;
  }

  public MessageTemplate getTemplate() {
    return template;
  }

  public void addSequence(Sequence seq) {
    for (Field<?> field : seq.getFields()) {
      values.put(field, seq.get(field));
    }
  }

  public void encode(ByteBuffer buffer) {
    template.encode(buffer, this);
  }
}
