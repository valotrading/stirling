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
package xtch.soup;

public class PacketType {
  public static final PacketType ELEM = new PacketType();

  public static final String SEQUENCE_DATA = "S";
  public static final String SERVER_HEARTBEAT = "H";
  public static final String DEBUG = "+";
  public static final String LOGIN_ACCEPTED = "A";
  public static final String LOGIN_REJECTED = "J";
  public static final String LOGIN_REQUEST = "L";
  public static final String END_OF_SESSION = "Z";
  public static final String LOGOUT_REQUEST = "O";

  private PacketType() {
  }
}
