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
package stirling.itch.bats.top120

/*
 * Section 4.3
 */
object RejectReason {
  val AuthenticationOrAuthorizationProblem = 'A'.toByte
}

/*
 * Section 5.1.2
 */
object HaltStatus {
  val Halted    = 'H'.toByte
  val QuoteOnly = 'Q'.toByte
  val Trading   = 'T'.toByte
}

/*
 * Section 5.1.2
 */
object RegSHOAction {
  val NoPriceTestInEffect                = '0'.toByte
  val RegSHOPriceTestRestrictionInEffect = '1'.toByte
}
