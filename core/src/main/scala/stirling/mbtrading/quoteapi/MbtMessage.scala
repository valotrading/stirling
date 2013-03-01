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
package stirling.mbtrading.quoteapi

import java.nio.ByteBuffer
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import silvertip.PartialMessageException

object MbtMessage {
  type Tag = Int
  type Value = String
  type MessageType = Char
  def parse(buffer: ByteBuffer): MbtMessage = {
    for (pos <- buffer.position until buffer.limit) {
      if (buffer.get(pos) == '\n') {
        val bytes = new Array[Byte](pos - buffer.position + 1)
        buffer.get(bytes)
        return message(bytes)
      }
    }
    throw new PartialMessageException
  }
  private def message(bytes: Array[Byte]) = {
    msgFields(bytes).foldLeft(MbtMessage(msgType(bytes))) { case (msg, (tag, value)) =>
      msg.set(tag, value)
    }
  }
  private def msgType(bytes: Array[Byte]) = {
    bytes(0).toChar
  }
  private def msgFields(bytes: Array[Byte]) = {
    body(bytes).split(";").map(tagAndValue).toSet
  }
  private def body(bytes: Array[Byte]) = {
    new String(bytes, 2, bytes.length - 2).trim
  }
  private def tagAndValue(field: String) = {
    val tagAndValue = field.split("=")
    (tagAndValue(0).toInt, tagAndValue(1))
  }
  val dateFormat = DateTimeFormat.forPattern("MM/dd/yy")
  val timeFormat = DateTimeFormat.forPattern("HH:mm:ss")
}

import MbtMessage._

case class MbtMessage(msgType: MessageType, fields: Map[MbtMessage.Tag, MbtMessage.Value] = Map()) {
  import MbtMessageTag._
  import MbtMessageType._
  import Predef._
  def get(tag: Tag): Option[String] = {
    fields.get(tag)
  }
  def getString(tag: Tag) = {
    fields(tag)
  }
  def getDouble(tag: Tag) = {
    fields(tag).toDouble
  }
  def getInt(tag: Tag) = {
    fields(tag).toInt
  }
  def getLong(tag: Tag) = {
    fields(tag).toLong
  }
  def set(tag: Tag, value: Value): MbtMessage = {
    MbtMessage(msgType, fields + (tag -> value))
  }
  def set(tag: Tag, value: Int): MbtMessage = {
    set(tag, value.toString)
  }
  def hasValue(tag: Tag) = {
    fields.contains(tag)
  }
  def merge(other: MbtMessage) = other.msgType match {
    case this.msgType => MbtMessage(this.msgType, this.fields ++ other.fields)
    case _ => throw new IllegalArgumentException("msgType %c, expected msgType %c".format(other.msgType, this.msgType))
  }
  def format = {
    formatHeader + formatBody + "\n"
  }
  def formatHeader = {
    "%c|".format(msgType)
  }
  def formatBody = {
    fields.map { case (tag, value) =>
      "%s=%s".format(tag, value)
    }.mkString(";")
  }
  def isMarketDataMessage = {
    marketDataMessages.contains(msgType)
  }
  def level1UpdateTimestamp = {
    val (date, time) = (getString(Date), getString(Timestamp))
    dateFormat.parseDateTime(date).withFields(timeFormat.parseLocalTime(time)).getMillis
  }
  def tasUpdateTimestamp = {
    timeFormat.parseDateTime(getString(Timestamp)).getMillis
  }
}

object MbtMessageType {
  val Login = 'L'
  val Subscription = 'S'
  val Unsubscription = 'U'
  val Fundamental = 'H'
  val Heartbeat = '9'
  val LogonAccepted = 'G'
  val LogonDenied = 'D'
  val Level1Update = '1'
  val Level2Update = '2'
  val TasUpdate = '3'
  val FundamentalDataResponse = 'N'
  val OptionsChainsUpdate = '4'
  val marketDataMessages = Set(Level1Update, Level2Update, TasUpdate, FundamentalDataResponse, OptionsChainsUpdate)
}

object MbtMessageTag {
  val CompanyName = 2021
  val ContractSize = 2041
  val Country = 2025
  val Currency = 2026
  val CurrentTick = 2013
  val Cusip = 2028
  val Date = 2015
  val Exchange = 2022
  val ExpMonth = 2036
  val ExpYear = 2040
  val High = 2009
  val InfoMsgFrom = 8055
  val Isin = 2029
  val LastAsk = 2004
  val LastAskSize = 2006
  val LastBid = 2003
  val LastBidSize = 2005
  val LastExchange = 2042
  val Size = 2007
  val LoginReason = 103
  val Low = 2010
  val MarginMultiplier = 2031
  val MmidPrice = 2019
  val MmidSource = 2033
  val MmidStatus = 2024
  val MmidTime = 2020
  val Mpid = 2016
  val Open = 2011
  val OpenInterest = 2037
  val Password = 101
  val PrevClose = 2008
  val Price = 2002
  val PutOrCall = 2038
  val QuotePrice = 2018
  val QuoteSize = 2017
  val RequestRejected = 1011
  val StrikePrice = 2035
  val SubscriptionType = 2000
  val Symbol = 1003
  val TickCondition = 2082
  val TickSize = 2027
  val TickStatus = 2083
  val TickType = 2084
  val Timestamp = 2014
  val TotalVolume = 2012
  val TradingStatus = 2032
  val UpcInfo = 2023
  val TasType = 2039
  val Username = 100
  val UserPermissions = 154
}

object MbtQuoteServerTick {
  val Uptick = 20020
  val Downtick = 20021
}

object MbtQuoteServerSubscriptionType {
  val Level1 = 20000
  val Level2 = 20001
  val Level1AndLevel2 = 20002
  val Trades = 20003
  val OptionsChains = 20004
}

object MbtQuoteServerTasType {
  val Normal = 30030
  val FormT = 30031
}

object MbtQuoteServerTickCondition {
  val RegularSale = 0
  val FormT = 29
  val IntermarketSweep = 45
  val ClosePrice = 49
  val AveragePrice = 53;
}

object MbtQuoteServerTickStatus {
  val Normal = 0
  val Filtered = 1
  val OutOfSequence = 2
  val Deleted = 3
}

object MbtQuoteServerTickType {
  val Trade = 0
  val Bid = 1
  val Ask = 2
}
