/*
 * Copyright 2009 the original author or authors.
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
package fixengine.tags.fix44.mbtrading;

import fixengine.messages.StringField;
import fixengine.messages.Tag;

public class OrderGroupID1 extends Tag<StringField> {
    public static final OrderGroupID1 TAG = new OrderGroupID1();

    public OrderGroupID1() { super(10055, StringField.class); }
}
