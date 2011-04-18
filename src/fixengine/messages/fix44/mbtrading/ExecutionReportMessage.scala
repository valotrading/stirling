package fixengine.messages.fix44.mbtrading

import fixengine.messages.{
  AbstractMessage,
  ExecutionReportMessage => ExecutionReportMessageTrait,
  MessageHeader,
  MessageVisitor,
  Required
}
import fixengine.tags.fix42.{
  Account,
  AvgPx,
  ClOrdID,
  Commission,
  CumQty,
  DiscretionInst,
  DiscretionOffset,
  EffectiveTime,
  ExDestination,
  ExecID,
  ExecInst,
  ExecType,
  ExpireTime,
  LastPx,
  LastShares,
  LeavesQty,
  MaxFloor,
  MsgType,
  OrdStatus,
  OrdType,
  OrderID,
  OrderQty,
  OrigClOrdID,
  Price,
  Side,
  SecondaryOrderID,
  StopPx,
  Symbol,
  Text,
  TimeInForce,
  TransactTime
}
import fixengine.tags.fix43.{
  LegPrice,
  LegRefID,
  MassStatusReqID,
  MultiLegReportingType,
  OrderRestrictions,
  PositionEffect,
  Price2,
  Product
}
import fixengine.tags.fix44.mbtrading.{
  LiquidityTag,
  MBTInternalOrderId,
  OrderGroupID1,
  PosRealizedPNL
}
import fixengine.tags.fix44.{
  LastRptRequested
}
class ExecutionReportMessage(header: MessageHeader) extends AbstractMessage(header) with ExecutionReportMessageTrait {
  field(MsgType.Tag)
  field(Account.Tag)
  field(AvgPx.Tag)
  field(ClOrdID.Tag)
  field(Commission.Tag, Required.NO)
  field(CumQty.Tag)
  field(ExecID.Tag)
  field(ExecInst.Tag, Required.NO)
  field(LastPx.Tag, Required.NO)
  field(LastShares.Tag, Required.NO)
  field(OrderID.Tag)
  field(OrderQty.Tag, Required.NO)
  field(OrdStatus.Tag)
  field(OrdType.Tag, Required.NO)
  field(OrigClOrdID.Tag, Required.NO)
  field(Price.Tag, Required.NO)
  field(Symbol.Tag)
  field(Side.Tag)
  field(Text.Tag, Required.NO)
  field(TimeInForce.Tag, Required.NO)
  field(TransactTime.Tag, Required.NO)
  field(PositionEffect.Tag, Required.NO)
  field(StopPx.Tag, Required.NO)
  field(ExDestination.Tag, Required.NO)
  field(MaxFloor.Tag, Required.NO)
  field(ExpireTime.Tag, Required.NO)
  field(ExecType.Tag)
  field(LeavesQty.Tag)
  field(EffectiveTime.Tag, Required.NO)
  field(SecondaryOrderID.Tag, Required.NO)
  field(DiscretionInst.Tag, Required.NO)
  field(DiscretionOffset.Tag, Required.NO)
  field(MultiLegReportingType.Tag, Required.NO)
  field(Product.Tag, Required.NO)
  field(OrderRestrictions.Tag, Required.NO)
  field(LegPrice.Tag, Required.NO)
  field(MassStatusReqID.Tag, Required.NO)
  field(Price2.Tag, Required.NO)
  field(LegRefID.Tag, Required.NO)
  field(LastRptRequested.Tag, Required.NO)
  field(LiquidityTag.Tag, Required.NO)
  field(PosRealizedPNL.Tag, Required.NO)
  field(MBTInternalOrderId.Tag, Required.NO)
  field(OrderGroupID1.Tag, Required.NO)

  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}
