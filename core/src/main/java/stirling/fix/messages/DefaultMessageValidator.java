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
package stirling.fix.messages;

import java.util.ArrayList;
import java.util.List;

import stirling.fix.messages.Validator.ErrorHandler;
import stirling.fix.session.Session;
import stirling.fix.tags.fix42.BusinessRejectReason;
import stirling.fix.tags.fix43.SessionRejectReason;
import stirling.fix.tags.fix42.OrigSendingTime;

public class DefaultMessageValidator implements Validator<Message> {
    protected List<Validator<Message>> validators = new ArrayList<Validator<Message>>() {
        {
            add(createApplicationAvailableValidator());
            add(createBeginStringValidator());
            add(createSenderCompIdValidator());
            add(createTargetCompIdValidator());
            add(createOrigSendingTimeValidator());
            add(createSendingTimeValidator());
            add(createOnBehalfOfCompIdValidator());
            add(createDeliverToCompIdValidator());
            add(createConditionalTagValidator());
            add(createRequiredTagValidator());
        }
        private static final long serialVersionUID = 1L;
    };

    protected Validator<Message> createApplicationAvailableValidator() {
        return new AbstractMessageValidator() {
            @Override
            protected boolean isValid(Session session, Message message) {
                return isApplicationAvailableValid(session, message);
            }

            @Override
            protected void error(Session session, Message message, ErrorHandler handler) {
                handler.businessReject(BusinessRejectReason.ApplicationNotAvailable(), "Application not available", ErrorLevel.WARNING);
            }
        };
    }

    protected boolean isApplicationAvailableValid(Session session, Message message) {
        return session.isAvailable();
    }

    protected Validator<Message> createBeginStringValidator() {
        return new AbstractMessageValidator() {
            @Override
            protected boolean isValid(Session session, Message message) {
                return isBeginStringValid(session, message);
            }

            @Override
            protected void error(Session session, Message message, ErrorHandler handler) {
                handler.terminate("BeginString is invalid, expecting " + session.getConfig().getVersion().value() + " but received " + message.getBeginString());
            }
        };
    }

    protected boolean isBeginStringValid(Session session, Message message) {
        return message.hasValidBeginString(session.getConfig());
    }

    protected Validator<Message> createSenderCompIdValidator() {
        return new AbstractMessageValidator() {
            @Override
            protected boolean isValid(Session session, Message message) {
                return isSenderCompIdValid(session, message);
            }

            @Override
            protected void error(Session session, Message message, ErrorHandler handler) {
                handler.sessionReject(SessionRejectReason.CompIdProblem(), "Invalid SenderCompID(49): " + message.getSenderCompId(), ErrorLevel.ERROR, true);
            }
        };
    }

    protected boolean isSenderCompIdValid(Session session, Message message) {
        return message.hasValidSenderCompId(session.getConfig());
    }

    protected Validator<Message> createTargetCompIdValidator() {
        return new AbstractMessageValidator() {
            @Override
            protected boolean isValid(Session session, Message message) {
                return isTargetCompIdValid(session, message);
            }

            @Override
            protected void error(Session session, Message message, ErrorHandler handler) {
                handler.sessionReject(SessionRejectReason.CompIdProblem(), "Invalid TargetCompID(56): " + message.getTargetCompId(), ErrorLevel.ERROR, true);
            }
        };
    }

    protected boolean isTargetCompIdValid(Session session, Message message) {
        return message.hasValidTargetCompId(session.getConfig());
    }

    protected Validator<Message> createOrigSendingTimeValidator() {
        return new AbstractMessageValidator() {
            @Override
            protected boolean isValid(Session session, Message message) {
                return isOrigSendingTimeValid(session, message);
            }

            @Override
            protected void error(Session session, Message message, ErrorHandler handler) {
                String text = "OrigSendingTime " + message.getOrigSendingTime() + " after " + message.getSendingTime();
                handler.sessionReject(SessionRejectReason.SendingTimeAccuracyProblem(), text, ErrorLevel.ERROR, true);
            }
        };
    }

    protected boolean isOrigSendingTimeValid(Session session, Message message) {
        return message.hasOrigSendingTimeEarlierThanOrEqualToSendingTime();
    }

    protected Validator<Message> createSendingTimeValidator() {
        return new AbstractMessageValidator() {
            @Override
            protected boolean isValid(Session session, Message message) {
                return isSendingTimeValid(session, message);
            }

            @Override
            protected void error(Session session, Message message, ErrorHandler handler) {
                String text = "SendingTime is invalid: " + message.getSendingTime();
                handler.sessionReject(SessionRejectReason.SendingTimeAccuracyProblem(), text, ErrorLevel.ERROR, true);
            }
        };
    }

    protected boolean isSendingTimeValid(Session session, Message message) {
        return message.hasAccurateSendingTime();
    }

    protected Validator<Message> createOnBehalfOfCompIdValidator() {
        return new AbstractMessageValidator() {
            @Override
            protected boolean isValid(Session session, Message message) {
                return isOnBehalfOfCompIdValid(session, message);
            }

            @Override
            protected void error(Session session, Message message, ErrorHandler handler) {
                String text = "OnBehalfOfCompID(115) not allowed: " + message.getOnBehalfOfCompId();
                handler.sessionReject(SessionRejectReason.CompIdProblem(), text, ErrorLevel.ERROR, false);
            }
        };
    }

    protected boolean isOnBehalfOfCompIdValid(Session session, Message message) {
        return !message.hasOnBehalfOfCompId();
    }

    protected Validator<Message> createDeliverToCompIdValidator() {
        return new AbstractMessageValidator() {
            @Override
            protected boolean isValid(Session session, Message message) {
                return isDeliverToCompIdValid(session, message);
            }

            @Override
            protected void error(Session session, Message message, ErrorHandler handler) {
                String text = "DeliverToCompID(128) not allowed: " + message.getDeliverToCompId();
                handler.sessionReject(SessionRejectReason.CompIdProblem(), text, ErrorLevel.ERROR, false);
            }
        };
    }

    protected boolean isDeliverToCompIdValid(Session session, Message message) {
        return !message.hasDeliverToCompId();
    }

    protected Validator<Message> createConditionalTagValidator() {
        return new AbstractFieldsValidator() {
            @Override
            protected boolean isValid(Session session, Field field) {
                return isConditionalTagValid(session, field);
            }

            @Override
            protected void error(Session session, Message message, Field field, ErrorHandler handler) {
                if (session.isAuthenticated()) {
                    String text = toString(field) + ": Tag missing";
                    handler.sessionReject(SessionRejectReason.TagMissing(), text, ErrorLevel.ERROR, false);
                } else {
                    handler.terminate(toString(field) + ": Tag missing");
                }
            }
        };
    }

    protected boolean isConditionalTagValid(Session session, Field field) {
        if (field.required().isConditional()) {
            return true;
        }
        return !missing(field);
    }

    protected Validator<Message> createRequiredTagValidator() {
        return new AbstractFieldsValidator() {
            @Override
            protected boolean isValid(Session session, Field field) {
                return isRequiredTagValid(session, field);
            }

            @Override
            protected void error(Session session, Message message, Field field, ErrorHandler handler) {
                if (OrigSendingTime.Tag().equals(field.tag())) {
                    handler.sessionReject(SessionRejectReason.TagMissing(), toString(field) + ": Required tag missing", ErrorLevel.ERROR, false);
                } else {
                    handler.businessReject(BusinessRejectReason.ConditionallyRequiredFieldMissing(), toString(field) + ": Conditionally required field missing", ErrorLevel.ERROR);
                }
            }
        };
    }

    protected boolean isRequiredTagValid(Session session, Field field) {
        if (!field.required().isConditional()) {
            return true;
        }
        return !missing(field);
    }

    protected boolean missing(Field field) {
        return field.required().isRequired() && !field.hasValue();
    }

    @Override
    public boolean validate(Session session, Message message, ErrorHandler handler) {
        for (Validator<Message> validator : validators) {
            if (!validator.validate(session, message, handler))
                return false;
        }
        return true;
    }
}
