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
package stirling.itch

import stirling.itch.messages.{Message, MessageOps}
import stirling.itch.types.{FieldType, FieldTypeOps}

abstract class Spec extends BaseSpec with Helpers

trait Helpers {
  implicit def fieldTypeToFieldTypeOps[T](value: FieldType[T]) = new FieldTypeOps(value)
  implicit def messageToMessageOps(value: Message) = new MessageOps(value)
}
