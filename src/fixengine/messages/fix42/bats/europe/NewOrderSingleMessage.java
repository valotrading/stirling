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
package fixengine.messages.fix42.bats.europe;

import fixengine.messages.MessageHeader;
import fixengine.messages.Required;
import fixengine.tags.Account;
import fixengine.tags.ClOrdID;
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
import fixengine.tags.Symbol;
import fixengine.tags.fix42.ClearingAccount;
import fixengine.tags.fix42.ClearingFirm;
import fixengine.tags.fix42.Side;
import fixengine.tags.fix42.bats.europe.DisplayIndicator;
import fixengine.tags.fix42.bats.europe.ExecInst;
import fixengine.tags.fix42.bats.europe.IDSource;
import fixengine.tags.fix42.bats.europe.MaxRemovePct;
import fixengine.tags.fix42.bats.europe.OrigCompID;
import fixengine.tags.fix42.bats.europe.OrigSubID;
import fixengine.tags.fix42.bats.europe.PreventParticipantMatch;
import fixengine.tags.fix42.bats.europe.RoutingInst;
import fixengine.tags.fix42.bats.europe.SecurityID;
import fixengine.tags.fix42.bats.europe.TimeInForce;

public class NewOrderSingleMessage extends fixengine.messages.fix42.NewOrderSingleMessage {
    public NewOrderSingleMessage(MessageHeader header) {
        super(header);
    }

    @Override protected void fields() {
        field(Account.Tag(), Required.NO);
        field(ClOrdID.Tag());
        field(Currency.Tag(), new Required() {
            @Override public boolean isRequired() {
                return getEnum(IDSource.Tag()).equals(IDSource.ISIN());
            }
        });
        field(ExecInst.Tag(), Required.NO);
        field(IDSource.Tag(), new Required() {
            @Override public boolean isRequired() {
                return !hasValue(Symbol.Tag());
            }
        });
        field(OrderQty.Tag());
        field(OrdType.Tag());
        field(Price.Tag(), new Required() {
            @Override public boolean isRequired() {
                return getEnum(OrdType.Tag()).equals(OrdType.Limit());
            }
        });
        field(OrderCapacity.Tag(), Required.NO);
        field(SecurityID.Tag(), new Required() {
            @Override public boolean isRequired() {
                return hasValue(IDSource.Tag());
            }
        });
        field(Side.Tag());
        field(Symbol.Tag(), Required.NO);
        field(TimeInForce.Tag(), Required.NO);
        field(MinQty.Tag(), Required.NO);
        field(MaxFloor.Tag(), Required.NO);
        field(ExpireTime.TAG, new Required() {
            @Override public boolean isRequired() {
                return getEnum(TimeInForce.Tag()).equals(TimeInForce.GoodTillDate());
            }
        });
        field(SecurityExchange.Tag(), new Required() {
            @Override public boolean isRequired() {
                return getEnum(IDSource.Tag()).equals(IDSource.ISIN());
            }
        });
        field(PegDifference.TAG, Required.NO);
        field(ClearingFirm.TAG, Required.NO);
        field(ClearingAccount.TAG, Required.NO);
        field(PreventParticipantMatch.TAG, Required.NO);
        field(RoutingInst.Tag(), Required.NO);
        field(DisplayIndicator.Tag(), Required.NO);
        field(MaxRemovePct.Tag(), new Required() {
            @Override public boolean isRequired() {
                return getEnum(RoutingInst.Tag()).equals(RoutingInst.PostOnlyAtLimit());
            }
        });
        field(OrigCompID.TAG, Required.NO);
        field(OrigSubID.TAG, Required.NO);
    }
}
