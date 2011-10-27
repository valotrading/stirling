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
package xtch.turquoise

object MessageType {
  /**
   * Administrative message types as specified in section 7.1.1 of [2].
   */
  val Logon = "A"
  val LogonReply = "B"
  val Logout = "5"
  val Heartbeat = "0"
  val MissedMessageRequest = "M"
  val MissedMessageRequestAck = "N"
  val MissedMessageReport = "P"
  val Reject = "3"

  /**
   * Client-initiated message types as specified in section 7.1.2.1 of [2].
   */
  val NewOrder = "D"
  val OrderCancelRequest ="F"
  val OrderMassCancelRequest = "q"
  val OrderCancelReplaceRequest = "G"

  /**
   * Server-initiated message types as specified in section 7.1.2.2 of [2].
   */
  val ExecutionReport = "8"
  val OrderCancelReject ="9"
  val OrderMassCancelReport = "r"

  /**
   * Other server-initiated message types as specified in section 7.1.3.1 of [2].
   */
  val BusinessMessageReject = "j"
}
