/*
 * Copyright 2011 the original author or authors.
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
package stirling.fix.messages.fix42

import org.scalatest.WordSpec
import org.scalatest.matchers.MustMatchers
import stirling.fix.messages.StringTag
import stirling.fix.tags.fix42.Account

class DefaultMessageFactorySpec extends WordSpec with MustMatchers {
  "DefaultMessageFactory" should {
    val factory = new DefaultMessageFactory
    "create an Account tag" in {
      factory.createTag("Account").asInstanceOf[StringTag] must equal(Account.Tag)
    }
  }
}
