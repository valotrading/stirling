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
package stirling.nasdaqomx.souptcp10

import java.nio.ByteBuffer
import silvertip.{GarbledMessageException, MessageParser, PartialMessageException}
import stirling.io.ByteBuffers

object HelloParser extends MessageParser[HelloMessage] {
  def parse(buffer: ByteBuffer): HelloMessage = {
    if (buffer.remaining < HelloMessage.length)
      throw new PartialMessageException

    ByteBuffers.getAlpha(buffer, HelloMessage.length) match {
      case HELO.string => HELO
      case EHLO.string => EHLO
      case unknown     => throw new GarbledMessageException("Unknown message: %s".format(unknown))
    }
  }
}
