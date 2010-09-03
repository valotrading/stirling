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
package fixengine.examples.console.commands;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Scanner;

import fixengine.Config;
import fixengine.Version;
import fixengine.session.Session;
import fixengine.session.HeartBtIntValue;

import fixengine.examples.console.ConsoleClient;

import fixengine.messages.DefaultMessageVisitor;
import fixengine.messages.MsgTypeValue;

import fixengine.tags.ClOrdID;
import fixengine.tags.OrderID;

import silvertip.Connection;
import silvertip.Message;
import silvertip.protocols.FixMessageParser;

import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 * @author Karim Osman
 */
public class Logon implements Command {
  private static final Logger logger = Logger.getLogger("ConsoleClient");

  static {
    logger.setUseParentHandlers(false);
    try {
      logger.addHandler(new FileHandler("fixengine.log"));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void execute(final ConsoleClient client, Scanner scanner) throws CommandArgException {
    try {
      Connection conn = Connection.connect(new InetSocketAddress(host(scanner), port(scanner)),
          new FixMessageParser(), new Connection.Callback() {
          @Override public void messages(Connection conn, Iterator<Message> messages) {
            while (messages.hasNext()) {
              Message msg = messages.next();
              client.getSession().receive(conn, msg, new DefaultMessageVisitor() {
                @Override public void defaultAction(fixengine.messages.Message message) {
                  if (message.getMsgType().equals(MsgTypeValue.EXECUTION_REPORT.value()))
                    client.setOrderID(message.getString(ClOrdID.TAG), message.getString(OrderID.TAG));
                  logger.info(message.toString());
                }
              });
            }
          }

          @Override public void idle(Connection conn) {
            client.getSession().keepAlive(conn);
          }

          @Override public void closed(Connection conn) {
          }
        });
      client.setConnection(conn);
      Session session = new Session(getHeartBtInt(), client.getConfig(), client.getSessionStore(), client.getMessageFactory()) {
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
      session.logon(conn);
      client.getEvents().register(conn);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private HeartBtIntValue getHeartBtInt() {
    return new HeartBtIntValue(30);
  }

  private InetAddress host(Scanner scanner) throws CommandArgException {
    if (!scanner.hasNext())
      throw new CommandArgException("hostname must be specified");
    try {
      return InetAddress.getByName(scanner.next());
    } catch (UnknownHostException e) {
      throw new CommandArgException("unknown hostname");
    }
  }

  private int port(Scanner scanner) throws CommandArgException {
    if (!scanner.hasNext())
      throw new CommandArgException("port must be specified");
    try {
      return Integer.parseInt(scanner.next());
    } catch (NumberFormatException e) {
      throw new CommandArgException("invalid port");
    }
  }
}
