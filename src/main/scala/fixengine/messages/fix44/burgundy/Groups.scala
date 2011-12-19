package fixengine.messages.fix44.burgundy

import fixengine.messages.{Required, RepeatingGroup, RepeatingGroupInstance}

trait Groups {
  def parties() {
    parties(Required.YES)
  }
  def parties(req: Required) {
    import fixengine.tags.fix44.PartyRole
    import fixengine.tags.fix43.{PartyIDSource, PartyID, NoPartyIDs}
    group(new RepeatingGroup(NoPartyIDs.Tag) {
      override def newInstance(): RepeatingGroupInstance =
        new RepeatingGroupInstance(PartyID.Tag) {
          field(PartyIDSource.Tag)
          field(PartyRole.Tag, Required.NO)
        }
    }, req)
  }
  protected def group(group: RepeatingGroup, required: Required): Unit
}
