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
 * Used for options when delivering the order to an execution system/exchange to
 * specify if the order is for a customer or the firm placing the order itself.
 * 
 * Note: No longer used as of FIX 4.3.
 * 
 * @author Pekka Enberg
 */
public class CustomerOrFirmField extends EnumField<CustomerOrFirm> {
    private static final Tag TAG = new Tag(204);

    public CustomerOrFirmField(Required required) {
        super(TAG, required);
    }

    @Override
    public void parse(String value) {
        this.value = CustomerOrFirm.parse(Integer.parseInt(value));
    }
}
