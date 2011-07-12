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
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import fixengine.examples.console.ConsoleClient;
import fixengine.messages.AbstractField;
import fixengine.messages.ExchangeField;
import fixengine.messages.BooleanField;
import fixengine.messages.EnumField;
import fixengine.messages.EnumTag;
import fixengine.messages.Field;
import fixengine.messages.FloatField;
import fixengine.messages.IntegerField;
import fixengine.messages.UtcTimestampField;
import fixengine.messages.Message;
import fixengine.messages.MessageFactory;
import fixengine.messages.PriceField;
import fixengine.messages.QtyField;
import fixengine.messages.StringField;
import fixengine.messages.Tag;
import fixengine.messages.Value;
import fixengine.tags.fix42.OrderID;
import fixengine.tags.fix42.OrigClOrdID;
import fixengine.tags.fix42.TransactTime;

public abstract class FixMessageCommand implements Command {
  private static final Set<Class<? extends Parser>> parserClasses = new HashSet<Class<? extends Parser>>();

  {
    parserClasses.add(IntegerFieldParser.class);
    parserClasses.add(StringFieldParser.class);
    parserClasses.add(ExchangeFieldParser.class);
    parserClasses.add(FloatFieldParser.class);
    parserClasses.add(QtyFieldParser.class);
    parserClasses.add(PriceFieldParser.class);
    parserClasses.add(EnumFieldParser.class);
    parserClasses.add(BooleanFieldParser.class);
  }

  public String[] getArgumentNames(ConsoleClient client) {
    List<String> fields = new ArrayList<String>();
    Iterator<Field> fieldsIterator = newMessage(client).iterator();
    while (fieldsIterator.hasNext()) {
      Field field = fieldsIterator.next();
      String prettyName = field.prettyName();
      fields.add(prettyName.replaceAll("\\(([0-9]+)\\)", "="));
    }
    return fields.toArray(new String[0]);
  }

  public void execute(ConsoleClient client, Scanner scanner) throws CommandArgException {
    try {
      Message message = newMessage(client);
      if (isTransactTimeDefined(message))
        setTransactTime(message, client);
      setFields(message, scanner, client.getMessageFactory());
      if (isModifyingOrderMessage() && message.isDefined(OrderID.Tag()))
        setMessageOrderID(message, client);
      if (client.getSession() != null)
        client.getSession().send(client.getConnection(), message);
    } catch (Exception e) {
      throw new CommandArgException("failed to set field: " + e.getMessage());
    }
  }

  private boolean isTransactTimeDefined(Message message) {
    return message.lookup(TransactTime.Tag()) != null;
  }

  private void setTransactTime(Message message, ConsoleClient client) {
    UtcTimestampField field = (UtcTimestampField) message.lookup(TransactTime.Tag());
    field.setValue(client.getSession().currentTime());
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

    protected Type typeArg(String field) {
      return typeArg(tagClass(field));
    }

    protected Type typeArg(Class<?> clazz) {
      ParameterizedType type = (ParameterizedType) clazz.getSuperclass().getGenericSuperclass();
      return type.getActualTypeArguments()[0];
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

  private static class ExchangeFieldParser extends StringFieldParser {
    public ExchangeFieldParser(MessageFactory messageFactory) {
      super(messageFactory);
    }

    @Override protected Class<? extends Field> getFieldClass() {
      return ExchangeField.class;
    }
  }

  private static class FloatFieldParser extends AbstractFieldParser {
    public FloatFieldParser(MessageFactory messageFactory) {
      super(messageFactory);
    }

    @Override @SuppressWarnings("unchecked") public void setField(Message msg, String field) {
      try {
        AbstractField<Double> f = (AbstractField<Double>) msg.lookup(messageFactory.createTag(tag(field)));
        f.setValue(Double.valueOf(value(field)));
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }

    @Override protected Class<? extends Field> getFieldClass() {
      return FloatField.class;
    }
  }

  private static class QtyFieldParser extends FloatFieldParser {
    public QtyFieldParser(MessageFactory messageFactory) {
      super(messageFactory);
    }

    @Override protected Class<? extends Field> getFieldClass() {
      return QtyField.class;
    }
  }

  private static class PriceFieldParser extends FloatFieldParser {
    public PriceFieldParser(MessageFactory messageFactory) {
      super(messageFactory);
    }

    @Override protected Class<? extends Field> getFieldClass() {
      return PriceField.class;
    }
  }

  private static class IntegerFieldParser extends AbstractFieldParser {
    public IntegerFieldParser(MessageFactory messageFactory) {
      super(messageFactory);
    }

    @Override @SuppressWarnings("unchecked") public void setField(Message msg, String field) {
      try {
        AbstractField<Integer> f = (AbstractField<Integer>) msg.lookup(messageFactory.createTag(tag(field)));
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
      if (hasTypeArg(field) && typeArg(field) instanceof ParameterizedType) {
        ParameterizedType type = (ParameterizedType) typeArg(field);
        return type.getRawType().equals(getFieldClass());
      }
      return false;
    }

    @Override @SuppressWarnings("unchecked") public void setField(Message msg, String field) {
      try {
        EnumTag<?> tag = (EnumTag<?>) messageFactory.createTag(tag(field));
        AbstractField<Value<?>> f = (AbstractField<Value<?>>) msg.lookup(tag);
        f.setValue(tag.valueOf(value(field)));
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }

    @Override protected Class<? extends Field> getFieldClass() {
      return EnumField.class;
    }
  }

  private static class BooleanFieldParser extends AbstractFieldParser {
    public BooleanFieldParser(MessageFactory messageFactory) {
      super(messageFactory);
    }

    @Override @SuppressWarnings("unchecked") public void setField(Message msg, String field) {
      try {
        AbstractField<Boolean> f = (AbstractField<Boolean>) msg.lookup(messageFactory.createTag(tag(field)));
        if (f != null)
          f.setValue(Boolean.valueOf(value(field)));
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }

    @Override protected Class<? extends Field> getFieldClass() {
      return BooleanField.class;
    }
  }
}
