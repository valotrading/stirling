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
package fixengine.messages;

import jdave.junit4.JDaveRunner;

import org.junit.runner.RunWith;

/**
 * @author Pekka Enberg 
 */
@RunWith(JDaveRunner.class)
public class ExecutionReportMessageParsingSpec extends FixMessageParsingSpecification<ExecutionReportMessage> {
    public class ExecutionReportThatContainsRequiredFields {
        private static final String BODY = "37=orderid17=execid150=239=255=AAPL54=138=50151=014=16=120.00";

        private ExecutionReportMessage report;

        public ExecutionReportMessage create() {
            return report = (ExecutionReportMessage) parse(MsgType.EXECUTION_REPORT, BODY);
        }

        public void hasNoMissingFields() {
            specify(report.contains(new fixengine.Specification<Field>() {
                @Override
                public boolean isSatisfiedBy(Field field) {
                    return field.isMissing();
                }
            }), must.equal(false));
        }
    }

    public class ExecutionReportThatContainsMultipleParties {
        private static final String BODY = "37=orderid17=execid150=239=255=AAPL54=138=50151=014=16=120.00453=2448=X448=Y";

        private ExecutionReportMessage report;

        public ExecutionReportMessage create() {
            return report = (ExecutionReportMessage) parse(MsgType.EXECUTION_REPORT, BODY);
        }

        public void hasNoMissingFields() {
            specify(report.contains(new fixengine.Specification<Field>() {
                @Override
                public boolean isSatisfiedBy(Field field) {
                    return field.isMissing();
                }
            }), must.equal(false));
        }
    }
}