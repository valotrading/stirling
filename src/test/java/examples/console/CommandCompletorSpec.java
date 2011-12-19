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
package examples.console;

import fixengine.examples.console.CommandCompletor;
import fixengine.examples.console.commands.Command;
import jdave.Specification;
import jdave.junit4.JDaveRunner;
import org.jmock.Expectations;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(JDaveRunner.class)
public class CommandCompletorSpec extends Specification<CommandCompletor> {
  public class AnyArguments {
    private CommandCompletor completor;
    private Command startCommand;
    private Command stopCommand;
    public List<String> candidates = new ArrayList<String>();

    public CommandCompletor create() {
      Map<String, Command> commands = new HashMap<String, Command>();
      commands.put("start", startCommand = mock(Command.class, "start"));
      commands.put("stop", stopCommand = mock(Command.class, "stop"));
      return completor = new CommandCompletor(null, commands);
    }

    public void displaysAllCommandsInAscendingOrder() {
      specify(candidates(""), must.containExactly("start", "stop"));
    }

    public void displaysOnlyMatchingCommand() {
      specify(candidates("sta"), must.containExactly("start "));
    }

    public void displaysAllArgumentForCommand() {
      checking(new Expectations() {{
        atLeast(1).of(startCommand).getArgumentNames(null); will(returnValue(new String[]{"now", "later", "never"}));
      }});
      specify(candidates("start "), must.containExactly("now", "later", "never"));
    }

    public void displaysMatchingArgumentForCommand() {
      checking(new Expectations() {{
        atLeast(1).of(startCommand).getArgumentNames(null); will(returnValue(new String[]{"now", "later", "never"}));
      }});
      specify(candidates("start n"), must.containExactly("now", "never"));
    }

    public void displaysAllArgumentForCommandMultipleTimes() {
      checking(new Expectations() {{
        atLeast(1).of(startCommand).getArgumentNames(null); will(returnValue(new String[]{"now", "later", "never"}));
      }});
      specify(candidates("start now=true "), must.containExactly("now", "later", "never"));
    }

    private List<String> candidates(String buffer) {
      completor.complete(buffer, buffer.length(), candidates);
      return candidates;
    }
  }
}
