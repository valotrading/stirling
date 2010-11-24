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

import xtch.elements.Field;
import xtch.turquoise.types.Alpha;
import xtch.turquoise.types.AsciiString;
import xtch.turquoise.types.SignedInt32;
import xtch.turquoise.types.SignedInt8;
import xtch.turquoise.types.UnsignedInt16;

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
}
