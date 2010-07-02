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
import org.joda.time.Minutes;

import fixengine.Config;

/**
 * @author Pekka Enberg
 */
public abstract class AbstractMessage implements Message {
    private static final Minutes MAX_TIME_DIFFERENCE = Minutes.TWO;

    private final DeliverToCompIdField deliverToCompIdField = new DeliverToCompIdField(Required.NO);
    private final OnBehalfOfCompIdField onBehalfOfCompId = new OnBehalfOfCompIdField(Required.NO);
    private final OrigSendingTimeField origSendingTime = new OrigSendingTimeField(Required.NO);
    private final PossDupFlagField possDupFlag = new PossDupFlagField(Required.NO);
    private final PossResendField possResend = new PossResendField(Required.NO);
    private final SenderCompIdField senderCompId = new SenderCompIdField();
    private final TargetCompIdField targetCompId = new TargetCompIdField();
    private final BeginStringField beginString = new BeginStringField();
    private final SendingTimeField sendingTime = new SendingTimeField();
    private final MsgSeqNumField msgSeqNum = new MsgSeqNumField();
    private final Fields fields = new Fields();
    private final MsgTypeField msgType;
    private final MessageHeader header;

    protected AbstractMessage(MsgType msgType) {
        this(new MessageHeader(msgType));
    }

    protected AbstractMessage(MessageHeader header) {
        this(header.getMsgType().value(), header);
    }

    protected AbstractMessage(String msgType, MessageHeader header) {
        this.msgType = new MsgTypeField(msgType);
        this.header = header;

        add(senderCompId);
        add(targetCompId);
        add(onBehalfOfCompId);
        add(deliverToCompIdField);
        add(msgSeqNum);
        add(possDupFlag);
        add(possResend);
        add(sendingTime);
        add(origSendingTime);
    }

    public abstract void apply(MessageVisitor visitor);

    public void setHeaderConfig(Config config) {
        setBeginString(config.getVersion().value());
        setSenderCompId(config.getSenderCompId());
        setTargetCompId(config.getTargetCompId());
    }

    public String getMsgType() {
        return msgType.getValue();
    }

    public void setBeginString(String beginString) {
        this.beginString.setValue(beginString);
    }

    public String getBeginString() {
        return beginString.getValue();
    }

    public void setBodyLength(int bodyLength) {
        header.setBodyLength(bodyLength);
    }

    public void setSenderCompId(String senderCompId) {
        this.senderCompId.setValue(senderCompId);
    }

    public String getSenderCompId() {
        return senderCompId.getValue();
    }

    public void setTargetCompId(String targetCompId) {
        this.targetCompId.setValue(targetCompId);
    }

    public String getTargetCompId() {
        return targetCompId.getValue();
    }

    public void setOnBehalfOfCompId(String onBehalfOfCompId) {
        this.onBehalfOfCompId.setValue(onBehalfOfCompId);
    }

    public void setDeliverToCompId(String deliverToCompId) {
        this.deliverToCompIdField.setValue(deliverToCompId);
    }

    public void setMsgSeqNum(int msgSeqNum) {
        this.msgSeqNum.setValue(msgSeqNum);
    }

    public int getMsgSeqNum() {
        return msgSeqNum.getValue();
    }

    public void setSendingTime(DateTime sendingTime) {
        this.sendingTime.setValue(sendingTime);
    }

    public DateTime getSendingTime() {
        return sendingTime.getValue();
    }

    public void setOrigSendingTime(DateTime origSendingTime) {
        this.origSendingTime.setValue(origSendingTime);
    }

    public DateTime getOrigSendingTime() {
        return origSendingTime.getValue();
    }

    public boolean hasOrigSendingTime() {
        return origSendingTime.hasValue();
    }

    public void setPossDupFlag(boolean possDupFlag) {
        this.possDupFlag.setValue(possDupFlag);
    }

    public boolean getPossDupFlag() {
        if (!possDupFlag.hasValue())
            return false;
        return possDupFlag.getValue();
    }

    public boolean getPossResend() {
        if (!possResend.hasValue())
            return false;
        return possResend.getValue();
    }

    public boolean hasValidTargetCompId(Config config) {
        return config.getSenderCompId().equals(getTargetCompId());
    }

    public boolean isPointToPoint() {
        return !onBehalfOfCompId.hasValue() && !deliverToCompIdField.hasValue();
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
        if (!sendingTime.hasValue()) {
            return true;
        }
        Minutes difference = Minutes.minutesBetween(currentTime, getSendingTime());
        return difference.isLessThan(MAX_TIME_DIFFERENCE);
    }

    @Override
    public boolean hasOrigSendTimeAfterSendingTime() {
        if (!getPossDupFlag() || !origSendingTime.hasValue()) {
            return true;
        }
        return !origSendingTime.getValue().isAfter(sendingTime.getValue());
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
        buffer.append(msgType);
        buffer.append(fields.format());
        buffer.prefix(new BodyLengthField(buffer.length()));
        buffer.prefix(beginString);
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
        result.append(beginString + " ");
        result.append(msgType + " ");
        for (Field field : fields.getFields()) {
            result.append(field.toString() + " ");
        }
        return result.toString();
    }
}
