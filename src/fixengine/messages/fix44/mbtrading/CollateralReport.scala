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
  CollateralReport => CollateralReportMessageTrait,
  MessageHeader,
  MessageVisitor,
  Required
}
import fixengine.tags.fix42.{
  Account,
  Commission,
  Currency
}
import fixengine.tags.fix44.{
  CollInquiryID,
  CollRptID,
  CollStatus,
  MarginExcess,
  MarginRatio,
  TotalNetValue
}
import fixengine.tags.fix44.mbtrading.{
  AccountCredit,
  BODOOvernightExcessEq,
  MBTAccountType,
  MorningBuyingPower,
  MorningExcessEquity,
  MorningExcessEquity2,
  OvernightExcess,
  RealizedPnL
}
class CollateralReport(header: MessageHeader) extends AbstractMessage(header) with CollateralReportMessageTrait {
  field(Account.Tag)
  field(Commission.Tag, Required.NO)
  field(Currency.Tag, Required.NO)
  field(MarginRatio.Tag, Required.NO)
  field(MarginExcess.Tag)
  field(TotalNetValue.Tag)
  field(CollRptID.Tag)
  field(CollInquiryID.Tag, Required.NO)
  field(CollStatus.Tag)

  field(MorningBuyingPower.Tag)
  field(MBTAccountType.Tag)
  field(RealizedPnL.Tag, Required.NO)
  field(MorningExcessEquity.Tag, Required.NO)
  field(MorningExcessEquity2.Tag, Required.NO)
  field(AccountCredit.Tag, Required.NO)
  field(OvernightExcess.Tag, Required.NO)
  field(BODOOvernightExcessEq.Tag, Required.NO)
  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}
