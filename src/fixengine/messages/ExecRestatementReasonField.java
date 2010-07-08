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
package fixengine.messages;

/**
 * Code to idenfity reason for an ExecutionReport message sent with
 * ExecType=Restated or used when communicating an unsolicited cancel.
 * 
 * @author Pekka Enberg
 */
public class ExecRestatementReasonField extends EnumField<ExecRestatementReason> {
    private static final Tag TAG = new Tag(378);

    public ExecRestatementReasonField(Required required) {
        super(TAG, required);
    }

    @Override
    public void parse(String value) {
        this.value = ExecRestatementReason.parse(Integer.parseInt(value));
    }
}
