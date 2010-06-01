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
package fixengine.examples.exchange;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import fixengine.messages.ExecutionReportMessage;
import fixengine.messages.OrdStatus;

/**
 * @author Pekka Enberg 
 */
public class HasOrdStatus extends TypeSafeMatcher<ExecutionReportMessage> {
    private final OrdStatus expected;

    public HasOrdStatus(OrdStatus expected) {
        this.expected = expected;
    }

    @Override
    public boolean matchesSafely(ExecutionReportMessage message) {
        return message.getOrdStatus().equals(expected);
    }

    public void describeTo(Description description) {
        description.appendText("hasOrdStatus(").appendValue(expected).appendText(")");
    }

    public static Matcher<ExecutionReportMessage> hasOrdStatus(OrdStatus status) {
        return new HasOrdStatus(status);
    }
}