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
package fixengine.examples.console;

import fixengine.examples.console.commands.CommandArgException;
import jdave.Block;
import jdave.Specification;
import jdave.junit4.JDaveRunner;
import org.junit.runner.RunWith;

import java.util.Scanner;

@RunWith(JDaveRunner.class)
public class ArgumentsSpec extends Specification<Arguments> {
  public class AnyArguments {
    private Arguments arguments;

    public Arguments create() {
      StringBuilder builder = new StringBuilder();
      builder.append("argument1= ");
      builder.append("argument2=value2 ");
      builder.append("argument3 ");
      builder.append("argument4=100 ");
      return arguments = new Arguments(new Scanner(builder.toString()));
    }

    public void returnsArgumentAndValue() {
      specify(arguments.value("argument2"), must.equal("value2"));
    }

    public void returnsRequiredArgumentAndValue() throws CommandArgException {
      specify(arguments.requiredValue("argument2"), must.equal("value2"));
    }

    public void returnsRequiredArgumentAndIntValue() throws CommandArgException {
      specify(arguments.requiredIntValue("argument4"), must.equal(100));
    }

    public void returnsNullIfValueEmpty() {
      specify(arguments.value("argument1"), must.equal(null));
    }

    public void returnsNullIfNoValue() {
      specify(arguments.value("argument3"), must.equal(null));
    }

    public void throwsExceptionIfRequiredValueEmpty() {
      specify(new Block() {
        @Override
        public void run() throws Exception {
          arguments.requiredValue("argument1");
        }
      }, must.raise(CommandArgException.class, "argument1 must be specified"));
    }

    public void throwsExceptionIfNoRequiredValue() {
      specify(new Block() {
        @Override
        public void run() throws Exception {
          arguments.requiredValue("argument3");
        }
      }, must.raise(CommandArgException.class, "argument3 must be specified"));
    }

    public void throwsExceptionIfRequiredIntValueInvalid() {
      specify(new Block() {
        @Override
        public void run() throws Exception {
          arguments.requiredIntValue("argument2");
        }
      }, must.raise(CommandArgException.class, "argument2 must be an integer"));
    }
  }
}
