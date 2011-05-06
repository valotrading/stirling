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

import fixengine.messages.{
  AbstractMessage,
  MessageHeader,
  MessageVisitor,
  OrderMassCancelRequest => OrderMassCancelRequestTrait,
  Required
}
import fixengine.tags.fix42.{
  ClOrdID,
  OnBehalfOfCompID,
  SecurityID,
  Symbol,
  TransactTime
}
import fixengine.tags.fix43.{
  SecurityIDSource
}
import fixengine.tags.fix44.{
  MassCancelRequestType
}

class OrderMassCancelRequest(header: MessageHeader) extends AbstractMessage(header) with OrderMassCancelRequestTrait {
  field(ClOrdID.Tag)
  field(OnBehalfOfCompID.Tag, Required.NO)
  field(MassCancelRequestType.Tag)
  field(Symbol.Tag, Required.NO)
  field(SecurityIDSource.Tag, Required.NO)
  field(SecurityID.Tag, Required.NO)
  field(TransactTime.Tag)
  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}
