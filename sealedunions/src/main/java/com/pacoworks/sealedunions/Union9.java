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

public interface Union9<First, Second, Third, Fourth, Fifth, Sixth, Seventh, Eighth, Ninth> {
    void continued(Consumer<First> continuationFirst, Consumer<Second> continuationSecond,
            Consumer<Third> continuationThird, Consumer<Fourth> continuationFourth,
            Consumer<Fifth> continuationFifth, Consumer<Sixth> continuationSixth,
            Consumer<Seventh> continuationSeventh, Consumer<Eighth> continuationEighth,
            Consumer<Ninth> continuationNinth);

    <R> R map(Function<First, R> mapFirst, Function<Third, R> mapSecond,
            Function<Third, R> mapThird, Function<Fourth, R> mapFourth, Function<Fifth, R> mapFifth,
            Function<Sixth, R> mapSixth, Function<Seventh, R> mapSeventh,
            Function<Eighth, R> mapEighth, Function<Ninth, R> mapNinth);

    interface IdentifiableUnion9<First, Second, Third, Fourth, Fifth, Sixth, Seventh, Eighth, Ninth>
            extends Union9<First, Second, Third, Fourth, Fifth, Sixth, Seventh, Eighth, Ninth> {
        boolean isFirst();

        boolean isSecond();

        boolean isThird();

        boolean isFourth();

        boolean isFifth();

        boolean isSixth();

        boolean isSeventh();

        boolean isEighth();

        boolean isNinth();
    }

    interface Factory<First, Second, Third, Fourth, Fifth, Sixth, Seventh, Eighth, Ninth> {
        Union9<First, Second, Third, Fourth, Fifth, Sixth, Seventh, Eighth, Ninth> first(
                First first);

        Union9<First, Second, Third, Fourth, Fifth, Sixth, Seventh, Eighth, Ninth> second(
                Second second);

        Union9<First, Second, Third, Fourth, Fifth, Sixth, Seventh, Eighth, Ninth> third(
                Third third);

        Union9<First, Second, Third, Fourth, Fifth, Sixth, Seventh, Eighth, Ninth> fourth(
                Fourth fourth);

        Union9<First, Second, Third, Fourth, Fifth, Sixth, Seventh, Eighth, Ninth> fifth(
                Fifth fifth);

        Union9<First, Second, Third, Fourth, Fifth, Sixth, Seventh, Eighth, Ninth> sixth(
                Sixth sixth);

        Union9<First, Second, Third, Fourth, Fifth, Sixth, Seventh, Eighth, Ninth> seventh(
                Seventh seventh);

        Union9<First, Second, Third, Fourth, Fifth, Sixth, Seventh, Eighth, Ninth> eighth(
                Eighth eighth);

        Union9<First, Second, Third, Fourth, Fifth, Sixth, Seventh, Eighth, Ninth> ninth(
                Ninth ninth);
    }
}
