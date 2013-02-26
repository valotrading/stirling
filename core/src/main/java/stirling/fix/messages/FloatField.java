/*
 * Copyright 2008 the original author or authors.
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
package stirling.fix.messages;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FloatField extends AbstractField<Double> {
    private static final DecimalFormatSymbols DECIMAL_SYMBOLS = new DecimalFormatSymbols(Locale.US);
    private static final Pattern DECIMAL_PATTERN = Pattern.compile("-?\\d*(\\.\\d*)?");

    public FloatField(Tag<? extends FloatField> tag) {
        this(tag, null, Required.YES);
    }

    public FloatField(Tag<? extends FloatField> tag, Required required) {
        this(tag, null, required);
    }

    public FloatField(Tag<? extends FloatField> tag, Double value, Required required) {
        super(tag, value, required);
    }

    @Override
    public void parse(String value) {
        Matcher matcher = DECIMAL_PATTERN.matcher(value);
        if (!matcher.matches()) {
            throw newInvalidValueFormatException(value);
        }
        try {
            this.value = Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw newInvalidValueFormatException(value);
        }
    }

    @Override
    protected final String value() {
        DecimalFormat format = new DecimalFormat("0.00#############", DECIMAL_SYMBOLS);
        return format.format(doubleValue());
    }

    protected Double doubleValue() {
        return value;
    }
}
