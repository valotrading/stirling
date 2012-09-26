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
package stirling.itch

class ByteStringSpec extends Spec {
  "ByteString" when {
    "checking equality" should {
      "not equal another ByteString unless bytes within range equal" in {
        new ByteString(foo) must not equal(new ByteString(foobar))
      }
      "equal another ByteString if bytes within range are equal" in {
        new ByteString(foo) must equal(new ByteString(foobar, 0, 3))
      }
    }
    "slicing" should {
      "return a ByteString containing the range of the slice" in {
        new ByteString(foobar).slice(0, 3) must equal(new ByteString(foo))
      }
    }
  }

  def foo    = "foo".getBytes
  def foobar =  "foobar".getBytes
}
