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

import org.joda.time.DateTime;
import org.joda.time.Minutes;


/**
 * @author Pekka Enberg 
 */
public class MessageHeader {
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
    private final BodyLengthField bodyLength = new BodyLengthField();
    private final MsgSeqNumField msgSeqNum = new MsgSeqNumField();
    private final MsgTypeField msgType = new MsgTypeField();
    private final Fields fields = new Fields();

    public MessageHeader() {
        this((String) null);
    }

    public MessageHeader(MsgType msgType) {
        this(msgType.value());
    }

    public MessageHeader(String msgType) {
        this.msgType.setValue(msgType);

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

    protected void add(Field field) {
        fields.add(field);
    }

    public Field lookup(Tag tag) {
        return fields.lookup(tag);
    }

    public Fields getFields() {
        return fields;
    }

    public BeginStringField getBeginStringField() {
        return beginString;
    }

    public MsgTypeField getMsgTypeField() {
        return msgType;
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

    public void setBeginString(String beginString) {
      this.beginString.setValue(beginString);
    }

    public int getBodyLength() {
        return bodyLength.getValue();
    }

    public void setBodyLength(int bodyLength) {
        this.bodyLength.setValue(bodyLength);
    }

    public String getMsgType() {
        return msgType.getValue();
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

    public boolean isPointToPoint() {
        return !onBehalfOfCompId.hasValue() && !deliverToCompIdField.hasValue();
    }

    public boolean hasAccurateSendingTime(DateTime currentTime) {
        if (!sendingTime.hasValue()) {
            return true;
        }
        Minutes difference = Minutes.minutesBetween(currentTime, getSendingTime());
        return difference.isLessThan(MAX_TIME_DIFFERENCE);
    }

    public boolean hasOrigSendTimeAfterSendingTime() {
        if (!getPossDupFlag() || !origSendingTime.hasValue()) {
            return true;
        }
        return !origSendingTime.getValue().isAfter(sendingTime.getValue());
    }

    public Message newMessage() {
        MsgType type = MsgType.parse(getMsgType());
        if (type == null) {
            return new UnknownMessage(msgType.getValue());
        }
        return type.newMessage(this);
    }
}