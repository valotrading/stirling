/*
 * Copyright 2011 the original author or authors.
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
package fixengine.examples.console.commands;

import static fixengine.messages.fix44.MsgTypeValue.COLLATERAL_INQUIRY;

import fixengine.examples.console.ConsoleClient;
import fixengine.messages.Message;
import fixengine.messages.MessageFactory;

public class CollateralInquiry extends FixMessageCommand {
  @Override protected Message newMessage(ConsoleClient client) {
    MessageFactory messageFactory = client.getMessageFactory();
    return messageFactory.create(COLLATERAL_INQUIRY);
  }

  @Override protected boolean isModifyingOrderMessage() {
    return false;
  }

  @Override public String usage() {
    return "<Argument=value>* : Creates and sends a collateral inquiry.";
  }
}
