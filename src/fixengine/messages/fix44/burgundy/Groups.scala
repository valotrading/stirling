package fixengine.messages.fix44.burgundy

import fixengine.messages.{FieldContainer, Required, RepeatingGroup, RepeatingGroupInstance}

trait Groups extends FieldContainer {
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
}