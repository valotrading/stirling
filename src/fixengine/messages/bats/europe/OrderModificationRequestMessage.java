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
import fixengine.messages.Required;

import fixengine.tags.Account;
import fixengine.tags.ClOrdID;
import fixengine.tags.Currency;
import fixengine.tags.OrderID;
import fixengine.tags.OrderQty;
import fixengine.tags.OrigClOrdID;
import fixengine.tags.SecurityExchange;
import fixengine.tags.Symbol;

import fixengine.tags.bats.europe.IDSource;
import fixengine.tags.bats.europe.OrdType;
import fixengine.tags.bats.europe.SecurityID;
import fixengine.tags.bats.europe.Side;
import fixengine.tags.bats.europe.CancelOrigOnReject;

public class OrderModificationRequestMessage extends fixengine.messages.OrderModificationRequestMessage {
    public OrderModificationRequestMessage() {
        super();
    }

    public OrderModificationRequestMessage(MessageHeader header) {
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
        field(IDSource.TAG);
        field(OrderID.TAG);
        field(OrderQty.TAG);
        field(OrdType.TAG);
        field(OrigClOrdID.TAG);
        field(SecurityID.TAG);
        field(Side.TAG);
        field(Symbol.TAG);
        field(SecurityExchange.TAG, new Required() {
            @Override public boolean isRequired() {
                return getEnum(IDSource.TAG).equals(IDSourceValue.ISIN);
            }
        });
        field(CancelOrigOnReject.TAG);
    }
}