/*
 * Copyright 2013 the original author or authors.
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
package stirling.bats.pitch1122

import org.scalatest.WordSpec
import org.scalatest.matchers.MustMatchers
import scala.language.implicitConversions
import stirling.io.ByteString

class MessageSpec extends WordSpec with MustMatchers {
  "Message" should {
    "decode SymbolClear" in {
      val message = SymbolClear("12345678sACME    ")
      message.timestamp            must equal(12345678)
      message.messageType          must equal('s')
      message.stockSymbol.toString must equal("ACME    ")
    }
  }

  implicit def stringToByteString(string: String): ByteString = new ByteString(string.getBytes)
}
