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
package stirling.console.commands;

import static stirling.fix.messages.fix44.MsgTypeValue.REQUEST_FOR_POSITIONS;

import stirling.console.ConsoleClient;
import stirling.fix.messages.Message;
import stirling.fix.messages.MessageFactory;

public class RequestForPositions extends FixMessageCommand {
    @Override protected Message newMessage(ConsoleClient client) {
        MessageFactory messageFactory = client.getMessageFactory();
        return messageFactory.create(REQUEST_FOR_POSITIONS);
    }

    @Override protected boolean isModifyingOrderMessage() {
        return false;
    }

    @Override public String description() {
        return "Creates and sends a request for positions.";
    }

    @Override public String usage() {
        return "<Argument=value>* : " + description();
    }
}
