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
package stirling.mbtrading.quoteapi

import silvertip.Connection

class MbtSession {
  import MbtMessage._

  private var level1 = Map[String, Fields]()

  def receive(message: MbtMessage): MbtMessage = message match {
    case message: Level1Update =>
      message.getString(Tag.Symbol) match {
        case Some(symbol) =>
          update(symbol, message)
          message.copy(fields = level1(symbol))
        case None =>
          message
      }
    case _ =>
      message
  }

  def send(connection: Connection[MbtMessage], message: MbtMessage) {
    connection.send(message.format)
  }

  private def update(symbol: String, message: Level1Update) {
    level1 = level1.get(symbol) match {
      case Some(fields) =>
        level1.updated(symbol, fields ++ message.fields)
      case None =>
        level1.updated(symbol, message.fields)
    }
  }
}
