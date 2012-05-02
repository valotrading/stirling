/*
 * Copyright 2012 the original author or authors.
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

import org.joda.time.DateTime;
import org.joda.time.Minutes;

import stirling.fix.tags.fix42.BeginString;
import stirling.fix.tags.fix42.BodyLength;
import stirling.fix.tags.fix42.DeliverToCompID;
import stirling.fix.tags.fix42.MsgType;
import stirling.fix.tags.fix42.OnBehalfOfCompID;
import stirling.fix.tags.fix42.OrigSendingTime;
import stirling.fix.tags.fix42.PossDupFlag;
import stirling.fix.tags.fix42.SendingTime;

public abstract class AbstractMessageHeader extends FieldContainer implements MessageHeader {
    private static final Minutes MAX_TIME_DIFFERENCE = Minutes.TWO;

    private final FieldContainer head = new FieldContainer();

    public AbstractMessageHeader(String msgType) {
        this();

        setMsgType(msgType);
    }

    public AbstractMessageHeader() {
        head.field(BeginString.Tag());
        head.field(BodyLength.Tag());
        head.field(MsgType.Tag());
    }

    @Override public void parse(ByteBuffer b) {
        parseHeadField(b, BeginString.Tag());
        parseHeadField(b, BodyLength.Tag());
        trailer(b);
        parseHeadField(b, MsgType.Tag());
        super.parse(b);
    }

    private void trailer(ByteBuffer b) {
        int checkSumPosition = b.position() + getBodyLength();
        b.limit(checkSumPosition);
    }

    private void parseHeadField(ByteBuffer b, Tag<?> tag) {
        Tag.parseTag(b);
        Field field = head.lookup(tag.value());
        field.parse(b);
    }

    public String getBeginString() {
        return head.getString(BeginString.Tag());
    }

    public void setBeginString(String beginString) {
        head.setString(BeginString.Tag(), beginString);
    }

    public int getBodyLength() {
        return head.getInteger(BodyLength.Tag());
    }

    public String getMsgType() {
        return head.getString(MsgType.Tag());
    }

    public void setMsgType(String msgType) {
        head.setString(MsgType.Tag(), msgType);
    }

    public boolean isPointToPoint() {
        return !hasValue(OnBehalfOfCompID.Tag()) && !hasValue(DeliverToCompID.Tag());
    }

    public boolean hasAccurateSendingTime(DateTime currentTime) {
        if (!hasValue(SendingTime.Tag())) {
            return true;
        }
        Minutes difference = Minutes.minutesBetween(currentTime, getDateTime(SendingTime.Tag()));
        return Math.abs(difference.getMinutes()) < MAX_TIME_DIFFERENCE.getMinutes();
    }

    public boolean hasOrigSendingTimeEarlierThanOrEqualToSendingTime() {
        if (!getBoolean(PossDupFlag.Tag()) || !hasValue(OrigSendingTime.Tag()) || !hasValue(SendingTime.Tag())) {
            return true;
        }
        return !getDateTime(OrigSendingTime.Tag()).isAfter(getDateTime(SendingTime.Tag()));
    }
}
