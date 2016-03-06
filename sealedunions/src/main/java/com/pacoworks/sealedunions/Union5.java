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

public interface Union5<First, Second, Third, Fourth, Fifth> {
    void continued(Consumer<First> continuationFirst, Consumer<Second> continuationSecond,
            Consumer<Third> continuationThird, Consumer<Fourth> continuationFourth,
            Consumer<Fifth> continuationFifth);

    <R> R join(Function<First, R> mapFirst, Function<Second, R> mapSecond,
            Function<Third, R> mapThird, Function<Fourth, R> mapFourth,
            Function<Fifth, R> mapFifth);


    interface Factory<First, Second, Third, Fourth, Fifth> {
        Union5<First, Second, Third, Fourth, Fifth> first(First first);

        Union5<First, Second, Third, Fourth, Fifth> second(Second second);

        Union5<First, Second, Third, Fourth, Fifth> third(Third third);

        Union5<First, Second, Third, Fourth, Fifth> fourth(Fourth fourth);

        Union5<First, Second, Third, Fourth, Fifth> fifth(Fifth fifth);
    }
}
