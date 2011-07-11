package fixengine.messages.fix44.mbtrading

import fixengine.messages.{
  AbstractMessage,
  ExecutionReport => ExecutionReportTrait,
  MessageHeader,
  MessageVisitor,
  RepeatingGroup,
  RepeatingGroupInstance,
  Required
}
import fixengine.tags.fix42.{
  Account,
  AvgPx,
  ClOrdID,
  ClientID,
  Commission,
  ComplianceID,
  CumQty,
  DiscretionInst,
  DiscretionOffset,
  EffectiveTime,
  ExDestination,
  ExecID,
  ExecInst,
  ExpireTime,
  LastPx,
  LastShares,
  LeavesQty,
  MaxFloor,
  OrdRejReason,
  OrdStatus,
  OrdType,
  OrderID,
  OrderQty,
  OrigClOrdID,
  Price,
  SecondaryOrderID,
  SecurityType,
  Side,
  StopPx,
  Symbol,
  Text,
  TimeInForce,
  TransactTime,
  UnderlyingSymbol
}
import fixengine.tags.fix43.{
  LegPrice,
  LegProduct,
  LegRefID,
  LegRatioQty,
  LegStrikePrice,
  LegSymbol,
  LegSide,
  MassStatusReqID,
  MultiLegReportingType,
  NoLegs,
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
  ExecType,
  LastRptRequested
}
class ExecutionReport(header: MessageHeader) extends AbstractMessage(header) with ExecutionReportTrait {
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
  field(Side.Tag)
  field(Symbol.Tag)
  field(Text.Tag, Required.NO)
  field(TimeInForce.Tag, Required.NO)
  field(TransactTime.Tag, Required.NO)
  field(PositionEffect.Tag, Required.NO)
  field(StopPx.Tag, Required.NO)
  field(ExDestination.Tag, Required.NO)
  field(OrdRejReason.Tag, Required.NO)
  field(ClientID.Tag)
  field(MaxFloor.Tag, Required.NO)
  field(ExpireTime.Tag, Required.NO)
  field(ExecType.Tag)
  field(LeavesQty.Tag)
  field(SecurityType.Tag, Required.NO)
  field(EffectiveTime.Tag, Required.NO)
  field(SecondaryOrderID.Tag, Required.NO)
  field(UnderlyingSymbol.Tag, Required.NO)
  field(ComplianceID.Tag, Required.NO)
  field(DiscretionInst.Tag, Required.NO)
  field(DiscretionOffset.Tag, Required.NO)
  field(MultiLegReportingType.Tag, Required.NO)
  field(Product.Tag, Required.NO)
  field(OrderRestrictions.Tag, Required.NO)
  field(MassStatusReqID.Tag, Required.NO)
  group(new RepeatingGroup(NoLegs.Tag) {
    override def newInstance:RepeatingGroupInstance = {
      return new RepeatingGroupInstance(LegPrice.Tag) {
        field(LegSymbol.Tag, Required.NO)
        field(LegProduct.Tag, Required.NO)
        field(LegStrikePrice.Tag, Required.NO)
        field(LegRatioQty.Tag, Required.NO)
        field(LegSide.Tag, Required.NO)
        field(LegRefID.Tag, Required.NO)
      }
    }
  }, Required.NO)
  field(LastRptRequested.Tag, Required.NO)
  field(LiquidityTag.Tag, Required.NO)
  field(PosRealizedPNL.Tag, Required.NO)
  field(OrderGroupID1.Tag, Required.NO)
  field(Price2.Tag, Required.NO)
  field(MBTInternalOrderId.Tag, Required.NO)
  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}
