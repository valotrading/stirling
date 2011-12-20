/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
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
package stirling.fix.messages.fix42.hotspotfx

import stirling.fix.messages.AbstractMessage
import stirling.fix.messages.MessageHeader
import stirling.fix.messages.MessageVisitor
import stirling.fix.messages.Required
import stirling.fix.tags.fix42.EncryptMethod
import stirling.fix.tags.fix42.HeartBtInt
import stirling.fix.tags.fix42.ResetSeqNumFlag
import stirling.fix.tags.fix44.Password
import stirling.fix.tags.fix44.Username

class Logon(header: MessageHeader) extends AbstractMessage(header) with stirling.fix.messages.Logon {
  field(EncryptMethod.Tag)
  field(HeartBtInt.Tag)
  field(ResetSeqNumFlag.Tag, Required.NO)
  field(Password.Tag, Required.NO)
  field(Username.Tag, Required.NO)

  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}
