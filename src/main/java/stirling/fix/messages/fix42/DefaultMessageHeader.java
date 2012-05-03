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
package stirling.fix.messages.fix42;

import stirling.fix.messages.AbstractMessageHeader;
import stirling.fix.messages.Required;
import stirling.fix.tags.fix42.DeliverToCompID;
import stirling.fix.tags.fix42.DeliverToSubID;
import stirling.fix.tags.fix42.MsgSeqNum;
import stirling.fix.tags.fix42.OnBehalfOfCompID;
import stirling.fix.tags.fix42.OnBehalfOfSubID;
import stirling.fix.tags.fix42.OrigSendingTime;
import stirling.fix.tags.fix42.PossDupFlag;
import stirling.fix.tags.fix42.PossResend;
import stirling.fix.tags.fix42.SenderCompID;
import stirling.fix.tags.fix42.SenderSubID;
import stirling.fix.tags.fix42.SendingTime;
import stirling.fix.tags.fix42.TargetCompID;
import stirling.fix.tags.fix42.TargetSubID;
import stirling.fix.tags.fix42.SenderLocationID;

/**
 * @author Pekka Enberg 
 */
public class DefaultMessageHeader extends AbstractMessageHeader {

    public DefaultMessageHeader(String msgType) {
        this();

        setMsgType(msgType);
    }

    public DefaultMessageHeader() {
        field(SenderCompID.Tag());
        field(SenderSubID.Tag(), Required.NO);
        field(TargetCompID.Tag());
        field(TargetSubID.Tag(), Required.NO);
        field(OnBehalfOfCompID.Tag(), Required.NO);
        field(OnBehalfOfSubID.Tag(), Required.NO);
        field(DeliverToCompID.Tag(), Required.NO);
        field(DeliverToSubID.Tag(), Required.NO);
        field(MsgSeqNum.Tag());
        field(PossDupFlag.Tag(), Required.NO);
        field(PossResend.Tag(), Required.NO);
        field(SendingTime.Tag());
        field(OrigSendingTime.Tag(), new Required() {
            @Override public boolean isRequired() {
                return hasValue(PossDupFlag.Tag()) && getBoolean(PossDupFlag.Tag());
            }
        });
        field(SenderLocationID.Tag(), Required.NO);
    }
}
