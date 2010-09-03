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

import fixengine.Config;

/**
 * @author Pekka Enberg 
 */
public interface Message extends Parseable, Iterable<Field>  {
    void apply(MessageVisitor visitor);
    String format();
    Field lookup(Tag<?> tag);
    String getString(Tag<StringField> tag);
    void setString(Tag<StringField> tag, String value);
    void setInteger(Tag<IntegerField> tag, Integer value);
    void setFloat(Tag<FloatField> tag, Double value);
    void setBoolean(Tag<BooleanField> tag, Boolean value);
    <T extends Formattable> void setEnum(Tag<? extends EnumField<T>> tag, T value);
    void setBeginString(String beginString);
    String getBeginString();
    String getMsgType();
    int getMsgSeqNum();
    void setSenderCompId(String senderCompId);
    void setTargetCompId(String targetCompId);
    String getSenderCompId();
    String getTargetCompId();
    void setOnBehalfOfCompId(String onBehalfOfCompId);
    void setDeliverToCompId(String deliverToCompId);
    void setHeaderConfig(Config config);
    void setMsgSeqNum(int next);
    void setSendingTime(DateTime sendingTime);
    void setOrigSendingTime(DateTime origSendingTime);
    void setPossDupFlag(boolean possDupFlag);
    DateTime getSendingTime();
    DateTime getOrigSendingTime();
    boolean getPossDupFlag();
    boolean isPointToPoint();
    boolean hasAccurateSendingTime(DateTime currentTime);
    boolean hasOrigSendTimeAfterSendingTime();
    boolean hasOrigSendingTime();
    boolean hasValidBeginString(Config config);
    boolean hasValidSenderCompId(Config config);
    boolean hasValidTargetCompId(Config config);
    boolean isTooLowSeqNum(int seqNo);
    void validate();
}
