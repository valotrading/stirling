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
package fixengine.messages.bats.europe;

import fixengine.messages.MessageHeader;
import fixengine.messages.OrdTypeValue;
import fixengine.messages.Required;

import fixengine.tags.Account;
import fixengine.tags.ClOrdID;
import fixengine.tags.ClearingAccount;
import fixengine.tags.ClearingFirm;
import fixengine.tags.Currency;
import fixengine.tags.ExpireTime;
import fixengine.tags.MaxFloor;
import fixengine.tags.MinQty;
import fixengine.tags.OrdType;
import fixengine.tags.OrderCapacity;
import fixengine.tags.OrderQty;
import fixengine.tags.PegDifference;
import fixengine.tags.Price;
import fixengine.tags.SecurityExchange;
import fixengine.tags.Side;
import fixengine.tags.Symbol;

import fixengine.tags.bats.europe.DisplayIndicator;
import fixengine.tags.bats.europe.ExecInst;
import fixengine.tags.bats.europe.IDSource;
import fixengine.tags.bats.europe.MaxRemovePct;
import fixengine.tags.bats.europe.OrigCompID;
import fixengine.tags.bats.europe.OrigSubID;
import fixengine.tags.bats.europe.PreventParticipantMatch;
import fixengine.tags.bats.europe.RoutingInst;
import fixengine.tags.bats.europe.SecurityID;
import fixengine.tags.bats.europe.TimeInForce;

public class NewOrderSingleMessage extends fixengine.messages.NewOrderSingleMessage {
    public NewOrderSingleMessage(MessageHeader header) {
        super(header);
    }

    @Override protected void fields() {
        field(Account.TAG, Required.NO);
        field(ClOrdID.TAG);
        field(Currency.TAG, new Required() {
            @Override public boolean isRequired() {
                return getEnum(IDSource.TAG).equals(IDSourceValue.ISIN);
            }
        });
        field(ExecInst.TAG, Required.NO);
        field(IDSource.TAG, new Required() {
            @Override public boolean isRequired() {
                return !hasValue(Symbol.TAG);
            }
        });
        field(OrderQty.TAG);
        field(OrdType.TAG);
        field(Price.TAG, new Required() {
            @Override public boolean isRequired() {
                return getEnum(OrdType.TAG).equals(OrdTypeValue.LIMIT);
            }
        });
        field(OrderCapacity.TAG, Required.NO);
        field(SecurityID.TAG, new Required() {
            @Override public boolean isRequired() {
                return hasValue(IDSource.TAG);
            }
        });
        field(Side.TAG);
        field(Symbol.TAG, Required.NO);
        field(TimeInForce.TAG, Required.NO);
        field(MinQty.TAG, Required.NO);
        field(MaxFloor.TAG, Required.NO);
        field(ExpireTime.TAG, new Required() {
            @Override public boolean isRequired() {
                return getEnum(TimeInForce.TAG).equals(TimeInForceValue.GOOD_TILL_DATE);
            }
        });
        field(SecurityExchange.TAG, new Required() {
            @Override public boolean isRequired() {
                return getEnum(IDSource.TAG).equals(IDSourceValue.ISIN);
            }
        });
        field(PegDifference.TAG, Required.NO);
        field(ClearingFirm.TAG, Required.NO);
        field(ClearingAccount.TAG, Required.NO);
        field(PreventParticipantMatch.TAG, Required.NO);
        field(RoutingInst.TAG, Required.NO);
        field(DisplayIndicator.TAG, Required.NO);
        field(MaxRemovePct.TAG, new Required() {
            @Override public boolean isRequired() {
                return getEnum(RoutingInst.TAG).equals(RoutingInstValue.POST_ONLY_AT_LIMIT);
            }
        });
        field(OrigCompID.TAG, Required.NO);
        field(OrigSubID.TAG, Required.NO);
    }
}
