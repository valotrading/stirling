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
package xtch.turquoise;

import java.math.BigInteger;

import xtch.elements.Field;
import xtch.turquoise.types.Alpha;
import xtch.turquoise.types.AsciiString;
import xtch.turquoise.types.Price;
import xtch.turquoise.types.SignedInt32;
import xtch.turquoise.types.SignedInt8;
import xtch.turquoise.types.UnsignedInt16;
import xtch.turquoise.types.UnsignedInt32;
import xtch.turquoise.types.UnsignedInt64;

public class Fields {
  public static final Field<Integer> START_OF_MESSAGE = new SignedInt8Field("StartOfMessage");
  public static final Field<Integer> MESSAGE_LENGTH = new UnsignedInt16Field("MessageLength");
  public static final Field<Character> MESSAGE_TYPE = new AlphaField("MessageType");
  public static final Field<String> COMP_ID = new StringField("CompID", 25);
  public static final Field<String> PASSWORD = new StringField("Password", 25);
  public static final Field<String> NEW_PASSWORD = new StringField("NewPassword", 25);
  public static final Field<Integer> MESSAGE_VERSION = new SignedInt8Field("SequenceNumber");
  public static final Field<Integer> REJECT_CODE = new SignedInt32Field("RejectCode");
  public static final Field<String> PASSWORD_EXPIRY_DAY_COUNT = new StringField("PasswordExpiryDayCount", 30);
  public static final Field<String> LOGOUT_REASON = new StringField("LogoutReason", 20);
  public static final Field<Integer> APP_ID = new SignedInt8Field("AppID");
  public static final Field<Integer> LAST_MSG_SEQ_NUM = new SignedInt32Field("LastMsgSeqNum");
  public static final Field<Integer> RESPONSE_TYPE = new SignedInt8Field("ResponseType");
  public static final Field<String> REJECT_REASON = new StringField("RejectReason", 30);
  public static final Field<Character> REJECTED_MESSAGE_TYPE = new AlphaField("RejectedMessageType");
  public static final Field<String> CLIENT_ORDER_ID = new StringField("ClientOrderID", 20);
  public static final Field<String> TRADER_ID = new StringField("TraderID", 11);
  public static final Field<String> ACCOUNT = new StringField("Account", 10);
  public static final Field<Integer> CLEARING_ACCOUNT = new SignedInt8Field("ClearingAccount");
  public static final Field<String> COMMON_SYMBOL = new StringField("CommonSymbol", 6);
  public static final Field<Integer> ORDER_TYPE = new SignedInt8Field("OrderType");
  public static final Field<Integer> TIME_IN_FORCE = new SignedInt8Field("TimeInForce");
  public static final Field<Long> EXPIRE_DATE_TIME = new UnsignedInt32Field("ExpireDateTime");
  public static final Field<Integer> SIDE = new SignedInt8Field("Side");
  public static final Field<Integer> ORDER_QTY = new SignedInt32Field("OrderQty");
  public static final Field<Integer> DISPLAY_QTY = new SignedInt32Field("DisplayQty");
  public static final Field<Double> LIMIT_PRICE = new PriceField("LimitPrice");
  public static final Field<Integer> CAPACITY = new SignedInt8Field("Capacity");
  public static final Field<Integer> AUTO_CANCEL = new SignedInt8Field("AutoCancel");
  public static final Field<Integer> ORDER_SUB_TYPE = new SignedInt8Field("OrderSubType");
  public static final Field<Integer> RESERVED_FIELD_1 = new SignedInt8Field("ReservedField1");
  public static final Field<Double> RESERVED_FIELD_2 = new PriceField("ReservedField2");
  public static final Field<Integer> TARGET_BOOK = new SignedInt8Field("TargetBook");
  public static final Field<Integer> EXEC_INSTRUCTION = new SignedInt8Field("ExecInstruction");
  public static final Field<Integer> MIN_QTY = new SignedInt32Field("MinQty");
  public static final Field<String> RESERVED_FIELD_3 = new StringField("ReservedField3", 4);
  public static final Field<String> ORIGINAL_CLIENT_ORDER_ID = new StringField("ClientOrderID", 20);
  public static final Field<String> ORDER_ID = new StringField("OrderID", 12);
  public static final Field<Integer> MASS_CANCEL_TYPE = new SignedInt8Field("MassCancelType");
  public static final Field<String> SEGMENT = new StringField("Segment", 4);
  public static final Field<Integer> SEQUENCE_NO = new SignedInt32Field("SequenceNo");
  public static final Field<String> EXECUTION_ID = new StringField("ExecutionID", 12);
  public static final Field<Character> EXEC_TYPE = new AlphaField("ExecType");
  public static final Field<String> EXECUTION_REPORT_REF_ID = new StringField("ExecutionReportRefID", 12);
  public static final Field<Integer> ORDER_STATUS = new SignedInt8Field("OrderStatus");
  public static final Field<Integer> ORDER_REJECT_CODE = new SignedInt32Field("OrderRejectCode");
  public static final Field<Double> EXECUTED_PRICE = new PriceField("ExecutedPrice");
  public static final Field<Integer> EXECUTED_QTY = new SignedInt32Field("ExecutedQty");
  public static final Field<Integer> LEAVES_QTY = new SignedInt32Field("LeavesQty");
  public static final Field<BigInteger> SECONDARY_ORDER_ID = new UnsignedInt64Field("SecondaryOrderID");
  public static final Field<String> COUNTERPARTY = new StringField("Counterparty", 11);
  public static final Field<Character> TRADE_LIQUIDITY_INDICATOR = new AlphaField("TradeLiquidityIndicator");
  public static final Field<BigInteger> TRADE_MATCH_ID = new UnsignedInt64Field("TradeMatchID");
  public static final Field<BigInteger> TRANSACT_TIME = new UnsignedInt64Field("TransactTime");
  public static final Field<Integer> TYPE_OF_TRADE = new SignedInt8Field("TypeOfTrade");
  public static final Field<Integer> CANCEL_REJECT_REASON = new SignedInt32Field("CancelRejectReason");
  public static final Field<Integer> MASS_CANCEL_RESPONSE = new SignedInt8Field("MassCancelResponse");
  public static final Field<Integer> MASS_CANCEL_REJECT_REASON = new SignedInt32Field("MassCancelRejectReason");

  private static class PriceField extends Field<Double> {
    protected PriceField(String name) {
      super(name, Price.TYPE, 8);
    }
  }
  
  private static class AlphaField extends Field<Character> {
    protected AlphaField(String name) {
      super(name, Alpha.TYPE, 1);
    }
  }

  private static class StringField extends Field<String> {
    protected StringField(String name, int length) {
      super(name, AsciiString.TYPE, length);
    }
  }

  private static class SignedInt8Field extends Field<Integer> {
    protected SignedInt8Field(String name) {
      super(name, SignedInt8.TYPE, 1);
    }
  }

  private static class UnsignedInt16Field extends Field<Integer> {
    protected UnsignedInt16Field(String name) {
      super(name, UnsignedInt16.TYPE, 2);
    }
  }

  private static class SignedInt32Field extends Field<Integer> {
    protected SignedInt32Field(String name) {
      super(name, SignedInt32.TYPE, 4);
    }
  }

  private static class UnsignedInt32Field extends Field<Long> {
    protected UnsignedInt32Field(String name) {
      super(name, UnsignedInt32.TYPE, 4);
    }
  }

  private static class UnsignedInt64Field extends Field<BigInteger> {
    protected UnsignedInt64Field(String name) {
      super(name, UnsignedInt64.TYPE, 8);
    }
  }
}
