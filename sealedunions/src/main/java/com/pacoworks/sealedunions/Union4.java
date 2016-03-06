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

public interface Union4<First, Second, Third, Fourth> {
    void continued(Consumer<First> continuationFirst, Consumer<Second> continuationSecond,
            Consumer<Third> continuationThird, Consumer<Fourth> continuationFourth);

    <R> R map(Function<First, R> mapFirst, Function<Third, R> mapSecond,
            Function<Third, R> mapThird, Function<Fourth, R> mapFourth);

    interface IdentifiableUnion9<First, Second, Third, Fourth>
            extends Union4<First, Second, Third, Fourth> {
        boolean isFirst();

        boolean isSecond();

        boolean isThird();

        boolean isFourth();

        boolean isFifth();
    }

    interface Factory<First, Second, Third, Fourth> {
        Union4<First, Second, Third, Fourth> first(First first);

        Union4<First, Second, Third, Fourth> second(Second second);

        Union4<First, Second, Third, Fourth> third(Third third);

        Union4<First, Second, Third, Fourth> fourth(Fourth fourth);
    }
}
