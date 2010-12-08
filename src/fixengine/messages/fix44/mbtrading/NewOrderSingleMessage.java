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
package fixengine.messages.fix44.mbtrading;

import fixengine.messages.MessageHeader;
import fixengine.messages.Required;
import fixengine.messages.TimeInForceValue;
import fixengine.tags.Account;
import fixengine.tags.ClOrdID;
import fixengine.tags.ExecInst;
import fixengine.tags.ExpireTime;
import fixengine.tags.HandlInst;
import fixengine.tags.MaturityMonthYear;
import fixengine.tags.MaxFloor;
import fixengine.tags.OrdType;
import fixengine.tags.OrderQty;
import fixengine.tags.PegDifference;
import fixengine.tags.SecurityType;
import fixengine.tags.SendingTime;
import fixengine.tags.Side;
import fixengine.tags.Symbol;
import fixengine.tags.TimeInForce;
import fixengine.tags.TransactTime;
import fixengine.tags.fix42.ComplianceID;
import fixengine.tags.fix42.DiscretionInst;
import fixengine.tags.fix42.DiscretionOffset;
import fixengine.tags.fix42.EffectiveTime;
import fixengine.tags.fix42.ExDestination;
import fixengine.tags.fix42.LocateReqd;
import fixengine.tags.fix42.PutOrCall;
import fixengine.tags.fix42.StopPx;
import fixengine.tags.fix42.StrikePrice;
import fixengine.tags.fix43.LegPrice;
import fixengine.tags.fix43.OrderRestrictions;
import fixengine.tags.fix43.Price2;
import fixengine.tags.fix43.SecondaryClOrdID;
import fixengine.tags.fix44.NoTrdRegTimestamps;
import fixengine.tags.fix44.TrdRegTimestamp;
import fixengine.tags.fix44.TrdRegTimestampOrigin;
import fixengine.tags.fix44.TrdRegTimestampType;
import fixengine.tags.fix44.Username;
import fixengine.tags.fix44.mbtrading.MBTXAggressive;
import fixengine.tags.fix44.mbtrading.OrderGroupID1;

public class NewOrderSingleMessage extends fixengine.messages.fix44.NewOrderSingleMessage {
    public NewOrderSingleMessage(MessageHeader header) {
        super(header);
    }

    @Override protected void fields() {
        field(Account.TAG);
        field(ClOrdID.TAG);
        field(ExecInst.TAG, Required.NO);
        field(HandlInst.TAG);
        field(OrderQty.TAG);
        field(OrdType.TAG);
        field(StopPx.TAG, Required.NO);
        field(SendingTime.TAG);
        field(Symbol.TAG);
        field(Side.TAG);
        field(TimeInForce.TAG);
        field(TransactTime.TAG);
        field(StopPx.TAG, Required.NO);
        field(ExDestination.TAG);
        field(MaxFloor.TAG, Required.NO);
        field(LocateReqd.TAG, Required.NO);
        field(ExpireTime.TAG, new Required() {
            @Override public boolean isRequired() {
                return getEnum(TimeInForce.TAG).equals(TimeInForceValue.GOOD_TILL_DATE);
            }
        });
        field(SecurityType.TAG, Required.NO);
        field(EffectiveTime.TAG, Required.NO);
        field(MaturityMonthYear.TAG, Required.NO);
        field(PutOrCall.TAG, Required.NO);
        field(StrikePrice.TAG, Required.NO);
        field(PegDifference.TAG, Required.NO);
        field(DiscretionInst.TAG, Required.NO);
        field(DiscretionOffset.TAG, Required.NO);
        field(ComplianceID.TAG, Required.NO);
        field(SecondaryClOrdID.TAG, Required.NO);
        field(OrderRestrictions.Tag(), Required.NO);
        field(Username.TAG);
        field(LegPrice.TAG, Required.NO);
        field(Price2.TAG, Required.NO);
        field(NoTrdRegTimestamps.TAG, Required.NO);
        field(TrdRegTimestamp.TAG, Required.NO);
        field(TrdRegTimestampType.TAG, Required.NO);
        field(TrdRegTimestampOrigin.TAG, Required.NO);
        field(MBTXAggressive.Tag(), Required.NO);
        field(OrderGroupID1.Tag(), Required.NO);
    }
}
