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
public enum EncryptMethod {
    /** None / other */
    NONE(0),

    /** PKCS (proprietary) */
    PKCS(1),

    /** DES (ECB mode) */
    DES(2),

    /** PKCS/DES (proprietary) */
    PKCS_DES(3),

    /** PGP/DES (defunct) */
    PGP_DES(4),

    /** PGP/DES-MD5 */
    PGP_DES_MD5(5),

    /** PEM/DES-MD5 */
    PEM_DES_MD5(6);

    private final int value;

    EncryptMethod(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static EncryptMethod parse(int value) {
        for (EncryptMethod type : EncryptMethod.values()) {
            if (type.value == value)
                return type;
        }
        throw new InvalidValueForTagException(Integer.toString(value));
    }
}