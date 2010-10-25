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
import fixengine.tags.CheckSum;
import fixengine.tags.DeliverToCompID;
import fixengine.tags.MsgSeqNum;
import fixengine.tags.MsgType;
import fixengine.tags.OnBehalfOfCompID;
import fixengine.tags.OrigSendingTime;
import fixengine.tags.PossDupFlag;
import fixengine.tags.PossResend;
import fixengine.tags.SenderCompID;
import fixengine.tags.SenderSubID;
import fixengine.tags.SendingTime;
import fixengine.tags.TargetCompID;
import fixengine.tags.TargetSubID;

/**
 * @author Pekka Enberg 
 */
public class MessageHeader extends FieldContainer implements Parseable {
    private static final Minutes MAX_TIME_DIFFERENCE = Minutes.TWO;

    private final FieldContainer head = new FieldContainer();

    public MessageHeader(String msgType) {
        this();

        setMsgType(msgType);
    }

    public MessageHeader() {
        head.field(BeginString.TAG);
        head.field(BodyLength.TAG);
        head.field(MsgType.TAG);
        field(SenderCompID.TAG);
        field(SenderSubID.TAG, Required.NO);
        field(TargetCompID.TAG);
        field(TargetSubID.TAG, Required.NO);
        field(OnBehalfOfCompID.TAG, Required.NO);
        field(DeliverToCompID.TAG, Required.NO);
        field(MsgSeqNum.TAG);
        field(PossDupFlag.TAG, Required.NO);
        field(PossResend.TAG, Required.NO);
        field(SendingTime.TAG);
        field(OrigSendingTime.TAG, new Required() {
            @Override public boolean isRequired() {
                return hasValue(PossDupFlag.TAG) && getBoolean(PossDupFlag.TAG);
            }
        });
    }

    @Override public void parse(ByteBuffer b) {
        parseHeadField(b, BeginString.TAG);
        parseHeadField(b, BodyLength.TAG);
        trailer(b);
        parseHeadField(b, MsgType.TAG);
        if (!containsMsgSeqNum(b))
            throw new MsgSeqNumMissingException("MsgSeqNum(34) is missing");
        super.parse(b);
    }

    private boolean containsMsgSeqNum(ByteBuffer b) {
        try {
            b.mark();
            while (b.hasRemaining()) {
                try {
                    int tag = Tag.parseTag(b);
                    String value = AbstractField.parseValue(b);
                    if (tag == MsgSeqNum.TAG.value())
                        return isMsgSeqNumValueValid(value);
                } catch (NonDataValueIncludesFieldDelimiterException e) {
                    /* Ignore tag that cannot be parsed due to delimiter in tag */
                }
            }
            return false;
        } finally {
            b.reset();
        }
    }

    private boolean isMsgSeqNumValueValid(String value) {
        try {
            Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private void trailer(ByteBuffer b) {
        int checkSumPosition = b.position() + getBodyLength();
        int parsedChecksum = parseChecksum(b, checkSumPosition);
        b.limit(checkSumPosition);
    }

    private static int parseChecksum(ByteBuffer b, int checkSumPosition) {
        int origPosition = b.position();
        b.position(checkSumPosition);
        Tag.parseTag(b);
        StringField field = CheckSum.TAG.newField(Required.YES);
        field.parse(b);
        b.position(origPosition);
        return Integer.parseInt(field.getValue());
    }

    private void parseHeadField(ByteBuffer b, Tag<?> tag) {
        Tag.parseTag(b);
        Field field = head.lookup(tag.value());
        field.parse(b);
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

    public Message newMessage(MessageFactory messageFactory) {
        return messageFactory.create(getMsgType(), this);
    }
}
