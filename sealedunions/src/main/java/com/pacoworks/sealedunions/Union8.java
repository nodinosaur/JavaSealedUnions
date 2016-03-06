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

public interface Union8<First, Second, Third, Fourth, Fifth, Sixth, Seventh, Eighth> {
    void continued(Consumer<First> continuationFirst, Consumer<Second> continuationSecond,
            Consumer<Third> continuationThird, Consumer<Fourth> continuationFourth,
            Consumer<Fifth> continuationFifth, Consumer<Sixth> continuationSixth,
            Consumer<Seventh> continuationSeventh, Consumer<Eighth> continuationEighth);

    <R> R join(Function<First, R> mapFirst, Function<Second, R> mapSecond,
            Function<Third, R> mapThird, Function<Fourth, R> mapFourth, Function<Fifth, R> mapFifth,
            Function<Sixth, R> mapSixth, Function<Seventh, R> mapSeventh,
            Function<Eighth, R> mapEighth);


    interface Factory<First, Second, Third, Fourth, Fifth, Sixth, Seventh, Eighth> {
        Union8<First, Second, Third, Fourth, Fifth, Sixth, Seventh, Eighth> first(First first);

        Union8<First, Second, Third, Fourth, Fifth, Sixth, Seventh, Eighth> second(Second second);

        Union8<First, Second, Third, Fourth, Fifth, Sixth, Seventh, Eighth> third(Third third);

        Union8<First, Second, Third, Fourth, Fifth, Sixth, Seventh, Eighth> fourth(Fourth fourth);

        Union8<First, Second, Third, Fourth, Fifth, Sixth, Seventh, Eighth> fifth(Fifth fifth);

        Union8<First, Second, Third, Fourth, Fifth, Sixth, Seventh, Eighth> sixth(Sixth sixth);

        Union8<First, Second, Third, Fourth, Fifth, Sixth, Seventh, Eighth> seventh(
                Seventh seventh);

        Union8<First, Second, Third, Fourth, Fifth, Sixth, Seventh, Eighth> eighth(Eighth eighth);
    }
}
