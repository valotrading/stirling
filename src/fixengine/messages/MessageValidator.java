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
package fixengine.messages;

import java.util.ArrayList;
import java.util.List;

import fixengine.messages.Validator.ErrorHandler;
import fixengine.session.Session;
import fixengine.tags.OrigSendingTime;

public class MessageValidator {
    private static final List<Validator<Message>> validators = new ArrayList<Validator<Message>>() {
        {
            add(new AbstractMessageValidator() {
                @Override protected boolean isValid(Session session, Message message) {
                    return message.hasMsgSeqNum();
                }

                @Override protected void error(Session session, Message message, ErrorHandler handler) {
                    handler.terminate("MsgSeqNum(35) is missing");
                }
            });
            add(new AbstractMessageValidator() {
                @Override protected boolean isValid(Session session, Message message) {
                    return session.isAvailable();
                }

                @Override protected void error(Session session, Message message, ErrorHandler handler) {
                    handler.businessReject(BusinessRejectReasonValue.APPLICATION_NOT_AVAILABLE, "Application not available", ErrorLevel.WARNING);
                }
            });
            add(new AbstractMessageValidator() {
                @Override protected boolean isValid(Session session, Message message) {
                    return !message.isTooLowSeqNum(session.getIncomingSeq().peek());
                }

                @Override protected void error(Session session, Message message, ErrorHandler handler) {
                    handler.terminate("MsgSeqNum too low, expecting " + session.getIncomingSeq().peek() + " but received " + message.getMsgSeqNum());
                }
            });
            add(new AbstractMessageValidator() {
                @Override protected boolean isValid(Session session, Message message) {
                    return message.hasValidBeginString(session.getConfig());
                }

                @Override protected void error(Session session, Message message, ErrorHandler handler) {
                    handler.terminate("BeginString is invalid, expecting " + session.getConfig().getVersion().value() + " but received " + message.getBeginString());
                }
            });
            add(new AbstractMessageValidator() {
                @Override protected boolean isValid(Session session, Message message) {
                    return message.hasValidSenderCompId(session.getConfig());
                }

                @Override protected void error(Session session, Message message, ErrorHandler handler) {
                    handler.sessionReject(SessionRejectReasonValue.COMP_ID_PROBLEM, "Invalid SenderCompID(49): " + message.getSenderCompId(), ErrorLevel.ERROR, true);
                }
            });
            add(new AbstractMessageValidator() {
                @Override protected boolean isValid(Session session, Message message) {
                    return message.hasValidTargetCompId(session.getConfig());
                }

                @Override protected void error(Session session, Message message, ErrorHandler handler) {
                    handler.sessionReject(SessionRejectReasonValue.COMP_ID_PROBLEM, "Invalid TargetCompID(56): " + message.getTargetCompId(), ErrorLevel.ERROR, true);
                }
            });
            add(new AbstractMessageValidator() {
                @Override protected boolean isValid(Session session, Message message) {
                    return message.hasOrigSendTimeAfterSendingTime();
                }

                @Override protected void error(Session session, Message message, ErrorHandler handler) {
                    String text = "OrigSendingTime " + message.getOrigSendingTime() + " after " + message.getSendingTime();
                    handler.sessionReject(SessionRejectReasonValue.SENDING_TIME_ACCURACY_PROBLEM, text, ErrorLevel.ERROR, true);
                }
            });
            add(new AbstractMessageValidator() {
                @Override protected boolean isValid(Session session, Message message) {
                    return message.hasAccurateSendingTime(session.currentTime());
                }

                @Override protected void error(Session session, Message message, ErrorHandler handler) {
                    String text = "SendingTime is invalid: " + message.getSendingTime();
                    handler.sessionReject(SessionRejectReasonValue.SENDING_TIME_ACCURACY_PROBLEM, text, ErrorLevel.ERROR, true);
                }
            });
            add(new AbstractMessageValidator() {
                @Override protected boolean isValid(Session session, Message message) {
                    return message.isPointToPoint();
                }

                @Override protected void error(Session session, Message message, ErrorHandler handler) {
                    String text = "Third-party message routing is not supported";
                    handler.sessionReject(SessionRejectReasonValue.COMP_ID_PROBLEM, text, ErrorLevel.ERROR, false);
                }
            });
            add(new AbstractFieldsValidator() {
                @Override protected boolean isValid(Session session, Field field) {
                    if (field.isConditional()) {
                        return true;
                    }
                    return !field.isMissing();
                }

                @Override protected void error(Session session, Message message, Field field, ErrorHandler handler) {
                    if (session.isAuthenticated()) {
                        String text = toString(field) + ": Tag missing";
                        handler.sessionReject(SessionRejectReasonValue.TAG_MISSING, text, ErrorLevel.ERROR, false);
                    } else {
                        handler.terminate(toString(field) + ": Tag missing");
                    }
                }
            });
            add(new AbstractFieldsValidator() {
                @Override protected boolean isValid(Session session, Field field) {
                    if (!field.isConditional()) {
                        return true;
                    }
                    return !field.isMissing();
                }

                @Override protected void error(Session session, Message message, Field field, ErrorHandler handler) {
                    if (field.hasSingleTag() && OrigSendingTime.TAG.equals(field.tag())) {
                        handler.sessionReject(SessionRejectReasonValue.TAG_MISSING, toString(field) + ": Required tag missing", ErrorLevel.ERROR, false);
                    } else {
                        handler.businessReject(BusinessRejectReasonValue.CONDITIONALLY_REQUIRED_FIELD_MISSING, toString(field) + ": Conditionally required field missing", ErrorLevel.ERROR);
                    }
                }
            });
        }
        private static final long serialVersionUID = 1L;
    };

    public static boolean validate(Session session, Message message, ErrorHandler handler) {
        for (Validator<Message> validator : validators) {
            if (!validator.validate(session, message, handler))
                return false;
        }
        return true;
    }
}
