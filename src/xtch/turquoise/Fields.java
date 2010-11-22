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
import xtch.turquoise.types.AsciiString;
import xtch.turquoise.types.SignedInt8;

public class Fields {
  public static final Field<String> COMP_ID = new StringField("CompID", 25);
  public static final Field<String> PASSWORD = new StringField("Password", 25);
  public static final Field<String> NEW_PASSWORD = new StringField("NewPassword", 25);
  public static final Field<Integer> MESSAGE_VERSION = new SignedInt8Field("SequenceNumber");

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
}
