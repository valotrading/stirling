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
package stirling.fix.messages.fix42.samrat

import stirling.fix.messages.{
  AbstractMessage,
  Logon => LogonTrait,
  MessageHeader,
  MessageVisitor,
  NewOrderSingle => NewOrderSingleTrait,
  OrderCancelReplaceRequest => OrderCancelReplaceRequestTrait,
  OrderCancelRequest => OrderCancelRequestTrait,
  Reject => RejectTrait,
  Required
}
import stirling.fix.tags.fix42.{
  ClOrdID,
  DiscretionOffset,
  EncryptMethod,
  ExpireTime,
  HeartBtInt,
  MaxFloor,
  MinQty,
  OrdType,
  OrderID,
  OrderQty,
  OrigClOrdID,
  PegDifference,
  Price,
  RefMsgType,
  RefSeqNo,
  RefTagId,
  SecurityID,
  Symbol,
  SymbolSfx,
  Text
}
import stirling.fix.tags.fix42.samrat.{
  AllowRouting,
  AlternateExDestination,
  CancelAllOnDisconnect,
  CancelAllOpen,
  ISO,
  Invisible,
  LockedOrCrossedAction,
  LongSaleAffirm,
  PegType,
  PostOnly,
  RegularSessionOnly,
  RouteToNYSE,
  ShortSaleAffirm,
  ShortSaleAffirmLongQuantity,
  Side,
  TimeInForce
}
import stirling.fix.tags.fix43.{
  SecurityIDSource,
  SessionRejectReason
}
import stirling.fix.tags.fix44.{
  ParticipationRate,
  Password,
  Username
}

class Logon(header: MessageHeader) extends AbstractMessage(header) with LogonTrait {
  field(EncryptMethod.Tag)
  field(HeartBtInt.Tag)
  field(Username.Tag)
  field(Password.Tag, Required.NO)
  field(CancelAllOnDisconnect.Tag, Required.NO)
  def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class Reject(header: MessageHeader) extends AbstractMessage(header) with RejectTrait {
  field(RefSeqNo.Tag)
  field(Text.Tag, Required.NO)
  field(RefTagId.Tag, Required.NO)
  field(RefMsgType.Tag, Required.NO)
  field(SessionRejectReason.Tag, Required.NO)
  def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class NewOrderSingle(header: MessageHeader) extends AbstractMessage(header) with NewOrderSingleTrait {
  field(ClOrdID.Tag)
  field(Symbol.Tag)
  field(SymbolSfx.Tag, Required.NO)
  field(SecurityID.Tag, Required.NO)
  field(SecurityIDSource.Tag, Required.NO)
  field(Side.Tag)
  field(OrderQty.Tag)
  field(OrdType.Tag)
  field(Price.Tag, Required.NO)
  field(TimeInForce.Tag, Required.NO)
  field(MinQty.Tag, Required.NO)
  field(MaxFloor.Tag, Required.NO)
  field(ExpireTime.Tag, Required.NO)
  field(PegDifference.Tag, Required.NO)
  field(DiscretionOffset.Tag, Required.NO)
  field(Invisible.Tag, Required.NO)
  field(PostOnly.Tag, Required.NO)
  field(ShortSaleAffirm.Tag, Required.NO)
  field(ShortSaleAffirmLongQuantity.Tag, Required.NO)
  field(LongSaleAffirm.Tag, Required.NO)
  field(AllowRouting.Tag, Required.NO)
  field(AlternateExDestination.Tag, Required.NO)
  field(RouteToNYSE.Tag, Required.NO)
  field(ISO.Tag, Required.NO)
  field(PegType.Tag, Required.NO)
  field(LockedOrCrossedAction.Tag, Required.NO)
  field(RegularSessionOnly.Tag, Required.NO)
  def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class OrderCancelRequest(header: MessageHeader) extends AbstractMessage(header) with OrderCancelRequestTrait {
  field(ClOrdID.Tag)
  field(OrderID.Tag, Required.NO)
  field(OrigClOrdID.Tag)
  field(CancelAllOpen.Tag, Required.NO)
  def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class OrderCancelReplaceRequest(header: MessageHeader) extends AbstractMessage(header) with OrderCancelReplaceRequestTrait {
  field(OrdType.Tag, Required.NO)
  field(OrigClOrdID.Tag)
  field(OrderID.Tag, Required.NO)
  field(ClOrdID.Tag)
  field(OrderQty.Tag, Required.NO)
  field(Price.Tag, Required.NO)
  field(ParticipationRate.Tag, Required.NO)
  def apply(visitor: MessageVisitor) = visitor.visit(this)
}
