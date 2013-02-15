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
package stirling.fix.session.store

import java.io.{File, IOException}
import org.joda.time.format.ISODateTimeFormat
import scala.collection.JavaConversions._
import stirling.fix.messages.Message
import stirling.fix.session.{Sequence, Session}

class DiskSessionStore(val path: String) extends SessionStore {
  def save(session: Session) {
    save(session, new DiskSessionState(session))
  }
  def saveOutgoingMessage(session: Session, message: Message) = Unit
  def saveIncomingMessage(session: Session, message: Message) = Unit
  def load(session: Session) {
    val file = fileFor(session)
    if (file.exists) {
      val state = DiskSessionState.read(file)
      session.setIncomingSeq(state.incomingSeq)
      session.setOutgoingSeq(state.outgoingSeq)
    }
  }
  def getOutgoingMessages(session: Session, beginSeqNo: Int, endSeqNo: Int) = List()
  def getIncomingMessages(session: Session, beginSeqNo: Int, endSeqNo: Int) = List()
  def resetOutgoingSeq(session: Session, incomingSeq: Sequence, outgoingSeq: Sequence) {
    save(session, new DiskSessionState(incomingSeq.peek, outgoingSeq.peek))
  }
  def clear(senderCompId: String, targetCompId: String) {
    Option.apply(directoryFor(senderCompId, targetCompId).listFiles).foreach(_.foreach(_.delete))
  }
  def isDuplicate(session: Session, message: Message) = false
  def close = Unit
  private def save(session: Session, state: DiskSessionState) {
    val directory = directoryFor(session)
    if (!directory.exists && !directory.mkdirs)
        throw new IOException("Unable to create directory: " + directory)
    state.write(fileFor(session))
  }
  private def directoryFor(session: Session): File = {
    directoryFor(senderCompIdIn(session), targetCompIdIn(session))
  }
  private def directoryFor(senderCompId: String, targetCompId: String): File = {
    new File(path, "%s-%s".format(senderCompId, targetCompId))
  }
  private def fileFor(session: Session) = {
    new File(directoryFor(session), dateOf(session))
  }
  private def dateOf(session: Session) = {
    ISODateTimeFormat.date.print(session.currentTime)
  }
  private def senderCompIdIn(session: Session) = session.getConfig.getSenderCompId
  private def targetCompIdIn(session: Session) = session.getConfig.getTargetCompId
}
