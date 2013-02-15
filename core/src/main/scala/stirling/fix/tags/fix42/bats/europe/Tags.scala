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
package stirling.fix.tags.fix42.bats.europe

import stirling.fix.messages.{
  BooleanTag,
  CharValue,
  EnumTag,
  FloatTag,
  IntegerTag,
  IntegerValue,
  StringTag,
  StringValue,
  Value
}
import java.lang.{
  String,
  Character,
  Integer
}

object ExecInst extends EnumTag[Character](18) {
  val MarketPeg = CharValue('P')        /* Market Peg (peg buy to MBBO offer, peg sell to MBBO bid) */
  val PrimaryPeg = CharValue('R')       /* Primary Peg (peg buy to MBBO bid, peg sell to MBBO offer) */
  val Midpoint = CharValue('M')         /* Midpoint (peg to MBBO midpoint) */
  val Alternate = CharValue('L')        /* Alternate Midpoint (less agressive of midpoint and 1 tick inside MBBO */
  val BatsMoc = CharValue('c')          /* BATS Market On Close */
  val BatsExtDarkOnly = CharValue('u')  /* BATS + External Dark Only */
  val BatsExtDarkLit = CharValue('v')   /* BATS + External Dark + Lit (default for routable orders) */
  val BatsExtLitOnly = CharValue('w')   /* BATS + External Lit Only */
}
object IDSource extends EnumTag[Character](22) {
  val SEDOL = CharValue('2')
  val ISIN = CharValue('4')
  val RIC = CharValue('5')
}
object SecurityID extends EnumTag[Character](48) {
  val SEDOL = CharValue('2')
  val ISIN = CharValue('4')
  val RIC = CharValue('5')
}
object Side extends EnumTag[Character](54) {
  val Buy = CharValue('1')
  val Sell = CharValue('2')
  val BuyMinus = CharValue('3')
  val SellPlus = CharValue('4')
  val SellShort = CharValue('5')
  val SellShortExempt = CharValue('6')
  val Undisclosed = CharValue('7')
  val Cross = CharValue('8')
  val CrossShort = CharValue('9')
  val CrossShortExempt = CharValue('A')
  val AsDefined = CharValue('B')
  val Opposite = CharValue('C')
}
object TimeInForce extends EnumTag[Integer](59) {
  val Day = IntegerValue(0)
  val GoodTillCancel = IntegerValue(1)
  val ImmediateOrCancel = IntegerValue(3)
  val GoodTillDate = IntegerValue(6)
  val GoodThroughCrossing = IntegerValue(8)
}
object CxlRejReason extends EnumTag[Integer](102) {
  val TooLateToCancel = IntegerValue(0)
  val UnknownOrder = IntegerValue(1)
  val PendingCancelOrReplace = IntegerValue(3)
}
object ExecType extends EnumTag[Character](150) {
  val New = CharValue('0')
  val PartialFill = CharValue('1')
  val Fill = CharValue('2')
  val Canceled = CharValue('4')
  val Replace = CharValue('5')
  val Rejected = CharValue('8')
  val Restated = CharValue('D')
  def isTrade(v: Value[Character]): Boolean = v == Fill || v == PartialFill
}
object CrossFlag extends EnumTag[Character](7740) {
  val MatchOnlyParticipant = CharValue('F')
  val MatchOnlyTradingFirm = CharValue('M')
}
object CentralCounterparty extends EnumTag[String](7772) {
  val EMCF = StringValue("EMCF") /* European Multilateral Clearing Facility */
  val LCHL = StringValue("LCHL") /* LCH.CLearnet */
  val XCLR = StringValue("XCLR") /* SIX x-clear */
}
object PreventParticipantMatch extends StringTag(7928)
object RoutingInst extends EnumTag[String](9303) {
  val Bats = StringValue("B")                             /* BATS Only */
  val PostOnly = StringValue("P")                         /* BATS Only - Post Only */
  val PostOnlyAtLimit = StringValue("Q")                  /* BATS Only - Post Only At Limit */
  val DarkBookOnly = StringValue("BD")                    /* BATS Dark Book Only */
  val AutomaticDarkRouted = StringValue("BA")             /* BATS Automatic Dark Routed */
  val DarkSelfCross = StringValue("BX")                   /* BATS Dark Self Cross */
  val RoutableCycle = StringValue("R")                    /* Routable CYCLE */
  val RoutableRecylcleOnLock = StringValue("RL")          /* Routable (CYCLE on Cross) */
  val RoutableRecycleOnCross = StringValue("RC")          /* Routable (RECYCLE on Cross) */
  val BatsPlusPrimaryListingExchagne = StringValue("PP")  /* BATS Plus Primary Listing Exchange */
  val BatsPlusLiquidnet = StringValue("PL")               /* BATS Plus Liquidnet */
}
object DisplayIndicator extends EnumTag[Character](9479) {
  val DisplayedOrder = CharValue('X')
  val Invisible = CharValue('I')
}
object ModifySequence extends IntegerTag(9617)
object MaxRemovePct extends IntegerTag(9618)
object CancelOrigOnReject extends BooleanTag(9619)
object CorrectedPrice extends FloatTag(9620)
object MTFAccessFee extends FloatTag(9621)
object OrigCompID extends StringTag(9688)
object OrigSubID extends StringTag(9689)
object TradeLiquidityIndicator extends EnumTag[String](9730) {
  val AddedLiquidity = StringValue("A")                         /* Added Liquidity */
  val RemovedLiquidity = StringValue("R")                       /* Removed Liquidity */
  val AddedLiquidityToDark = StringValue("AD")                  /* Added Liquidity to the BATS Dark Pool */
  val RemovedLiquidityFromDark = StringValue("RD")              /* Remove Liquidity from the BATS Dark Pool */
  val AddedLiquidityToDarkSelfCross = StringValue("AM")         /* Added Liquidity to the BATS Dark Self Cross */
  val RemovedLiquidityFromDarkSelfCross = StringValue("RM")     /* Removed Licensed from the BATS Dark Self Cross */
  val RoutedToAnotherMarket = StringValue("X")                  /* Routed to Another Market */
}
