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
 * @author Pekka Enberg 
 */
public class DefaultMessageVisitor implements MessageVisitor {
    @Override
    public void visit(ExecutionReportMessage message) {
        defaultAction(message);
    }

    @Override
    public void visit(HeartbeatMessage message) {
        defaultAction(message);
    }

    @Override
    public void visit(LogonMessage message) {
        defaultAction(message);
    }

    @Override
    public void visit(LogoutMessage message) {
        defaultAction(message);
    }

    @Override
    public void visit(NewOrderSingleMessage message) {
        defaultAction(message);
    }

    @Override
    public void visit(UnknownMessage message) {
        defaultAction(message);
    }

    @Override
    public void visit(OrderCancelRequestMessage message) {
        defaultAction(message);
    }

    @Override
    public void visit(OrderCancelRejectMessage message) {
        defaultAction(message);
    }

    @Override
    public void visit(OrderModificationRequestMessage message) {
        defaultAction(message);
    }

    @Override
    public void visit(BusinessMessageRejectMessage message) {
        defaultAction(message);
    }

    @Override
    public void visit(RejectMessage message) {
        defaultAction(message);
    }

    @Override
    public void visit(ResendRequestMessage message) {
        defaultAction(message);
    }

    @Override
    public void visit(SequenceResetMessage message) {
        defaultAction(message);
    }

    @Override
    public void visit(TestRequestMessage message) {
        defaultAction(message);
    }

    public void defaultAction(Message message) {
    }
}
