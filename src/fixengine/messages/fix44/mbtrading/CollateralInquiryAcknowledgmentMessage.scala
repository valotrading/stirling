/*
 * Copyright 2008 the original author or authors.
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
package fixengine.messages.fix44.mbtrading

import fixengine.messages.{
  AbstractMessage,
  CollateralInquiryAcknowledgmentMessage => CollateralInquiryAcknowledgmentTrait,
  MessageHeader,
  MessageVisitor,
  Required
}
import fixengine.tags.fix44.{
  CollInquiryID,
  CollInquiryStatus,
  TotNumReports
}
import fixengine.tags.fix44.mbtrading.UserQuotePerms

class CollateralInquiryAcknowledgmentMessage(header: MessageHeader) extends AbstractMessage(header) with CollateralInquiryAcknowledgmentTrait {
  field(CollInquiryID.Tag, Required.NO)
  field(TotNumReports.Tag)
  field(CollInquiryStatus.Tag)
  field(UserQuotePerms.Tag, Required.NO)

  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}
