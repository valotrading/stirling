/*
 * Copyright 2010 the original author or authors.
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
package fixengine.messages.fix42;

import fixengine.messages.AbstractMessage;
import fixengine.messages.MessageHeader;
import fixengine.messages.MessageVisitor;

import fixengine.tags.fix42.SubscriptionRequestType;
import fixengine.tags.fix42.TradSesMethod;
import fixengine.tags.fix42.TradSesMode;
import fixengine.tags.fix42.TradSesReqID;
import fixengine.tags.fix42.TradingSessionID;

public class TradingSessionStatusRequestMessage extends AbstractMessage implements fixengine.messages.TradingSessionStatusRequestMessage {
    public TradingSessionStatusRequestMessage(MessageHeader header) {
        super(header);

        field(TradSesReqID.TAG);
        field(TradingSessionID.TAG);
        field(TradSesMethod.TAG);
        field(TradSesMode.TAG);
        field(SubscriptionRequestType.TAG);
    }

    @Override public void apply(MessageVisitor visitor) {
        visitor.visit(this);
    }
}
