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
import java.nio.charset.Charset
import silvertip.PartialMessageException

object MbtMessage {
  val ASCII = Charset.forName("US-ASCII")

  type Fields = Map[Tag, Value]
  type Tag    = Int
  type Type   = Char
  type Value  = String

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

  private def message(bytes: Array[Byte]): MbtMessage = {
    Message(msgType(bytes))(msgFields(bytes))
  }

  private def msgType(bytes: Array[Byte]): Type = {
    bytes(0).toChar
  }

  private def msgFields(bytes: Array[Byte]): Fields = {
    body(bytes).split(";").map(tagAndValue).toMap
  }

  private def body(bytes: Array[Byte]): String = {
    new String(bytes, 2, bytes.length - 2, ASCII).trim
  }

  private def tagAndValue(field: String): (Tag, Value) = {
    val tagAndValue = field.split("=")
    (tagAndValue(0).toInt, tagAndValue(1))
  }

  val Message = Map(
    Type.Login                   -> Login,
    Type.Subscription            -> Subscription,
    Type.Unsubscription          -> Unsubscription,
    Type.Fundamental             -> Fundamental,
    Type.Heartbeat               -> Heartbeat,
    Type.LogonAccepted           -> LogonAccepted,
    Type.LogonDenied             -> LogonDenied,
    Type.Level1Update            -> Level1Update,
    Type.Level2Update            -> Level2Update,
    Type.TasUpdate               -> TasUpdate,
    Type.FundamentalDataResponse -> FundamentalDataResponse,
    Type.OptionsChainsUpdate     -> OptionsChainsUpdate
  )

  object Type {
    val Login                   = 'L'
    val Subscription            = 'S'
    val Unsubscription          = 'U'
    val Fundamental             = 'H'
    val Heartbeat               = '9'
    val LogonAccepted           = 'G'
    val LogonDenied             = 'D'
    val Level1Update            = '1'
    val Level2Update            = '2'
    val TasUpdate               = '3'
    val FundamentalDataResponse = 'N'
    val OptionsChainsUpdate     = '4'
  }

  object Tag {
    val CompanyName      = 2021
    val ContractSize     = 2041
    val Country          = 2025
    val Currency         = 2026
    val CurrentTick      = 2013
    val Cusip            = 2028
    val Date             = 2015
    val Exchange         = 2022
    val ExpMonth         = 2036
    val ExpYear          = 2040
    val High             = 2009
    val InfoMsgFrom      = 8055
    val Isin             = 2029
    val LastAsk          = 2004
    val LastAskSize      = 2006
    val LastBid          = 2003
    val LastBidSize      = 2005
    val LastExchange     = 2042
    val Size             = 2007
    val LoginReason      = 103
    val Low              = 2010
    val MarginMultiplier = 2031
    val MmidPrice        = 2019
    val MmidSource       = 2033
    val MmidStatus       = 2024
    val MmidTime         = 2020
    val Mpid             = 2016
    val Open             = 2011
    val OpenInterest     = 2037
    val Password         = 101
    val PrevClose        = 2008
    val Price            = 2002
    val PutOrCall        = 2038
    val QuotePrice       = 2018
    val QuoteSize        = 2017
    val RequestRejected  = 1011
    val StrikePrice      = 2035
    val SubscriptionType = 2000
    val Symbol           = 1003
    val TickCondition    = 2082
    val TickSize         = 2027
    val TickStatus       = 2083
    val TickType         = 2084
    val Timestamp        = 2014
    val TotalVolume      = 2012
    val TradingStatus    = 2032
    val UpcInfo          = 2023
    val TasType          = 2039
    val Username         = 100
    val UserPermissions  = 154
  }

  object Tick {
    val Uptick   = 20020
    val Downtick = 20021
  }

  object SubscriptionType {
    val Level1          = 20000
    val Level2          = 20001
    val Level1AndLevel2 = 20002
    val Trades          = 20003
    val OptionsChains   = 20004
  }

  object TasType {
    val Normal = 30030
    val FormT  = 30031
  }

  object TickCondition {
    val RegularSale      = 0
    val FormT            = 29
    val IntermarketSweep = 45
    val ClosePrice       = 49
    val AveragePrice     = 53
  }

  object TickStatus {
    val Normal        = 0
    val Filtered      = 1
    val OutOfSequence = 2
    val Deleted       = 3
  }

  object TickType {
    val Trade = 0
    val Bid   = 1
    val Ask   = 2
  }
}

sealed trait MbtMessage {
  import MbtMessage._

  def messageType: Type

  def fields: Fields

  def set(tag: Tag, value: Value): MbtMessage

  protected def containing(tag: Tag, value: Value): Fields = {
    fields.updated(tag, value)
  }

  def getString(tag: Tag): Option[String] = {
    get(tag)
  }

  def getDouble(tag: Tag): Option[Double] = {
    getNumber(tag, _.toDouble)
  }

  def getInt(tag: Tag): Option[Int] = {
    getNumber(tag, _.toInt)
  }

  def getLong(tag: Tag): Option[Long] = {
    getNumber(tag, _.toLong)
  }

  private def getNumber[T](tag: Tag, convert: (String) => T): Option[T] = {
    try {
      get(tag).map(convert)
    } catch {
      case e: NumberFormatException => None
    }
  }

  private def get(tag: Tag): Option[String] = {
    fields.get(tag)
  }

  def format: Array[Byte] = {
    toString.getBytes(ASCII)
  }

  override def toString: String = {
    header + body + "\n"
  }

  private def header: String = {
    "%c|".format(messageType)
  }

  private def body: String = {
    fields.map { case (tag, value) =>
      "%s=%s".format(tag, value)
    }.mkString(";")
  }
}

import MbtMessage.{Fields, Tag, Value}
import MbtMessage.{Type => MessageType}
import Map.empty

case class Login(fields: Fields = empty) extends MbtMessage {
  def messageType = MessageType.Login

  def set(tag: Tag, value: Value) = Login(containing(tag, value))
}

case class Subscription(fields: Fields = empty) extends MbtMessage {
  def messageType = MessageType.Subscription

  def set(tag: Tag, value: Value) = Subscription(containing(tag, value))
}

case class Unsubscription(fields: Fields = empty) extends MbtMessage {
  def messageType = MessageType.Unsubscription

  def set(tag: Tag, value: Value) = Unsubscription(containing(tag, value))
}

case class Fundamental(fields: Fields = empty) extends MbtMessage {
  def messageType = MessageType.Fundamental

  def set(tag: Tag, value: Value) = Fundamental(containing(tag, value))
}

case class Heartbeat(fields: Fields = empty) extends MbtMessage {
  def messageType = MessageType.Heartbeat

  def set(tag: Tag, value: Value) = Heartbeat(containing(tag, value))
}

case class LogonAccepted(fields: Fields = empty) extends MbtMessage {
  def messageType = MessageType.LogonAccepted

  def set(tag: Tag, value: Value) = LogonAccepted(containing(tag, value))
}

case class LogonDenied(fields: Fields = empty) extends MbtMessage {
  def messageType = MessageType.LogonDenied

  def set(tag: Tag, value: Value) = LogonDenied(containing(tag, value))
}

case class Level1Update(fields: Fields = empty) extends MbtMessage {
  def messageType = MessageType.Level1Update

  def set(tag: Tag, value: Value) = Level1Update(containing(tag, value))
}

case class Level2Update(fields: Fields = empty) extends MbtMessage {
  def messageType = MessageType.Level2Update

  def set(tag: Tag, value: Value) = Level2Update(containing(tag, value))
}

case class TasUpdate(fields: Fields = empty) extends MbtMessage {
  def messageType = MessageType.TasUpdate

  def set(tag: Tag, value: Value) = TasUpdate(containing(tag, value))
}

case class FundamentalDataResponse(fields: Fields = empty) extends MbtMessage {
  def messageType = MessageType.FundamentalDataResponse

  def set(tag: Tag, value: Value) = FundamentalDataResponse(containing(tag, value))
}

case class OptionsChainsUpdate(fields: Fields = empty) extends MbtMessage {
  def messageType = MessageType.OptionsChainsUpdate

  def set(tag: Tag, value: Value) = OptionsChainsUpdate(containing(tag, value))
}
