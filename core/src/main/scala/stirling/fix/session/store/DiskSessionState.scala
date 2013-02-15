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

import java.io.{File, FileWriter, IOException}
import scala.io.Source
import stirling.fix.session.{Sequence, Session}

object DiskSessionState {
  def read(file: File) = {
    parse(Source.fromFile(file).getLines)
  }
  private def parse(lines: Iterator[String]) = try {
    new DiskSessionState(lines.next.toInt, lines.next.toInt)
  } catch {
    case e: Exception => throw new IOException("Cannot read session state from disk", e)
  }
}

class DiskSessionState(val incomingSeqNo: Int, val outgoingSeqNo: Int) {
  def this(session: Session) = this(session.getIncomingSeq.peek, session.getOutgoingSeq.peek)
  def incomingSeq = asSeq(incomingSeqNo)
  def outgoingSeq = asSeq(outgoingSeqNo)
  def write(file: File) {
    val writer = new FileWriter(file)
    try {
      writer.write(format)
    } finally {
      writer.close
    }
  }
  private def format = {
    "%d\n%d\n".format(incomingSeqNo, outgoingSeqNo)
  }
  private def asSeq(seqNo: Int) = {
    val seq = new Sequence
    seq.reset(seqNo)
    seq
  }
}
