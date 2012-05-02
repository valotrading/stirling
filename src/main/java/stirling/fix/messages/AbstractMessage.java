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
package stirling.fix.messages;

import java.nio.ByteBuffer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import stirling.lang.Objects;

import org.joda.time.DateTime;

import stirling.fix.Config;
import stirling.fix.tags.fix42.BeginString;
import stirling.fix.tags.fix42.BodyLength;
import stirling.fix.tags.fix42.CheckSum;
import stirling.fix.tags.fix42.DeliverToCompID;
import stirling.fix.tags.fix42.MsgSeqNum;
import stirling.fix.tags.fix42.MsgType;
import stirling.fix.tags.fix42.OnBehalfOfCompID;
import stirling.fix.tags.fix42.OrigSendingTime;
import stirling.fix.tags.fix42.PossDupFlag;
import stirling.fix.tags.fix42.PossResend;
import stirling.fix.tags.fix42.SenderCompID;
import stirling.fix.tags.fix42.SenderSubID;
import stirling.fix.tags.fix42.SendingTime;
import stirling.fix.tags.fix42.TargetCompID;
import stirling.fix.tags.fix42.TargetSubID;

/**
 * @author Pekka Enberg
 */
public abstract class AbstractMessage extends DefaultFieldContainer implements Message {
    private final DefaultMessageHeader header;
    private DateTime receiveTime;

    protected AbstractMessage(DefaultMessageHeader header) {
        this.header = header;
    }

    @Override public Iterator<Field> iterator() {
        List<Field> result = new ArrayList<Field>();
        Iterator<Field> fields = super.iterator();
        while (fields.hasNext())
            result.add(fields.next());
        for (Field field : header)
            result.add(field);
        return result.iterator();
    }

    public abstract void apply(MessageVisitor visitor);

    public boolean isAdminMessage() {
        return false;
    }

    public void setHeaderConfig(Config config) {
        setBeginString(config.getVersion().value());

        setSenderCompId(config.getSenderCompId());
        if (config.getSenderSubID() != null)
          setSenderSubID(config.getSenderSubID());

        setTargetCompId(config.getTargetCompId());
        if (config.getTargetSubID() != null)
          setTargetSubID(config.getTargetSubID());
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

    public void setSenderCompId(String senderCompId) {
        header.setString(SenderCompID.Tag(), senderCompId);
    }

    public void setSenderSubID(String senderSubID) {
        header.setString(SenderSubID.Tag(), senderSubID);
    }

    public String getSenderCompId() {
        return header.getString(SenderCompID.Tag());
    }

    public void setTargetCompId(String targetCompId) {
        header.setString(TargetCompID.Tag(), targetCompId);
    }

    public void setTargetSubID(String targetSubID) {
        header.setString(TargetSubID.Tag(), targetSubID);
    }

    public String getTargetCompId() {
        return header.getString(TargetCompID.Tag());
    }

    public void setOnBehalfOfCompId(String onBehalfOfCompId) {
        header.setString(OnBehalfOfCompID.Tag(), onBehalfOfCompId);
    }

    public void setDeliverToCompId(String deliverToCompId) {
        header.setString(DeliverToCompID.Tag(), deliverToCompId);
    }

    public void setMsgSeqNum(int msgSeqNum) {
        header.setInteger(MsgSeqNum.Tag(), msgSeqNum);
    }

    @Override public int getMsgSeqNum() {
        return header.getInteger(MsgSeqNum.Tag());
    }

    public void setSendingTime(DateTime sendingTime) {
        header.setDateTime(SendingTime.Tag(), sendingTime);
    }

    @Override public void setReceiveTime(DateTime receiveTime) {
        this.receiveTime = receiveTime;
    }

    public DateTime getSendingTime() {
        return header.getDateTime(SendingTime.Tag());
    }

    public void setOrigSendingTime(DateTime origSendingTime) {
        header.setDateTime(OrigSendingTime.Tag(), origSendingTime);
    }

    public DateTime getOrigSendingTime() {
        return header.getDateTime(OrigSendingTime.Tag());
    }

    public boolean hasOrigSendingTime() {
        return header.hasValue(OrigSendingTime.Tag());
    }

    public void setPossDupFlag(boolean possDupFlag) {
        header.setBoolean(PossDupFlag.Tag(), possDupFlag);
    }

    public boolean getPossDupFlag() {
        return header.getBoolean(PossDupFlag.Tag());
    }

    public boolean getPossResend() {
        return header.getBoolean(PossResend.Tag());
    }

    @Override public void setPossResend(boolean possResend) {
        header.setBoolean(PossResend.Tag(), possResend);
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
    public boolean hasAccurateSendingTime() {
        return header.hasAccurateSendingTime(receiveTime);
    }

    @Override
    public boolean hasOrigSendingTimeEarlierThanOrEqualToSendingTime() {
        return header.hasOrigSendingTimeEarlierThanOrEqualToSendingTime();
    }

    public void parse(ByteBuffer b) {
        super.parse(b);
        finishParse(b);
    }

    private void finishParse(ByteBuffer b) {
        if (b.hasRemaining()) {
            int tag = Tag.parseTag(b);
            Field field = header.lookup(tag);
            if (CheckSum.Tag().value() == tag) {
                throw new OutOfOrderTagException("CheckSum(10): Out of order tag");
            } else if (field != null) {
                throw new OutOfOrderTagException(field.prettyName() + ": Out of order tag");
            }
            throw new InvalidTagException("Tag not defined for this message: " + tag);
        }
    }

    public String format() {
        MessageBuffer buffer = new MessageBuffer();
        buffer.append(new StringField(MsgType.Tag(), header.getMsgType()));
        buffer.append(header.format());
        buffer.append(super.format());
        buffer.prefix(new IntegerField(BodyLength.Tag(), buffer.length()));
        buffer.prefix(new StringField(BeginString.Tag(), header.getBeginString()));
        buffer.append(new StringField(CheckSum.Tag(), CheckSum.format(buffer.checksum())));
        return buffer.toString();
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
        result.append(new StringField(BeginString.Tag(), header.getBeginString()).toString() + " ");
        result.append(new StringField(MsgType.Tag(), header.getMsgType()).toString() + " ");
        for (Field field : this) {
            String s = field.toString();
            if (s.isEmpty())
                continue;
            result.append(field.toString() + " ");
        }
        return result.toString();
    }

    @Override public int compareTo(Message message) {
        return getMsgSeqNum() - message.getMsgSeqNum();
    }
}
