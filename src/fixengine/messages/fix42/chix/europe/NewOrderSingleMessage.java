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
package fixengine.messages.fix42.chix.europe;

import fixengine.messages.MessageHeader;
import fixengine.messages.OrdTypeValue;
import fixengine.messages.Required;
import fixengine.messages.TimeInForceValue;

import fixengine.tags.Account;
import fixengine.tags.MaxFloor;
import fixengine.tags.MinQty;
import fixengine.tags.Symbol;
import fixengine.tags.fix42.Side;
import fixengine.tags.TimeInForce;
import fixengine.tags.PegDifference;
import fixengine.tags.Price;
import fixengine.tags.OrderCapacity;
import fixengine.tags.SecurityID;
import fixengine.tags.SecurityExchange;
import fixengine.tags.ClOrdID;
import fixengine.tags.Currency;
import fixengine.tags.HandlInst;
import fixengine.tags.OrderQty;
import fixengine.tags.OrdType;
import fixengine.tags.ExpireTime;
import fixengine.tags.TransactTime;

import fixengine.tags.fix42.chix.europe.ExecInst;
import fixengine.tags.fix42.chix.europe.IDSource;

public class NewOrderSingleMessage extends fixengine.messages.fix42.NewOrderSingleMessage {
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
        field(HandlInst.TAG);
        field(IDSource.TAG, Required.NO);
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
        field(Side.Tag());
        field(Symbol.TAG);
        field(TimeInForce.TAG, Required.NO);
        field(TransactTime.TAG);
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
    }
}
