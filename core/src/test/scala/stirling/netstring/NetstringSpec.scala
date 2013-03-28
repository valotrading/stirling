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
package stirling.netstring

import java.nio.ByteBuffer
import java.nio.charset.Charset
import org.scalatest.WordSpec
import org.scalatest.matchers.MustMatchers
import silvertip.{GarbledMessageException, PartialMessageException}

class NetstringSpec extends WordSpec with MustMatchers {
  "Netstring" when {
    "formatting" should {
      "format a non-empty message" in {
        format("abc") must equal("3:abc,")
      }
      "format an empty message" in {
        format("") must equal("0:,")
      }
    }
    "parsing" should {
      "parse a complete message successfully" in {
        parse("3:abc,") must equal("abc")
      }
      "throw a PartialMessageException on partial length" in {
        intercept[PartialMessageException] {
          parse("3")
        }
      }
      "throw a GarbledMessageException on malformed length" in {
        intercept[GarbledMessageException] {
          parse("a:")
        }.getMessage must equal("Expected integer, got 'a'")
      }
      "throw a PartialMessageException on missing content" in {
        intercept[PartialMessageException] {
          parse("3:")
        }
      }
      "throw a PartialMessageException on partial content" in {
        intercept[PartialMessageException] {
          parse("3:ab")
        }
      }
      "throw a PartialMessageException on missing trailer" in {
        intercept[PartialMessageException] {
          parse("3:abc")
        }
      }
      "throw a GarbledMessageException on malformed trailer" in {
        intercept[GarbledMessageException] {
          parse("3:abcd")
        }.getMessage must equal("Expected 0x2C, got 0x64")
      }
    }
  }

  def format(content: String): String = {
    val bytes = Netstring.format(content.getBytes(ASCII))

    new String(bytes, ASCII)
  }

  def parse(message: String): String = {
    val buffer = ByteBuffer.wrap(message.getBytes)
    val bytes  = Netstring.parse(buffer)

    new String(bytes, ASCII)
  }

  def ASCII = Charset.forName("US-ASCII")
}
