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
package fixengine.messages

import java.lang.Integer
import org.scalatest.WordSpec
import org.scalatest.matchers.MustMatchers

class MessageTagSpec extends WordSpec with MustMatchers {
  "EnumTag" should {
    "resolve fields" in {
      IntTag.parse("1") must equal(IntTag.Foo)
      IntTag.valueOf("Foo") must equal(IntTag.Foo)
      IntTag.parse("2") must equal(IntTag.Bar)
      IntTag.valueOf("Bar") must equal(IntTag.Bar)
    }
  }
  "EnumTag" should {
    "throw an exception" in {
      intercept [InvalidValueForTagException] {
        IntTag.parse("0")
      }
    }
  }
  private object IntTag extends EnumTag[Integer](0) {
    val Foo = IntegerValue(1)
    val Bar = IntegerValue(2)
  }
}
