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
package xtch.turquoise.templates

import xtch.turquoise.Fields
import xtch.turquoise.MessageType

/**
 * Template for MissedMessageReport message as specified in section 7.3.7 of [2].
 */
object MissedMessageReport extends AbstractTemplate(MessageType.MissedMessageReport) {
  add(MessageHeader.TEMPLATE);
  add(Fields.RESPONSE_TYPE);
}
