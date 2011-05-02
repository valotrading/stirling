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

import java.lang.reflect.ParameterizedType;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import fixengine.examples.console.ConsoleClient;
import fixengine.messages.AbstractField;
import fixengine.messages.EnumField;
import fixengine.messages.Field;
import fixengine.messages.FloatField;
import fixengine.messages.IntegerField;
import fixengine.messages.Message;
import fixengine.messages.MessageFactory;
import fixengine.messages.StringField;
import fixengine.messages.Tag;
import fixengine.tags.fix42.OrderID;
import fixengine.tags.fix42.OrigClOrdID;

public abstract class FixMessageCommand implements Command {
  private static final Set<Class<? extends Parser>> parserClasses = new HashSet<Class<? extends Parser>>();

  {
    parserClasses.add(IntegerFieldParser.class);
    parserClasses.add(StringFieldParser.class);
    parserClasses.add(FloatFieldParser.class);
    parserClasses.add(EnumFieldParser.class);
  }

  public void execute(ConsoleClient client, Scanner scanner) throws CommandArgException {
    try {
      Message message = newMessage(client);
      setFields(message, scanner, client.getMessageFactory());
      if (client.getSession() != null)
        client.getSession().send(client.getConnection(), message);
      if (isModifyingOrderMessage())
        setMessageOrderID(message, client);
    } catch (Exception e) {
      throw new CommandArgException("failed to set field: " + e.getMessage());
    }
  }

  private void setMessageOrderID(Message msg, ConsoleClient client) {
    String orderID = client.findOrderID(origClientOrderID(msg));
    if (orderID == null)
      throw new RuntimeException("cannot find OrderID for OrigClOrdID: " + origClientOrderID(msg));
    msg.setString(OrderID.Tag(), orderID);
  }

  private String origClientOrderID(Message msg) {
    return msg.getString(OrigClOrdID.Tag());
  }

  protected abstract Message newMessage(ConsoleClient client);

  protected abstract boolean isModifyingOrderMessage();

  private void setFields(Message message, Scanner scanner, MessageFactory messageFactory) {
    while (scanner.hasNext()) {
      String field = scanner.next();
      findParserForField(field, messageFactory).setField(message, field);
    }
  }

  private Parser findParserForField(String field, MessageFactory messageFactory) {
    for (Class<? extends Parser> parserClass : parserClasses) {
      Parser parser = newParser(parserClass, messageFactory);
      if (parser.matches(field))
        return parser;
    }
    throw new RuntimeException("cannot parse field: " + field);
  }

  private Parser newParser(Class<? extends Parser> clazz, MessageFactory messageFactory) {
    try {
      return clazz.getDeclaredConstructor(MessageFactory.class).newInstance(messageFactory);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private interface Parser {
    boolean matches(String field);
    void setField(Message msg, String field);
  }

  private static abstract class AbstractFieldParser implements Parser {
    protected final MessageFactory messageFactory;

    protected AbstractFieldParser(MessageFactory messageFactory) {
      this.messageFactory = messageFactory;
    }

    @Override public boolean matches(String field) {
      if (hasTypeArg(field))
        return typeArg(field).equals(getFieldClass());
      return false;
    }

    @SuppressWarnings("unchecked") protected Class<? extends Tag> tagClass(String field) {
      return messageFactory.createTag(tag(field)).getClass();
    }

    protected abstract Class<? extends Field> getFieldClass();

    protected boolean hasTypeArg(String field) {
      ParameterizedType type = (ParameterizedType) tagClass(field).getSuperclass().getGenericSuperclass();
      return type.getActualTypeArguments().length > 0;
    }

    protected Class<?> typeArg(String field) {
      return typeArg(tagClass(field));
    }

    protected Class<?> typeArg(Class<?> clazz) {
      ParameterizedType type = (ParameterizedType) clazz.getSuperclass().getGenericSuperclass();
      return (Class<?>) type.getActualTypeArguments()[0];
    }

    protected String tag(String field) {
      return tagAndValue(field)[0];
    }

    protected String value(String field) {
      return tagAndValue(field)[1];
    }

    private String[] tagAndValue(String field) {
      String[] tagAndValue = field.split("=");
      if (tagAndValue.length != 2)
        throw new RuntimeException("invalid field: " + field);
      return tagAndValue;
    }
  }

  private static class StringFieldParser extends AbstractFieldParser {
    public StringFieldParser(MessageFactory messageFactory) {
      super(messageFactory);
    }

    @Override @SuppressWarnings("unchecked") public void setField(Message msg, String field) {
      try {
        AbstractField<String> f = (AbstractField<String>) msg.lookup(messageFactory.createTag(tag(field)));
        if (f != null)
          f.setValue(value(field));
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }

    @Override protected Class<? extends Field> getFieldClass() {
      return StringField.class;
    }
  }

  private static class FloatFieldParser extends AbstractFieldParser {
    public FloatFieldParser(MessageFactory messageFactory) {
      super(messageFactory);
    }

    @Override @SuppressWarnings("unchecked") public void setField(Message msg, String field) {
      try {
        AbstractField<Double> f = (AbstractField<Double>) msg.lookup(tagClass(field).newInstance());
        f.setValue(Double.valueOf(value(field)));
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }

    @Override protected Class<? extends Field> getFieldClass() {
      return FloatField.class;
    }
  }

  private static class IntegerFieldParser extends AbstractFieldParser {
    public IntegerFieldParser(MessageFactory messageFactory) {
      super(messageFactory);
    }

    @Override @SuppressWarnings("unchecked") public void setField(Message msg, String field) {
      try {
        AbstractField<Integer> f = (AbstractField<Integer>) msg.lookup(tagClass(field).newInstance());
        f.setValue(Integer.valueOf(value(field)));
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }

    @Override protected Class<? extends Field> getFieldClass() {
      return IntegerField.class;
    }
  }

  private static class EnumFieldParser extends AbstractFieldParser {
    public EnumFieldParser(MessageFactory messageFactory) {
      super(messageFactory);
    }

    @Override public boolean matches(String field) {
      if (hasTypeArg(field))
        return typeArg(field).getSuperclass().equals(EnumField.class);
      return false;
    }

    @Override @SuppressWarnings("unchecked") public void setField(Message msg, String field) {
      try {
        AbstractField<Enum> f = (AbstractField<Enum>) msg.lookup(tagClass(field).newInstance());
        f.setValue(Enum.valueOf(enumType(field), value(field)));
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }

    @Override protected Class<? extends Field> getFieldClass() {
      return EnumField.class;
    }

    @SuppressWarnings("unchecked") private Class<? extends Enum> enumType(String field) {
      return (Class<? extends Enum>) typeArg(typeArg(field));
    }
  }
}
