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
package stirling.fix.messages.fix42.samrat

import stirling.fix.messages.{
  AbstractMessage,
  Logon => LogonTrait,
  MessageHeader,
  MessageVisitor,
  Required
}
import stirling.fix.tags.fix42.{
  EncryptMethod,
  HeartBtInt
}
import stirling.fix.tags.fix42.samrat.{
  CancelAllOnDisconnect
}
import stirling.fix.tags.fix44.{
  Password,
  Username
}

class Logon(header: MessageHeader) extends AbstractMessage(header) with LogonTrait {
  field(EncryptMethod.Tag)
  field(HeartBtInt.Tag)
  field(Username.Tag)
  field(Password.Tag, Required.NO)
  field(CancelAllOnDisconnect.Tag, Required.NO)
  def apply(visitor: MessageVisitor) = visitor.visit(this)
}
