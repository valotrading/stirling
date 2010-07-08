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

import java.nio.ByteBuffer;

import org.joda.time.DateTime;
import org.joda.time.Minutes;

import fixengine.tags.BeginString;
import fixengine.tags.BodyLength;
import fixengine.tags.DeliverToCompID;
import fixengine.tags.MsgSeqNum;
import fixengine.tags.MsgType;
import fixengine.tags.OnBehalfOfCompID;
import fixengine.tags.OrigSendingTime;
import fixengine.tags.PossDupFlag;
import fixengine.tags.PossResend;
import fixengine.tags.SenderCompID;
import fixengine.tags.SendingTime;
import fixengine.tags.TargetCompID;

/**
 * @author Pekka Enberg 
 */
public class MessageHeader implements Parseable {
    private static final Minutes MAX_TIME_DIFFERENCE = Minutes.TWO;

    private final UtcTimestampField origSendingTime = new UtcTimestampField(OrigSendingTime.TAG, Required.NO);
    private final StringField onBehalfOfCompID = new StringField(OnBehalfOfCompID.TAG, Required.NO);
    private final StringField deliverToCompID = new StringField(DeliverToCompID.TAG, Required.NO);
    private final BooleanField possDupFlag = new BooleanField(PossDupFlag.TAG, Required.NO);
    private final BooleanField possResend = new BooleanField(PossResend.TAG, Required.NO);
    private final StringField senderCompID = new StringField(SenderCompID.TAG);
    private final StringField targetCompID = new StringField(TargetCompID.TAG);
    private final StringField beginString = new StringField(BeginString.TAG);
    private final IntegerField bodyLength = new IntegerField(BodyLength.TAG);
    private final UtcTimestampField sendingTime = new UtcTimestampField(SendingTime.TAG);
    private final IntegerField msgSeqNum = new IntegerField(MsgSeqNum.TAG);
    private final StringField msgType = new StringField(MsgType.TAG);
    private final Fields fields = new Fields();

    private int msgTypePosition;

    public MessageHeader() {
        this((String) null);
    }

    public MessageHeader(MessageType msgType) {
        this(msgType.value());
    }

    public MessageHeader(String msgType) {
        this.msgType.setValue(msgType);

        add(senderCompID);
        add(targetCompID);
        add(onBehalfOfCompID);
        add(deliverToCompID);
        add(msgSeqNum);
        add(possDupFlag);
        add(possResend);
        add(sendingTime);
        add(origSendingTime);
    }

    protected void add(Field field) {
        fields.add(field);
    }

    @Override public void parse(ByteBuffer b) {
        fields.parse(b);
    }

    public Field lookup(Tag tag) {
        return fields.lookup(tag);
    }

    public Fields getFields() {
        return fields;
    }

    public void setMsgType(String msgType) {
        this.msgType.setValue(msgType);
    }

    public StringField getBeginStringField() {
        return beginString;
    }

    public StringField getMsgTypeField() {
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
        this.senderCompID.setValue(senderCompId);
    }

    public String getSenderCompId() {
        return senderCompID.getValue();
    }

    public void setTargetCompId(String targetCompId) {
        this.targetCompID.setValue(targetCompId);
    }

    public String getTargetCompId() {
        return targetCompID.getValue();
    }

    public void setOnBehalfOfCompId(String onBehalfOfCompId) {
        this.onBehalfOfCompID.setValue(onBehalfOfCompId);
    }

    public void setDeliverToCompId(String deliverToCompId) {
        this.deliverToCompID.setValue(deliverToCompId);
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
        return !onBehalfOfCompID.hasValue() && !deliverToCompID.hasValue();
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

    public void setMsgTypePosition(int msgTypePosition) {
        this.msgTypePosition = msgTypePosition;
    }

    public int getMsgTypePosition() {
        return msgTypePosition;
    }

    public Message newMessage() {
        MessageType type = MessageType.parse(getMsgType());
        if (type == null) {
            throw new RuntimeException();
        }
        return type.newMessage(this);
    }

    public void validate() {
        fields.validate();
    }
}
