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

import org.specs.Specification
import org.specs.runner.JUnit4

import java.lang.Integer

class MessageTagSpec extends JUnit4(MessageTagSpec)
object MessageTagSpec extends Specification {
  "EnumTag" should {
    "resolve fields" in {
      IntTag.parse(1) must equalTo(IntTag.Foo)
      IntTag.parse(2) must equalTo(IntTag.Bar)
    }
  }
  "EnumTag" should {
    "throw an exception" in {
      IntTag.parse(0) must throwAn[InvalidValueForTagException]
    }
  }
  private object IntTag extends EnumTag[Integer](0) {
    val Tag = this
    val Foo = Value(1)
    val Bar = Value(2)
  }
}
