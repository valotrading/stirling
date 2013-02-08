/*
 * Copyright 2011 the original author or authors.
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
package stirling.fix.console

import java.util.ArrayList
import org.mockito.Mockito._
import org.scalatest.WordSpec
import org.scalatest.matchers.MustMatchers
import org.scalatest.mock.MockitoSugar
import scala.collection.JavaConversions._
import stirling.fix.console.commands.Command

class CommandCompletorSpec extends WordSpec with MustMatchers with CommandCompletorFixtures {
  "CommandCompletor" should {
    "display all commands in ascending order" in {
      candidates("") must equal(List("start", "stop"))
    }
    "display only matching commands" in {
      candidates("sta") must equal(List("start "))
    }
    "display all arguments for a command" in {
      candidates("start ").toSet must equal(Set("now", "later", "never"))
    }
    "display only matching arguments for a command" in {
      candidates("start n").toSet must equal(Set("now", "never"))
    }
    "display all arguments for a command multiple times" in {
      candidates("start now=true ").toSet must equal(Set("now", "later", "never"))
    }
  }
}

trait CommandCompletorFixtures extends MockitoSugar {
  def candidates(buffer: String) = {
    val candidates = new ArrayList[String]()
    completor.complete(buffer, buffer.length, candidates)
    List(candidates: _*)
  }
  def commands = Map("start" -> startCommand, "stop" -> stopCommand)
  def completor = new CommandCompletor(consoleClient, commands)
  def consoleClient = null

  def startCommand = {
    val command = mock[Command]
    when(command.getArgumentNames(null)).thenReturn(startCommandArguments.toArray)
    command
  }
  def startCommandArguments = List("now", "later", "never")
  def stopCommand = mock[Command]
}
