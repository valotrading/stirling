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

import java.util.List;

import lang.Objects;

import org.joda.time.DateTime;

import fixengine.Config;

/**
 * @author Pekka Enberg
 */
public abstract class AbstractMessage implements Message {
    private final Fields fields = new Fields();
    private final MessageHeader header;

    protected AbstractMessage(MsgType msgType) {
        this(new MessageHeader(msgType));
    }

    protected AbstractMessage(MessageHeader header) {
        this.header = header;
    }

    public abstract void apply(MessageVisitor visitor);

    public void setHeaderConfig(Config config) {
        setBeginString(config.getVersion().value());
        setSenderCompId(config.getSenderCompId());
        setTargetCompId(config.getTargetCompId());
    }

    public String getMsgType() {
        return header.getMsgType();
    }

    public void setBeginString(String beginString) {
        header.setBeginString(beginString);
    }

    public String getBeginString() {
        return header.getBeginString();
    }

    public void setBodyLength(int bodyLength) {
        header.setBodyLength(bodyLength);
    }

    public void setSenderCompId(String senderCompId) {
        header.setSenderCompId(senderCompId);
    }

    public String getSenderCompId() {
        return header.getSenderCompId();
    }

    public void setTargetCompId(String targetCompId) {
        header.setTargetCompId(targetCompId);
    }

    public String getTargetCompId() {
        return header.getTargetCompId();
    }

    public void setOnBehalfOfCompId(String onBehalfOfCompId) {
        header.setOnBehalfOfCompId(onBehalfOfCompId);
    }

    public void setDeliverToCompId(String deliverToCompId) {
        header.setDeliverToCompId(deliverToCompId);
    }

    public void setMsgSeqNum(int msgSeqNum) {
        header.setMsgSeqNum(msgSeqNum);
    }

    public int getMsgSeqNum() {
        return header.getMsgSeqNum();
    }

    public void setSendingTime(DateTime sendingTime) {
        header.setSendingTime(sendingTime);
    }

    public DateTime getSendingTime() {
        return header.getSendingTime();
    }

    public void setOrigSendingTime(DateTime origSendingTime) {
        header.setOrigSendingTime(origSendingTime);
    }

    public DateTime getOrigSendingTime() {
        return header.getOrigSendingTime();
    }

    public boolean hasOrigSendingTime() {
        return header.hasOrigSendingTime();
    }

    public void setPossDupFlag(boolean possDupFlag) {
        header.setPossDupFlag(possDupFlag);
    }

    public boolean getPossDupFlag() {
        return header.getPossDupFlag();
    }

    public boolean getPossResend() {
        return header.getPossResend();
    }

    public boolean hasValidTargetCompId(Config config) {
        return config.getSenderCompId().equals(getTargetCompId());
    }

    public boolean isPointToPoint() {
        return header.isPointToPoint();
    }

    @Override
    public boolean hasValidBeginString(Config config) {
        return config.getVersion().value().equals(getBeginString());
    }

    @Override
    public boolean hasValidSenderCompId(Config config) {
        return config.getTargetCompId().equals(getSenderCompId());
    }

    @Override
    public boolean isTooLowSeqNum(int seqNo) {
        return !getPossDupFlag() && getMsgSeqNum() < seqNo;
    }

    @Override
    public boolean isOrigSendingTimeMissing() {
        return getPossDupFlag() && !hasOrigSendingTime();
    }

    @Override
    public boolean hasAccurateSendingTime(DateTime currentTime) {
        return header.hasAccurateSendingTime(currentTime);
    }

    @Override
    public boolean hasOrigSendTimeAfterSendingTime() {
        return header.hasOrigSendTimeAfterSendingTime();
    }

    @Override
    public Field lookup(Tag tag) {
        return fields.lookup(tag);
    }

    @Override
    public List<Field> getFields() {
        return fields.getFields();
    }

    public String format() {
        MessageBuffer buffer = new MessageBuffer();
        buffer.append(header.getMsgTypeField());
        buffer.append(header.getFields().format());
        buffer.append(fields.format());
        buffer.prefix(new BodyLengthField(buffer.length()));
        buffer.prefix(header.getBeginStringField());
        buffer.append(new CheckSumField(buffer.checksum()));
        return buffer.toString();
    }

    protected void add(Field field) {
        fields.add(field);
    }

    @Override
    public boolean equals(Object obj) {
        return Objects.equal(this, obj);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(header.getBeginStringField() + " ");
        result.append(header.getMsgTypeField() + " ");
        for (Field field : fields.getFields()) {
            result.append(field.toString() + " ");
        }
        return result.toString();
    }
}
