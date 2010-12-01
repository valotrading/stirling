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
import fixengine.messages.RepeatingGroup;
import fixengine.messages.RepeatingGroupInstance;
import fixengine.messages.Required;
import fixengine.tags.Account;
import fixengine.tags.ClOrdID;
import fixengine.tags.HandlInst;
import fixengine.tags.OrdType;
import fixengine.tags.OrderQty;
import fixengine.tags.Side;
import fixengine.tags.TimeInForce;
import fixengine.tags.TransactTime;
import fixengine.tags.fix42.ExDestination;
import fixengine.tags.fix42.StopPx;
import fixengine.tags.fix43.LegCFICode;
import fixengine.tags.fix43.LegMaturityMonthYear;
import fixengine.tags.fix43.LegPositionEffect;
import fixengine.tags.fix43.LegRatioQty;
import fixengine.tags.fix43.LegRefID;
import fixengine.tags.fix43.LegSide;
import fixengine.tags.fix43.LegStrikePrice;
import fixengine.tags.fix43.LegSymbol;
import fixengine.tags.fix43.NoLegs;
import fixengine.tags.fix44.NoTrdRegTimestamps;
import fixengine.tags.fix44.Password;
import fixengine.tags.fix44.TrdRegTimestamp;
import fixengine.tags.fix44.TrdRegTimestampOrigin;
import fixengine.tags.fix44.TrdRegTimestampType;
import fixengine.tags.fix44.Username;
import fixengine.tags.fix44.burgundy.ExecInst;

public class NewOrderMultiLegMessage extends fixengine.messages.fix44.NewOrderMultiLegOrderMessage {
    public NewOrderMultiLegMessage(MessageHeader header) {
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
        field(Side.TAG);
        field(TimeInForce.TAG);
        field(TransactTime.TAG);
        field(ExDestination.TAG);
        field(NoTrdRegTimestamps.TAG, Required.NO);
        field(TrdRegTimestamp.TAG, Required.NO);
        field(TrdRegTimestampType.TAG, Required.NO);
        field(TrdRegTimestampOrigin.TAG, Required.NO);
        group(new RepeatingGroup(NoLegs.TAG) {
            @Override protected RepeatingGroupInstance newInstance() {
                return new RepeatingGroupInstance(LegSymbol.TAG) {
                    {
                        field(LegCFICode.TAG);
                        field(LegMaturityMonthYear.TAG);
                        field(LegStrikePrice.TAG);
                        field(LegPositionEffect.TAG);
                        field(LegSide.TAG);
                        field(LegRatioQty.TAG);
                        field(LegRefID.TAG);
                        field(Username.TAG);
                        field(Password.TAG);
                    }
                };
            }
        }, Required.NO);
    }
}