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
package stirling.fix.messages

import stirling.fix.messages.fix43.SeqNumField
import java.lang.{Integer, Character}

abstract class MessageTag[T <: Field](value: Int, clazz: Class[T]) extends Tag[T](value, clazz) {
  val Tag = this
}

abstract class EnumTag[T](value: Int) extends MessageTag[EnumField[Value[T]]](value, classOf[EnumField[Value[T]]]) {
  def newField(required: Required) = new EnumField(this) { setRequired(required) }
  def parse(v: String) = values.find { value =>
    value.v.toString.equals(v)
  }.getOrElse {
    throw new InvalidValueForTagException(v)
  }
  def valueOf(name: String) = namesAndValues.find { case (n, v) =>
    n.equals(name)
  }.getOrElse {
    throw new Exception("No such enum field with name: " + name)
  }._2
  private def values: Array[Value[T]] = {
    getClass.getDeclaredFields.filter { field =>
      classOf[Value[T]].isAssignableFrom(field.getType)
    }.map{ field =>
      method(field).invoke(this).asInstanceOf[Value[T]]
    }
  }
  private def namesAndValues: Array[(String, Value[T])] = {
    getClass.getDeclaredFields.filter { field =>
      classOf[Value[T]].isAssignableFrom(field.getType)
    }.map{ field =>
      (field.getName, method(field).invoke(this).asInstanceOf[Value[T]])
    }
  }
  private def method(field: java.lang.reflect.Field) = getClass.getDeclaredMethod(field.getName)
}

abstract class Value[T](val v: T) extends Formattable {
  def value = v.toString
}

case class CharValue(char: Character) extends Value[Character](char)
case class IntegerValue(int: Integer) extends Value[Integer](int)
case class StringValue(str: String) extends Value[String](str)
case class BooleanValue(bool: Boolean) extends Value[Boolean](bool)

abstract class BooleanTag(value: Int) extends MessageTag[BooleanField](value, classOf[BooleanField]) {
  def newField(required: Required) = new BooleanField(this) { setRequired(required) }
}
abstract class CharTag(value: Int) extends MessageTag[CharField](value, classOf[CharField]) {
  def newField(required: Required) = new CharField(this) { setRequired(required) }
}
abstract class FloatTag(value: Int) extends MessageTag[FloatField](value, classOf[FloatField]) {
  def newField(required: Required) = new FloatField(this) { setRequired(required) }
}
abstract class IntegerTag(value: Int) extends MessageTag[IntegerField](value, classOf[IntegerField]) {
  def newField(required: Required) = new IntegerField(this) { setRequired(required) }
}
abstract class StringTag(value: Int) extends MessageTag[StringField](value, classOf[StringField]) {
  def newField(required: Required) = new StringField(this) { setRequired(required) }
}
abstract class ExchangeTag(value: Int) extends MessageTag[ExchangeField](value, classOf[ExchangeField]) {
  def newField(required: Required) = new ExchangeField(this) { setRequired(required) }
}
abstract class LocalMktDateTag(value: Int) extends MessageTag[LocalMktDateField](value, classOf[LocalMktDateField]) {
  def newField(required: Required) = new LocalMktDateField(this) { setRequired(required) }
}
abstract class MonthYearTag(value: Int) extends MessageTag[MonthYearField](value, classOf[MonthYearField]) {
  def newField(required: Required) = new MonthYearField(this) { setRequired(required) }
}
abstract class UtcTimestampTag(value: Int) extends MessageTag[UtcTimestampField](value, classOf[UtcTimestampField]) {
  def newField(required: Required) = new UtcTimestampField(this) { setRequired(required) }
}
abstract class NumInGroupTag(value: Int) extends MessageTag[NumInGroupField](value, classOf[NumInGroupField]) {
  def newField(required: Required) = new NumInGroupField(this) { setRequired(required) }
}
abstract class PriceTag(value: Int) extends MessageTag[PriceField](value, classOf[PriceField]) {
  def newField(required: Required) = new PriceField(this) { setRequired(required) }
}
abstract class PriceOffsetTag(value: Int) extends MessageTag[PriceOffsetField](value, classOf[PriceOffsetField]) {
  def newField(required: Required) = new PriceOffsetField(this) { setRequired(required) }
}
abstract class QtyTag(value: Int) extends MessageTag[QtyField](value, classOf[QtyField]) {
  def newField(required: Required) = new QtyField(this) { setRequired(required) }
}
abstract class SeqNumTag(value: Int) extends MessageTag[SeqNumField](value, classOf[SeqNumField]) {
  def newField(required: Required) = new SeqNumField(this) { setRequired(required) }
}

abstract class CurrencyTag(value: Int) extends StringTag(value)
abstract class AmtTag(value: Int) extends FloatTag(value)
abstract class PercentageTag(value: Int) extends FloatTag(value)
