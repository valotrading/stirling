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
package stirling.fix.console.commands;

import static stirling.fix.messages.fix42.MsgTypeValue.LOGON;

import stirling.fix.console.ConsoleClient;
import stirling.fix.messages.Message;
import stirling.fix.messages.MessageFactory;
import stirling.fix.tags.fix42.EncryptMethod;
import stirling.fix.tags.fix42.HeartBtInt;

public class Logon extends FixMessageCommand {
    @Override protected Message newMessage(ConsoleClient client) {
        MessageFactory messageFactory = client.getMessageFactory();
        stirling.fix.messages.Logon message = (stirling.fix.messages.Logon) messageFactory.create(LOGON);
        message.setInteger(HeartBtInt.Tag(), 30);
        message.setEnum(EncryptMethod.Tag(), EncryptMethod.None());
        return message;
    }

    @Override protected boolean isModifyingOrderMessage() {
        return false;
    }

    @Override public String description() {
        return "Creates and sends logon message.";
    }

    @Override public String usage() {
        return "<Argument=value>* : " + description();
    }
}
