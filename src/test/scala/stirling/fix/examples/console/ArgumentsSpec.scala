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
package stirling.fix.examples.console

import java.util.Scanner
import org.scalatest.WordSpec
import org.scalatest.matchers.MustMatchers
import stirling.fix.examples.console.commands.CommandArgException

class ArgumentsSpec extends WordSpec with MustMatchers with ArgumentsFixtures {
  "Arguments" should {
    "return an argument value" in {
      arguments.value("argument2") must equal("value2")
    }
    "return a required argument value" in {
      arguments.requiredValue("argument2") must equal("value2")
    }
    "return a required argument integer value" in {
      arguments.requiredIntValue("argument4") must equal(100)
    }
    "return null in a value is empty" in {
      arguments.value("argument1") must equal(null)
    }
    "return null if a value is missing" in {
      arguments.value("argument3") must equal(null)
    }
    "throw an exception if a required value is empty" in {
      intercept[CommandArgException] {
        arguments.requiredValue("argument1")
      }.getMessage must equal("argument1 must be specified")
    }
    "throw an exception if a required value is missing" in {
      intercept[CommandArgException] {
        arguments.requiredValue("argument3")
      }.getMessage must equal("argument3 must be specified")
    }
    "throw an exception if a required integer value is invalid" in {
      intercept[CommandArgException] {
        arguments.requiredIntValue("argument2")
      }.getMessage must equal("argument2 must be an integer")
    }
  }
}

trait ArgumentsFixtures {
  def arguments = new Arguments(new Scanner(builder.toString))
  def builder = {
    val builder = new StringBuilder()
    builder.append("argument1= ")
    builder.append("argument2=value2 ")
    builder.append("argument3 ")
    builder.append("argument4=100 ")
    builder
  }
}
