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
package fixengine.messages.fix44.burgundy

import fixengine.tags.fix44.burgundy.IncludeTickRules
import fixengine.tags.fix42.{
  SecurityReqID,
  Currency
}
import fixengine.tags.fix43.SecurityListRequestType
import fixengine.messages.{
  MessageVisitor,
  Required,
  AbstractMessage,
  MessageHeader,
  SecurityListRequest => SecurityListRequestTrait
}

class SecurityListRequest(header: MessageHeader) extends AbstractMessage(header) with SecurityListRequestTrait {
  field(Currency.Tag, Required.NO)
  field(IncludeTickRules.Tag, Required.NO)
  field(SecurityReqID.Tag)
  field(SecurityListRequestType.Tag)

  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}
