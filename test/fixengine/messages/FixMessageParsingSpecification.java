/*
 * Copyright 2009 the original author or authors.
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

import org.joda.time.DateTime;

import fixengine.Version;
import jdave.Specification;

/**
 * @author Pekka Enberg 
 */
public class FixMessageParsingSpecification<T extends Message> extends Specification<T> {
    @SuppressWarnings("unchecked")
    protected T parse(MsgType type, String body) {
        TokenStream tokens = toTokenStream(type, body);
        MessageHeader header = new MessageHeader();
        header.parse(tokens);
        T message = (T) type.newMessage(header);
        message.parse(tokens);
        return message;
    }

    protected TokenStream toTokenStream(MsgType msgType, String body) {
        MessageBuffer buffer = new MessageBuffer();
        buffer.append(new MsgTypeField(msgType.value()));
        buffer.append(new SenderCompIdField("S"));
        buffer.append(new TargetCompIdField("T"));
        buffer.append(new MsgSeqNumField(1));
        buffer.append(new SendingTimeField(new DateTime()));
        buffer.append(body);
        buffer.prefix(new BodyLengthField(buffer.length()));
        buffer.prefix(new BeginStringField(Version.FIX_4_3));
        buffer.append(new CheckSumField(buffer.checksum()));
        return new TokenStream(buffer.toString());
    }
}