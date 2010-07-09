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

import lang.Objects;

import org.joda.time.DateTime;

import fixengine.Config;

/**
 * @author Pekka Enberg
 */
public abstract class AbstractEmptyMessage implements Message {
    @Override
    public abstract void apply(MessageVisitor visitor);

    @Override
    public String format() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getMsgType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getMsgSeqNum() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setBeginString(String beginString) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getBeginString() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setMsgSeqNum(int next) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setHeaderConfig(Config config) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSendingTime(DateTime sendingTime) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSenderCompId(String senderCompId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setTargetCompId(String targetCompId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setOnBehalfOfCompId(String onBehalfOfCompId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public DateTime getSendingTime() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setOrigSendingTime(DateTime origSendingTime) {
        throw new UnsupportedOperationException();
    }

    @Override
    public DateTime getOrigSendingTime() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getSenderCompId() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setPossDupFlag(boolean possDupFlag) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getPossDupFlag() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isPointToPoint() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasAccurateSendingTime(DateTime currentTime) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasOrigSendTimeAfterSendingTime() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasOrigSendingTime() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasValidBeginString(Config config) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasValidSenderCompId(Config config) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isTooLowSeqNum(int seqNo) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isOrigSendingTimeMissing() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Fields getFields() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object obj) {
        return Objects.equal(this, obj);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this);
    }
}
