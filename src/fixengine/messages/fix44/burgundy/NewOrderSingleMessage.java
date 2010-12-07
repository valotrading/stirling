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
package fixengine.messages.fix44.burgundy;

import fixengine.messages.MessageHeader;
import fixengine.messages.RepeatingGroup;
import fixengine.messages.RepeatingGroupInstance;
import fixengine.messages.Required;
import fixengine.messages.TimeInForceValue;
import fixengine.tags.Account;
import fixengine.tags.ClOrdID;
import fixengine.tags.Currency;
import fixengine.tags.ExpireTime;
import fixengine.tags.HandlInst;
import fixengine.tags.MaxFloor;
import fixengine.tags.MaxShow;
import fixengine.tags.MinQty;
import fixengine.tags.NoPartyIDs;
import fixengine.tags.OrdType;
import fixengine.tags.OrderQty;
import fixengine.tags.Price;
import fixengine.tags.SecurityID;
import fixengine.tags.Symbol;
import fixengine.tags.Text;
import fixengine.tags.TimeInForce;
import fixengine.tags.TransactTime;
import fixengine.tags.fix42.Side;
import fixengine.tags.fix43.AccountType;
import fixengine.tags.fix43.OrderCapacity;
import fixengine.tags.fix43.PartyID;
import fixengine.tags.fix43.PartyIDSource;
import fixengine.tags.fix43.PegOffsetValue;
import fixengine.tags.fix43.SecurityIDSource;
import fixengine.tags.fix44.PegMoveType;
import fixengine.tags.fix44.PegOffsetType;
import fixengine.tags.fix44.PegScope;
import fixengine.tags.fix44.burgundy.DisplayHighQty;
import fixengine.tags.fix44.burgundy.DisplayLowQty;
import fixengine.tags.fix44.burgundy.DisplayMethod;
import fixengine.tags.fix44.burgundy.DisplayMinIncr;
import fixengine.tags.fix44.burgundy.ExecInst;
import fixengine.tags.fix44.burgundy.OrderRestrictions;
import fixengine.tags.fix44.burgundy.PartyRole;

public class NewOrderSingleMessage extends fixengine.messages.fix44.NewOrderSingleMessage {
    public NewOrderSingleMessage(MessageHeader header) {
        super(header);
    }

    @Override protected void fields() {
        field(Account.TAG, Required.NO);
        field(ClOrdID.TAG);
        field(Currency.TAG, Required.NO);
        field(ExecInst.TAG, Required.NO);
        field(HandlInst.TAG, Required.NO);
        field(Symbol.TAG);
        field(SecurityIDSource.TAG, Required.NO);
        field(SecurityID.TAG, Required.NO);
        field(OrderQty.TAG, Required.NO);
        field(OrdType.TAG);
        field(Price.TAG);
        field(Side.Tag());
        field(Text.TAG, Required.NO);
        field(TimeInForce.TAG, Required.NO);
        field(TransactTime.TAG);
        field(MinQty.TAG, Required.NO);
        field(DisplayMethod.TAG, Required.NO);
        field(DisplayLowQty.TAG, Required.NO);
        field(DisplayHighQty.TAG, Required.NO);
        field(DisplayMinIncr.TAG, Required.NO);
        field(MaxFloor.TAG, Required.NO);
        field(ExpireTime.TAG, new Required() {
            @Override public boolean isRequired() {
                return getEnum(TimeInForce.TAG).equals(TimeInForceValue.GOOD_TILL_DATE);
            }
        });
        field(MaxShow.TAG, Required.NO);
        field(PegOffsetValue.TAG, Required.YES);
        field(PegMoveType.TAG, Required.NO);
        field(PegOffsetType.TAG, Required.NO);
        field(PegScope.TAG, Required.NO);
        group(new RepeatingGroup(NoPartyIDs.TAG) {
            @Override protected RepeatingGroupInstance newInstance() {
                return new RepeatingGroupInstance(PartyID.TAG) {
                    {
                        field(PartyIDSource.TAG);
                        field(PartyRole.TAG, Required.NO);
                    }
                };
            }
        }, Required.NO);
        field(OrderCapacity.TAG, Required.NO);
        field(OrderRestrictions.TAG, Required.NO);
        field(AccountType.TAG, Required.NO);
    }
}
