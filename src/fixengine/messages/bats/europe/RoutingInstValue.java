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
package fixengine.messages.bats.europe;

import fixengine.messages.Formattable;
import fixengine.messages.InvalidValueForTagException;

public enum RoutingInstValue implements Formattable {
    BATS("B"),                                /* BATS Only */
    POST_ONLY("P"),                           /* BATS Only - Post Only */
    POST_ONLY_AT_LIMIT("Q"),                  /* BATS Only - Post Only At Limit */
    DARK_BOOK_ONLY("BD"),                     /* BATS Dark Book Only */
    AUTOMATIC_DARK_ROUTED("BA"),              /* BATS Automatic Dark Routed */
    DARK_SELF_CROSS("BX"),                    /* BATS Dark Self Cross */
    ROUTABLE_CYCLE("R"),                      /* Routable CYCLE */
    ROUTABLE_RECYLCLE_ON_LOCK("RL"),          /* Routable (CYCLE on Cross) */
    ROUTABLE_RECYCLE_ON_CROSS("RC"),          /* Routable (RECYCLE on Cross) */
    BATS_PLUS_PRIMARY_LISTING_EXCHAGNE("PP"), /* BATS Plus Primary Listing Exchange */
    BATS_PLUS_LIQUIDNET("PL");                /* BATS Plus Liquidnet */

    private String value;

    RoutingInstValue(String value) {
        this.value = value;
    }

    @Override public String value() {
        return value;
    }

    public static RoutingInstValue parse(String value) {
        for (RoutingInstValue type : RoutingInstValue.values()) {
            if (type.value.equals(value))
                return type;
        }
        throw new InvalidValueForTagException(value);
    }
}
