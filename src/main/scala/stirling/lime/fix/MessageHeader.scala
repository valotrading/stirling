/*
 * Copyright 2012 the original author or authors.
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
package stirling.lime.fix

import stirling.fix.messages.{
  AbstractMessageHeader,
  Required
}
import stirling.fix.tags.fix42.{
  MsgSeqNum,
  OrigSendingTime,
  PossDupFlag,
  PossResend,
  SenderCompID,
  SenderSubID,
  SendingTime,
  TargetCompID,
  TargetSubID
}

class MessageHeader extends AbstractMessageHeader {
  field(SenderCompID.Tag)
  field(TargetCompID.Tag)
  field(MsgSeqNum.Tag)
  field(SenderSubID.Tag,     Required.NO)
  field(TargetSubID.Tag,     Required.NO)
  field(PossDupFlag.Tag,     Required.NO)
  field(PossResend.Tag,      Required.NO)
  field(SendingTime.Tag)
  field(OrigSendingTime.Tag, Required.NO)
}
