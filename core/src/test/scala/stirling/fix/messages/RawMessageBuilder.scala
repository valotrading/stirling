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
package stirling.fix.messages

class RawMessageBuilder {
  def field(tag: Tag[_], value: String): RawMessageBuilder = {
    field(tag.value, value)
  }
  def field(tag: Int, value: String): RawMessageBuilder = {
    s.append(tag)
    s.append('=')
    s.append(value)
    s.append(Field.DELIMITER)
    this
  }
  override def toString = s.toString
  private val s = new StringBuilder
}
