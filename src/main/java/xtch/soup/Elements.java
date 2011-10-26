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
package xtch.soup;

import xtch.elements.Field;
import xtch.types.AsciiString;

public class Elements {
  public static final Field<String> TEXT = new Text("Text");
  public static final Field<String> USERNAME = new Text("Username", 6);
  public static final Field<String> PASSWORD = new Text("Password", 10);
  public static final Field<String> SESSION = new Text("Session", 10);
  public static final Field<String> SEQUENCE_NUMBER = new Text("SequenceNumber", 10);
  public static final Field<String> REJECTED_REASON_CODE = new Text("RejectedReasonCode");

  private static class Text extends Field<String> {
    protected Text(String name, int length) {
      super(name, AsciiString.TYPE, length);
    }

    protected Text(String name) {
      super(name, AsciiString.TYPE); 
    }
  }
}
