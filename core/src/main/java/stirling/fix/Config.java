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
package stirling.fix;

/**
 * @author Pekka Enberg 
 */
public class Config {
    private String senderCompId;
    private String senderSubId;
    private String targetCompId;
    private String targetSubId;
    private Version version;

    private CompIdValidator onBehalfOfCompIdValidator = new CompIdValidator() {
        @Override
        public boolean validate(String onBehalfOfCompId, boolean exists, String msgType) {
            return exists == false;
        }
    };

    private CompIdValidator deliverToCompIdValidator = new CompIdValidator() {
        @Override
        public boolean validate(String deliverToCompId, boolean exists, String msgType) {
            return exists == false;
        }
    };

    public Config() {
        this.version = Version.FIX_4_2;
        this.senderCompId = "";
        this.targetCompId = "";
    }

    public Config(Version version, String senderCompId, String targetCompId) {
        this.version = version;
        this.senderCompId = senderCompId;
        this.targetCompId = targetCompId;
    }

    public String getSenderCompId() {
        return senderCompId;
    }

    public Config setSenderCompId(String senderCompId) {
        this.senderCompId = senderCompId;
        return this;
    }

    public String getSenderSubId() {
        return senderSubId;
    }

    public Config setSenderSubId(String senderSubId) {
        this.senderSubId = senderSubId;
        return this;
    }

    public String getTargetCompId() {
        return targetCompId;
    }

    public Config setTargetCompId(String targetCompId) {
        this.targetCompId = targetCompId;
        return this;
    }

    public String getTargetSubId() {
        return targetSubId;
    }

    public Config setTargetSubId(String targetSubId) {
        this.targetSubId = targetSubId;
        return this;
    }

    public Version getVersion() {
        return version;
    }

    public Config setVersion(Version version) {
        this.version = version;
        return this;
    }

    public boolean supports(Version version) {
        return this.version.ordinal() >= version.ordinal();
    }

    public void setOnBehalfOfCompIdValidator(CompIdValidator validator) {
        onBehalfOfCompIdValidator = validator;
    }

    public void setDeliverToCompIdValidator(CompIdValidator validator) {
        deliverToCompIdValidator = validator;
    }

    public CompIdValidator getOnBehalfOfCompIdValidator() {
        return onBehalfOfCompIdValidator;
    }

    public CompIdValidator getDeliverToCompIdValidator() {
        return deliverToCompIdValidator;
    }

    public Config counterparty() {
        Config config = new Config();
        config.setVersion(getVersion());
        config.setSenderCompId(getTargetCompId());
        config.setSenderSubId(getTargetSubId());
        config.setTargetCompId(getSenderCompId());
        config.setTargetSubId(getSenderSubId());
        return config;
    }
}
