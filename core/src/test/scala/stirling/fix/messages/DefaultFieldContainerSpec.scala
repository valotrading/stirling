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
package stirling.fix.messages

import org.scalatest.WordSpec
import org.scalatest.matchers.MustMatchers
import stirling.fix.tags.fix42.MsgType

class DefaultFieldContainerSpec extends WordSpec with MustMatchers with DefaultFieldContainerFixtures {
  "DefaultFieldContainer" should {
    "return false" in {
      newContainer.hasValue(MsgType.Tag) must equal(false)
    }
  }
}

trait DefaultFieldContainerFixtures {
  def newContainer = new DefaultFieldContainer
}
