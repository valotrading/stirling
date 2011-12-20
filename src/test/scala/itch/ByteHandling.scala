/*
 * Copyright 2010 the original author or authors.
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
package itch

import java.nio.ByteBuffer

trait ByteHandling {
  implicit def byteArrayToByteSeqOps(value: Array[Byte]) = new ByteSeqOps(value)
  implicit def byteSeqToByteSeqOps(value: Seq[Byte]) = new ByteSeqOps(value)
  implicit def intListToIntListOps(value: List[Int]) = new IntListOps(value)
  implicit def stringToStringOps(value: String) = new StringOps(value)
}

class ByteSeqOps(val value: Seq[Byte]) {
  def toByteBuffer = {
    ByteBuffer.wrap(value.toArray)
  }
}

class IntListOps(val value: List[Int]) {
  def toByteArray = {
    Array[Byte](toBytes: _*)
  }
  def toBytes = {
    value.map(_.asInstanceOf[Byte])
  }
}

class StringOps(val value: String) {
  def toByteArray = {
    value.getBytes
  }
  def toByteBuffer = new ByteSeqOps(toByteArray).toByteBuffer
  def toBytes = {
    List(toByteArray: _*)
  }
}
