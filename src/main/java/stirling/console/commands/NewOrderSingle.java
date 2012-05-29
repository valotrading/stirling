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
package stirling.console.commands;

import stirling.console.ConsoleClient;
import stirling.fix.messages.Message;

import static stirling.fix.messages.fix42.MsgTypeValue.NEW_ORDER_SINGLE;

public class NewOrderSingle extends FixMessageCommand {
  @Override protected Message newMessage(ConsoleClient client) {
    return client.getMessageFactory().create(NEW_ORDER_SINGLE);
  }

  @Override protected boolean isModifyingOrderMessage() {
    return false;
  }

  @Override public String usage() {
    return "<Argument=value>* : Creates and sends new single order message.";
  }
}
