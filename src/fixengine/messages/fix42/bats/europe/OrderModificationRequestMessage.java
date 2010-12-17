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

import fixengine.tags.fix42.Account;
import fixengine.tags.fix42.ClOrdID;
import fixengine.tags.fix42.Currency;
import fixengine.tags.fix42.OrdType;
import fixengine.tags.fix42.OrderID;
import fixengine.tags.fix42.OrderQty;
import fixengine.tags.fix42.OrigClOrdID;
import fixengine.tags.fix42.Price;
import fixengine.tags.fix42.SecurityExchange;
import fixengine.tags.fix42.Side;
import fixengine.tags.fix42.Symbol;
import fixengine.tags.fix42.bats.europe.CancelOrigOnReject;
import fixengine.tags.fix42.bats.europe.IDSource;
import fixengine.tags.fix42.bats.europe.SecurityID;

public class OrderModificationRequestMessage extends fixengine.messages.fix42.OrderModificationRequestMessage {
    public OrderModificationRequestMessage(MessageHeader header) {
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
        field(IDSource.Tag(), new Required() {
            @Override public boolean isRequired() {
                return !hasValue(Symbol.Tag());
            }
        });
        field(OrderID.Tag());
        field(OrderQty.Tag());
        field(OrdType.Tag());
        field(OrigClOrdID.Tag());
        field(Price.Tag(), new Required() {
            @Override public boolean isRequired() {
                return getEnum(OrdType.Tag()).equals(OrdType.Limit());
            }
        });
        field(SecurityID.Tag(), new Required() {
            @Override public boolean isRequired() {
                return hasValue(IDSource.Tag());
            }
        });
        field(Side.Tag());
        field(Symbol.Tag(), Required.NO);
        field(SecurityExchange.Tag(), new Required() {
            @Override public boolean isRequired() {
                return getEnum(IDSource.Tag()).equals(IDSource.ISIN());
            }
        });
        field(CancelOrigOnReject.Tag(), Required.NO);
    }
}
