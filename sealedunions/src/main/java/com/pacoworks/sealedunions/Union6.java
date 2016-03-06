/*
 * Copyright (c) pakoito 2016
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pacoworks.sealedunions;

import java.util.function.Consumer;
import java.util.function.Function;

public interface Union6<First, Second, Third, Fourth, Fifth, Sixth> {
    void continued(Consumer<First> continuationFirst, Consumer<Second> continuationSecond,
            Consumer<Third> continuationThird, Consumer<Fourth> continuationFourth,
            Consumer<Fifth> continuationFifth, Consumer<Sixth> continuationSixth);

    <R> R join(Function<First, R> mapFirst, Function<Third, R> mapSecond,
            Function<Third, R> mapThird, Function<Fourth, R> mapFourth, Function<Fifth, R> mapFifth,
            Function<Sixth, R> mapSixth);

    interface IdentifiableUnion9<First, Second, Third, Fourth, Fifth, Sixth>
            extends Union6<First, Second, Third, Fourth, Fifth, Sixth> {
        boolean isFirst();

        boolean isSecond();

        boolean isThird();

        boolean isFourth();

        boolean isFifth();

        boolean isSixth();
    }

    interface Factory<First, Second, Third, Fourth, Fifth, Sixth> {
        Union6<First, Second, Third, Fourth, Fifth, Sixth> first(First first);

        Union6<First, Second, Third, Fourth, Fifth, Sixth> second(Second second);

        Union6<First, Second, Third, Fourth, Fifth, Sixth> third(Third third);

        Union6<First, Second, Third, Fourth, Fifth, Sixth> fourth(Fourth fourth);

        Union6<First, Second, Third, Fourth, Fifth, Sixth> fifth(Fifth fifth);

        Union6<First, Second, Third, Fourth, Fifth, Sixth> sixth(Sixth sixth);
    }
}
