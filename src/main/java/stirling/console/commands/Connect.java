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
package stirling.console.commands;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import stirling.console.Arguments;
import stirling.console.ConsoleClient;
import stirling.fix.messages.DefaultMessageComparator;
import stirling.fix.messages.DefaultMessageVisitor;
import stirling.fix.messages.FixMessage;
import stirling.fix.messages.FixMessageParser;
import stirling.fix.messages.fix42.MsgTypeValue;
import stirling.fix.session.HeartBtIntValue;
import stirling.fix.session.Session;
import stirling.fix.tags.fix42.ClOrdID;
import stirling.fix.tags.fix42.OrderID;
import silvertip.Connection;

public class Connect implements Command {
  private static final Logger logger = Logger.getLogger("ConsoleClient");
  private static final String ARGUMENT_PORT = "Port";
  private static final String ARGUMENT_HOST = "Host";

  static {
    logger.setUseParentHandlers(false);
    try {
      FileHandler stirlingLog = new FileHandler("stirling.log");
      stirlingLog.setFormatter(new SimpleFormatter());
      logger.addHandler(stirlingLog);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void execute(final ConsoleClient client, Scanner scanner) throws CommandException {
    Arguments arguments = new Arguments(scanner);
    try {
      Connection conn = Connection.connect(new InetSocketAddress(host(arguments), port(arguments)),
          new FixMessageParser(), new Connection.Callback<FixMessage>() {
          @Override public void messages(Connection<FixMessage> conn, Iterator<FixMessage> messages) {
            while (messages.hasNext()) {
              FixMessage msg = messages.next();
              client.getSession().receive(conn, msg, new DefaultMessageVisitor() {
                @Override public void defaultAction(stirling.fix.messages.Message message) {
                  if (message.getMsgType().equals(MsgTypeValue.EXECUTION_REPORT))
                    client.setOrderID(message.getString(ClOrdID.Tag()), message.getString(OrderID.Tag()));
                  logger.info(message.toString());
                }
              });
            }
          }

          @Override public void idle(Connection<FixMessage> conn) {
            client.getSession().keepAlive(conn);
          }

          @Override public void closed(Connection<FixMessage> conn) {
          }

          @Override public void garbledMessage(String message, byte[] data) {
          }
        });
      client.setConnection(conn);
      Session session = new Session(getHeartBtInt(), client.getConfig(), client.getSessionStore(), client.getMessageFactory(), new DefaultMessageComparator()) {
        @Override
        protected boolean checkSeqResetSeqNum() {
          /* Do not verify that the sequence numbers of SeqReset messages as
           * test scenarios of OpenFIX certification sets MsgSeqNum to 1
           * despite the value of GapFillFlag.
           */
          return false;
        }
      };
      client.setSession(session);
      client.getEvents().register(conn);
    } catch (ConnectException e) {
      client.error("Unable to connect: " + e.getMessage());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private HeartBtIntValue getHeartBtInt() {
    return HeartBtIntValue.seconds(30);
  }

  private InetAddress host(Arguments arguments) throws CommandException {
    try {
      return InetAddress.getByName(arguments.requiredValue(ARGUMENT_HOST));
    } catch (UnknownHostException e) {
      throw new CommandException("unknown hostname");
    }
  }

  private int port(Arguments arguments) throws CommandArgException {
    return arguments.requiredIntValue(ARGUMENT_PORT);
  }

  public String[] getArgumentNames(ConsoleClient client) {
    return new String[] { ARGUMENT_HOST + "=", ARGUMENT_PORT + "=" };
  }

  public String description() {
    return "Opens connection which will be used for sending messages.";
  }

  public String usage() {
    return ARGUMENT_HOST + "=<host> " + ARGUMENT_PORT + "=<port> : " + description();
  }
}
