/*
 * Copyright 2008 the original author or authors.
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

import fixengine.Version;

/**
 * @author Pekka Enberg 
 */
public class MessageHeader {
    private final BeginStringField beginString = new BeginStringField();
    private final BodyLengthField bodyLength = new BodyLengthField();
    private final MsgTypeField msgType = new MsgTypeField();

    public MessageHeader() {
    }

    public MessageHeader(MsgType msgType) {
        this(msgType.value());
    }

    public MessageHeader(String msgType) {
        this.msgType.setValue(msgType);
    }

    public void parse(TokenStream tokens) {
        beginString.supports(tokens.tag());
        beginString.parse(tokens);
        if (!Version.supports(beginString.getValue())) {
            throw new InvalidBeginStringException(beginString.getValue());
        }
        bodyLength.supports(tokens.tag());
        bodyLength.parse(tokens);

        msgType.supports(tokens.tag());
        msgType.parse(tokens);
    }

    public int checksum() {
        return beginString.checksum() + bodyLength.checksum() + msgType.checksum();
    }

    public int length() {
        return msgType.length();
    }

    public String getBeginString() {
        return beginString.getValue();
    }

    public int getBodyLength() {
        return bodyLength.getValue();
    }

    public void setBodyLength(int bodyLength) {
        this.bodyLength.setValue(bodyLength);
    }

    public MsgType getMsgType() {
        return MsgType.parse(msgType.getValue());
    }

    public Message newMessage() {
        MsgType type = getMsgType();
        if (type == null) {
            return new UnknownMessage(msgType.getValue(), this);
        }
        return type.newMessage(this);
    }
}