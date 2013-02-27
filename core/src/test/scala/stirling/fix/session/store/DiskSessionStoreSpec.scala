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

import java.io.{File, FileWriter}
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import org.scalatest.WordSpec
import org.scalatest.mock.MockitoSugar
import org.scalatest.matchers.MustMatchers
import scala.io.Source
import scala.language.implicitConversions
import silvertip.Connection
import stirling.fix.{Config, Version}
import stirling.fix.messages.{DefaultMessageComparator, DefaultMessageVisitor, FixMessage, Message}
import stirling.fix.messages.fix42.DefaultMessageFactory
import stirling.fix.messages.fix42.MsgTypeValue.{HEARTBEAT, LOGON}
import stirling.fix.session.{HeartBtIntValue, Sequence, Session}
import stirling.fix.tags.fix42.{EncryptMethod, HeartBtInt}

class DiskSessionStoreSpec extends WordSpec with MustMatchers with MockitoSugar with DiskSessionStoreFixtures {
  "DiskSessionStore" when {
    "loading non-existing file" must {
      file.delete
      val session = newSession
      "not alter incoming sequence number" in {
        session.getIncomingSeq.peek must equal(1)
      }
      "not alter outgoing sequence number" in {
        session.getOutgoingSeq.peek must equal(1)
      }
    }
    "loading existing file" must {
      writeFile(5, 3)
      val session = newSession
      "load incoming sequence number" in {
        session.getIncomingSeq.peek must equal(5)
      }
      "load outgoing sequence number" in {
        session.getOutgoingSeq.peek must equal(3)
      }
    }
    "saving" must {
      file.delete
      val session = newSession
      val connection = mock[Connection[FixMessage]]
      session.logon(connection)
      session.receive(connection, newLogon, newMessageVisitor)
      session.receive(connection, newHeartbeat, newMessageVisitor)
      val (incomingSeq, outgoingSeq) = readFile
      "save incoming sequence number" in {
        incomingSeq must equal(3)
      }
      "save outgoing sequence number" in {
        outgoingSeq must equal(2)
      }
    }
  }
}

trait DiskSessionStoreFixtures {
  val path = {
    val file = File.createTempFile("DiskSessionStoreSpec", "")
    file.deleteOnExit
    file.getPath + "0"
  }
  def newConfig = new Config {
    setVersion(Version.FIX_4_2)
    setSenderCompId(senderCompId)
    setTargetCompId(targetCompId)
  }
  def newHeartBtIntValue = HeartBtIntValue.seconds(30)
  def newLogon = newMessage(LOGON, 1) { message =>
    message.setInteger(HeartBtInt.Tag, 30)
    message.setEnum(EncryptMethod.Tag, EncryptMethod.None)
  }
  def newHeartbeat = newMessage(HEARTBEAT, 2) { message => }
  def newMessageComparator = new DefaultMessageComparator
  def newMessageFactory = new DefaultMessageFactory
  def newMessageVisitor = new DefaultMessageVisitor
  def newSession = new Session(newHeartBtIntValue, newConfig, newSessionStore, newMessageFactory, newMessageComparator)
  def newSessionStore = new DiskSessionStore(path)
  def senderCompId = "INITIATOR"
  def targetCompId = "ACCEPTOR"
  def date = ISODateTimeFormat.date.print(new DateTime(System.currentTimeMillis))
  def directory = new File(path, "%s-%s".format(senderCompId, targetCompId))
  def file = new File(directory, "%s".format(date))
  def readFile = {
    val lines = Source.fromFile(file).getLines
    val incomingSeqNo = lines.next.toInt
    val outgoingSeqNo = lines.next.toInt
    (incomingSeqNo, outgoingSeqNo)
  }
  def writeFile(incomingSeqNo: Int, outgoingSeqNo: Int) {
    directory.mkdirs
    val writer = new FileWriter(file)
    writer.write("%d\n%d\n".format(incomingSeqNo, outgoingSeqNo))
    writer.close
  }
  implicit def intToSeq(seqNo: Int): Sequence = {
    val seq = new Sequence
    seq.reset(seqNo)
    seq
  }
  def newMessage(msgType: String, msgSeqNum: Int)(filler: (Message) => Unit) = {
    val message = newMessageFactory.create(msgType)
    message.setHeaderConfig(new Config {
      setVersion(Version.FIX_4_2)
      setSenderCompId(targetCompId)
      setTargetCompId(senderCompId)
    })
    message.setMsgSeqNum(msgSeqNum)
    message.setSendingTime(new DateTime)
    filler(message)
    FixMessage.fromString(message.format, message.getMsgType)
  }
}
