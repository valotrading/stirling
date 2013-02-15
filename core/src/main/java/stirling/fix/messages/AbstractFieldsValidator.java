/*
 * Copyright 2009 the original author or authors.
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

import stirling.fix.session.Session;

/**
 * @author Pekka Enberg 
 */
public abstract class AbstractFieldsValidator implements Validator<Message> {
    @Override
    public final boolean validate(Session session, Message message, ErrorHandler handler) {
        for (Field field : message) {
            if (!isValid(session, field)) {
                error(session, message, field, handler);
                return false;
            }
        }
        return true;
    }

    protected abstract boolean isValid(Session session, Field field);

    protected abstract void error(Session session, Message message, Field field, ErrorHandler handler);

    protected String toString(Field field) {
        return field.prettyName();
    }
}
