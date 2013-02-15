/*
 * Copyright 2009 the original author or authors.
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
package stirling.fix

import org.scalatest.WordSpec
import org.scalatest.matchers.MustMatchers

class ConfigSpec extends WordSpec with MustMatchers {
  "Config" when {
    "testing support" should {
      val config = new Config().setVersion(Version.FIX_4_2)
      "support earlier versions" in {
        config.supports(Version.FIX_4_1) must equal(true)
      }
      "support current version" in {
        config.supports(Version.FIX_4_2) must equal(true)
      }
      "not support future versions" in {
        config.supports(Version.FIX_4_3) must equal(false)
      }
    }
    "configuring counterparty" should {
      val senderCompId = "INITIATOR"
      val senderSubId = "SUB-INITIATOR"
      val targetCompId = "ACCEPTOR"
      val targetSubId = "SUB-ACCEPTOR"
      val version = Version.FIX_4_2
      val config = new Config(version, senderCompId, targetCompId)
        .setSenderSubId(senderSubId).setTargetSubId(targetSubId)
      val counterparty = config.counterparty
      "reverse sender and target identifiers" in {
        counterparty.getSenderCompId must equal(targetCompId)
        counterparty.getSenderSubId must equal(targetSubId)
        counterparty.getTargetCompId must equal(senderCompId)
        counterparty.getTargetSubId must equal(senderSubId)
      }
      "retain version" in {
        counterparty.getVersion must equal(version)
      }
    }
  }
}
