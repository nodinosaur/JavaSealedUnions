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

public interface Union7<First, Second, Third, Fourth, Fifth, Sixth, Seventh> {
    void continued(Consumer<First> continuationFirst, Consumer<Second> continuationSecond,
            Consumer<Third> continuationThird, Consumer<Fourth> continuationFourth,
            Consumer<Fifth> continuationFifth, Consumer<Sixth> continuationSixth,
            Consumer<Seventh> continuationSeventh);

    <R> R join(Function<First, R> mapFirst, Function<Third, R> mapSecond,
            Function<Third, R> mapThird, Function<Fourth, R> mapFourth, Function<Fifth, R> mapFifth,
            Function<Sixth, R> mapSixth, Function<Seventh, R> mapSeventh);

    interface IdentifiableUnion9<First, Second, Third, Fourth, Fifth, Sixth, Seventh>
            extends Union7<First, Second, Third, Fourth, Fifth, Sixth, Seventh> {
        boolean isFirst();

        boolean isSecond();

        boolean isThird();

        boolean isFourth();

        boolean isFifth();

        boolean isSixth();

        boolean isSeventh();
    }

    interface Factory<First, Second, Third, Fourth, Fifth, Sixth, Seventh> {
        Union7<First, Second, Third, Fourth, Fifth, Sixth, Seventh> first(First first);

        Union7<First, Second, Third, Fourth, Fifth, Sixth, Seventh> second(Second second);

        Union7<First, Second, Third, Fourth, Fifth, Sixth, Seventh> third(Third third);

        Union7<First, Second, Third, Fourth, Fifth, Sixth, Seventh> fourth(Fourth fourth);

        Union7<First, Second, Third, Fourth, Fifth, Sixth, Seventh> fifth(Fifth fifth);

        Union7<First, Second, Third, Fourth, Fifth, Sixth, Seventh> sixth(Sixth sixth);

        Union7<First, Second, Third, Fourth, Fifth, Sixth, Seventh> seventh(Seventh seventh);
    }
}
