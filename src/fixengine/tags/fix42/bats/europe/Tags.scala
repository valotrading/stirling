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
package fixengine.tags.fix42.bats.europe

import fixengine.messages.{BooleanTag, EnumTag, IntegerTag, FloatTag, StringTag, Value}
import java.lang.{String, Character, Integer}

object ExecInst extends EnumTag[Character](18) {
  val Tag = this
  val MarketPeg = Value('P')        /* Market Peg (peg buy to MBBO offer, peg sell to MBBO bid) */
  val PrimaryPeg = Value('R')       /* Primary Peg (peg buy to MBBO bid, peg sell to MBBO offer) */
  val Midpoint = Value('M')         /* Midpoint (peg to MBBO midpoint) */
  val Alternate = Value('L')        /* Alternate Midpoint (less agressive of midpoint and 1 tick inside MBBO */
  val BatsMoc = Value('c')          /* BATS Market On Close */
  val BatsExtDarkOnly = Value('u')  /* BATS + External Dark Only */
  val BatsExtDarkLit = Value('v')   /* BATS + External Dark + Lit (default for routable orders) */
  val BatsExtLitOnly = Value('w')   /* BATS + External Lit Only */
}

object IDSource extends EnumTag[Character](22) {
  val Tag = this
  val SEDOL = Value('2')
  val ISIN = Value('4')
  val RIC = Value('5')
}

object SecurityID extends EnumTag[Character](48) {
  val Tag = this
  val SEDOL = Value('2')
  val ISIN = Value('4')
  val RIC = Value('5')
}

object Side extends EnumTag[Character](54) {
  val Tag = this
  val Buy = Value('1')
  val Sell = Value('2')
  val BuyMinus = Value('3')
  val SellPlus = Value('4')
  val SellShort = Value('5')
  val SellShortExempt = Value('6')
  val Undisclosed = Value('7')
  val Cross = Value('8')
  val CrossShort = Value('9')
  val CrossShortExempt = Value('A')
  val AsDefined = Value('B')
  val Opposite = Value('C')
}

object TimeInForce extends EnumTag[Integer](59) {
  val Tag = this
  val Day = Value(0)
  val GoodTillCancel = Value(1)
  val ImmediateOrCancel = Value(3)
  val GoodTillDate = Value(6)
  val GoodThroughCrossing = Value(8)
}

object CxlRejReason extends EnumTag[Integer](102) {
  val Tag = this
  val TooLateToCancel = Value(0)
  val UnknownOrder = Value(1)
  val PendingCancelOrReplace = Value(3)
}

object ExecType extends EnumTag[Character](150) {
  val Tag = this
  val New = Value('0')
  val PartialFill = Value('1')
  val Fill = Value('2')
  val Canceled = Value('4')
  val Replace = Value('5')
  val Rejected = Value('8')
  val Restated = Value('D')
  def isTrade(v: Value[Character]): Boolean = v == Fill || v == PartialFill
}

object CrossFlag extends EnumTag[Character](7740) {
  val Tag = this
  val MatchOnlyParticipant = Value('F')
  val MatchOnlyTradingFirm = Value('M')
}

object CentralCounterparty extends EnumTag[String](7772) {
  val Tag = this
  val EMCF = Value("EMCF") /* European Multilateral Clearing Facility */
  val LCHL = Value("LCHL") /* LCH.CLearnet */
  val XCLR = Value("XCLR") /* SIX x-clear */
}

object PreventParticipantMatch extends StringTag(7928) {
  val Tag = this
}

object RoutingInst extends EnumTag[String](9303) {
  val Tag = this
  val Bats = Value("B")                             /* BATS Only */
  val PostOnly = Value("P")                         /* BATS Only - Post Only */
  val PostOnlyAtLimit = Value("Q")                  /* BATS Only - Post Only At Limit */
  val DarkBookOnly = Value("BD")                    /* BATS Dark Book Only */
  val AutomaticDarkRouted = Value("BA")             /* BATS Automatic Dark Routed */
  val DarkSelfCross = Value("BX")                   /* BATS Dark Self Cross */
  val RoutableCycle = Value("R")                    /* Routable CYCLE */
  val RoutableRecylcleOnLock = Value("RL")          /* Routable (CYCLE on Cross) */
  val RoutableRecycleOnCross = Value("RC")          /* Routable (RECYCLE on Cross) */
  val BatsPlusPrimaryListingExchagne = Value("PP")  /* BATS Plus Primary Listing Exchange */
  val BatsPlusLiquidnet = Value("PL")               /* BATS Plus Liquidnet */
}

object DisplayIndicator extends EnumTag[Character](9479) {
  val Tag = this
  val DisplayedOrder = Value('X')
  val Invisible = Value('I')
}

object ModifySequence extends IntegerTag(9617) {
  val Tag = this
}

object MaxRemovePct extends IntegerTag(9618) {
  val Tag = this
}

object CancelOrigOnReject extends BooleanTag(9619) {
  val Tag = this
}

object CorrectedPrice extends FloatTag(9620) {
  val Tag = this
}

object MTFAccessFee extends FloatTag(9621) {
  val Tag = this
}

object OrigCompID extends StringTag(9688) {
  val Tag = this
}

object OrigSubID extends StringTag(9689) {
  val Tag = this
}

object TradeLiquidityIndicator extends EnumTag[String](9730) {
  val Tag = this
  val AddedLiquidity = Value("A")                         /* Added Liquidity */
  val RemovedLiquidity = Value("R")                       /* Removed Liquidity */
  val AddedLiquidityToDark = Value("AD")                  /* Added Liquidity to the BATS Dark Pool */
  val RemovedLiquidityFromDark = Value("RD")              /* Remove Liquidity from the BATS Dark Pool */
  val AddedLiquidityToDarkSelfCross = Value("AM")         /* Added Liquidity to the BATS Dark Self Cross */
  val RemovedLiquidityFromDarkSelfCross = Value("RM")     /* Removed Licensed from the BATS Dark Self Cross */
  val RoutedToAnotherMarket = Value("X")                  /* Routed to Another Market */
}
