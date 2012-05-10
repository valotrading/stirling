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
}
