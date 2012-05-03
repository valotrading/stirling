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
package stirling.fix.messages.fix44.burgundy

import stirling.fix.tags.fix42.Text
import stirling.fix.tags.fix44.{
  Headline,
  NoLinesOfText
}
import stirling.fix.messages.{
  AbstractMessage,
  MessageHeader,
  MessageVisitor,
  News => NewsTrait,
  RepeatingGroup,
  RepeatingGroupInstance
}

class News(header: MessageHeader) extends AbstractMessage(header) with NewsTrait {
  field(Headline.Tag)

  def linesOfTextGrp() {
    group(new RepeatingGroup(NoLinesOfText.Tag) {
      override def newInstance() =
        new RepeatingGroupInstance(Text.Tag)
    })
  }

  linesOfTextGrp()

  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}
