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
public class MessageHeader extends AbstractFieldContainer implements Parseable {
    private final AbstractFieldContainer head = new AbstractFieldContainer() { };
    private static final Minutes MAX_TIME_DIFFERENCE = Minutes.TWO;
    private int msgTypePosition;

    public MessageHeader(MsgTypeValue msgType) {
        this(msgType.value());
    }

    public MessageHeader(String msgType) {
        this();

        setMsgType(msgType);
    }

    public MessageHeader() {
        head.field(BeginString.TAG);
        head.field(BodyLength.TAG);
        head.field(MsgType.TAG);
        field(SenderCompID.TAG);
        field(TargetCompID.TAG);
        field(OnBehalfOfCompID.TAG, Required.NO);
        field(DeliverToCompID.TAG, Required.NO);
        field(MsgSeqNum.TAG);
        field(PossDupFlag.TAG, Required.NO);
        field(PossResend.TAG, Required.NO);
        field(SendingTime.TAG);
        field(OrigSendingTime.TAG, new Required() {
            @Override public boolean isRequired() {
                return hasValue(PossDupFlag.TAG);
            }
        });
    }

    @Override public void parse(ByteBuffer b) {
        parseHeadField(b, BeginString.TAG);
        parseHeadField(b, BodyLength.TAG);
        msgTypePosition = b.position();
        parseHeadField(b, MsgType.TAG);
        fields.parse(b);
    }

    private void parseHeadField(ByteBuffer b, Tag<?> tag) {
        try {
            tag.parse(b);
            Field field = head.lookup(tag.value());
            field.parse(b);
            if (!field.isFormatValid())
                throw new GarbledMessageException(tag.prettyName() + ": Invalid value format");
            if (field.isEmpty())
                throw new GarbledMessageException(tag.prettyName() + ": Empty tag");
        } catch (UnexpectedTagException e) {
            throw new GarbledMessageException(tag.prettyName() + ": is missing");
        }
    }

    public String getBeginString() {
        return head.getString(BeginString.TAG);
    }

    public void setBeginString(String beginString) {
        head.setString(BeginString.TAG, beginString);
    }

    public int getBodyLength() {
        return head.getInteger(BodyLength.TAG);
    }

    public String getMsgType() {
        return head.getString(MsgType.TAG);
    }

    public void setMsgType(String msgType) {
        head.setString(MsgType.TAG, msgType);
    }

    public boolean isPointToPoint() {
        return !hasValue(OnBehalfOfCompID.TAG) && !hasValue(DeliverToCompID.TAG);
    }

    public boolean hasAccurateSendingTime(DateTime currentTime) {
        if (!hasValue(SendingTime.TAG)) {
            return true;
        }
        Minutes difference = Minutes.minutesBetween(currentTime, getDateTime(SendingTime.TAG));
        return Math.abs(difference.getMinutes()) < MAX_TIME_DIFFERENCE.getMinutes();
    }

    public boolean hasOrigSendTimeAfterSendingTime() {
        if (!getBoolean(PossDupFlag.TAG) || !hasValue(OrigSendingTime.TAG)) {
            return true;
        }
        return !getDateTime(OrigSendingTime.TAG).isAfter(getDateTime(SendingTime.TAG));
    }

    public int getMsgTypePosition() {
        return msgTypePosition;
    }

    public Message newMessage() {
        MsgTypeValue type = MsgTypeValue.parse(getMsgType());
        return type.newMessage(this);
    }
}
