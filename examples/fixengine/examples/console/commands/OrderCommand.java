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

import fixengine.messages.EnumField;
import fixengine.messages.Field;
import fixengine.messages.FloatField;
import fixengine.messages.IntegerField;
import fixengine.messages.Message;
import fixengine.messages.StringField;
import fixengine.messages.Tag;

import fixengine.messages.MessageFactory;
import fixengine.messages.AbstractField;

import fixengine.tags.OrigClOrdID;
import fixengine.tags.OrderID;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import fixengine.examples.console.ConsoleClient;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Constructor;

public abstract class OrderCommand implements Command {
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
      String profileTagsPackage = client.getMessageFactory().getTagsPackage();
      setFields(message, scanner, profileTagsPackage);
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
    msg.setString(OrderID.TAG, orderID);
  }

  private String origClientOrderID(Message msg) {
    return msg.getString(OrigClOrdID.TAG);
  }

  protected abstract Message newMessage(ConsoleClient client);

  protected abstract boolean isModifyingOrderMessage();

  private void setFields(Message message, Scanner scanner, String profileTagsPackage) {
    while (scanner.hasNext()) {
      String field = scanner.next();
      findParserForField(field, profileTagsPackage).setField(message, field);
    }
  }

  private Parser findParserForField(String field, String profileTagsPackage) {
    for (Class<? extends Parser> parserClass : parserClasses) {
      Parser parser = newParser(parserClass, profileTagsPackage);
      if (parser.matches(field))
        return parser;
    }
    throw new RuntimeException("cannot parse field: " + field);
  }

  private Parser newParser(Class<? extends Parser> clazz, String profileTagsPackage) {
    try {
      return clazz.getDeclaredConstructor(String.class).newInstance(profileTagsPackage);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void setField(Message message, String field) {
  }

  private interface Parser {
    boolean matches(String field);
    void setField(Message msg, String field);
  }

  private static abstract class AbstractFieldParser implements Parser {
    private static final String DEFAULT_TAGS_PACKAGE = "fixengine.tags";
    private final String profileTagsPackage;

    protected AbstractFieldParser(String profileTagsPackage) {
      this.profileTagsPackage = profileTagsPackage;
    }

    @Override public boolean matches(String field) {
      if (hasTypeArg(field))
        return typeArg(field).equals(getFieldClass());
      return false;
    }

    @SuppressWarnings("unchecked") protected Class<Tag<?>> tagClass(String field) {
      try {
        try {
          return (Class<Tag<?>>) Class.forName(profileTagsPackage + "." + tag(field));
        } catch (ClassNotFoundException e) {
          return (Class<Tag<?>>) Class.forName(DEFAULT_TAGS_PACKAGE + "." + tag(field));
        }
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }

    protected abstract Class<? extends Field> getFieldClass();

    protected boolean hasTypeArg(String field) {
      ParameterizedType type = (ParameterizedType) tagClass(field).getGenericSuperclass();
      return type.getActualTypeArguments().length > 0;
    }

    protected Class<?> typeArg(String field) {
      return typeArg(tagClass(field));
    }

    protected Class<?> typeArg(Class<?> clazz) {
      ParameterizedType type = (ParameterizedType) clazz.getGenericSuperclass();
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
    public StringFieldParser(String profileTagsPackage) {
      super(profileTagsPackage);
    }

    @Override @SuppressWarnings("unchecked") public void setField(Message msg, String field) {
      try {
        AbstractField<String> f = (AbstractField<String>) msg.lookup(tagClass(field).newInstance());
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
    public FloatFieldParser(String profileTagsPackage) {
      super(profileTagsPackage);
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
    public IntegerFieldParser(String profileTagsPackage) {
      super(profileTagsPackage);
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
    public EnumFieldParser(String profileTagsPackage) {
      super(profileTagsPackage);
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
