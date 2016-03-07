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

package com.pacoworks.sealedunions.generic;

import java.util.function.Consumer;
import java.util.function.Function;

import com.pacoworks.sealedunions.Union5;

class Union5First<A, B, C, D, E> implements Union5<A, B, C, D, E> {
    private final A value;

    public Union5First(A value) {
        this.value = value;
    }

    @Override
    public void continued(Consumer<A> continuationFirst, Consumer<B> continuationSecond,
            Consumer<C> continuationThird, Consumer<D> continuationFourth,
            Consumer<E> continuationFifth) {
        continuationFirst.accept(value);
    }

    @Override
    public <R> R join(Function<A, R> mapFirst, Function<B, R> mapSecond, Function<C, R> mapThird,
            Function<D, R> mapFourth, Function<E, R> mapFifth) {
        return mapFirst.apply(value);
    }
}
