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

import com.pacoworks.sealedunions.Union6;

final class Union6Second<A, B, C, D, E, F> implements Union6<A, B, C, D, E, F> {
    private final B value;

    public Union6Second(B value) {
        this.value = value;
    }

    @Override
    public void continued(Consumer<A> continuationFirst, Consumer<B> continuationSecond,
            Consumer<C> continuationThird, Consumer<D> continuationFourth,
            Consumer<E> continuationFifth, Consumer<F> continuationSixth) {
        continuationSecond.accept(value);
    }

    @Override
    public <R> R join(Function<A, R> mapFirst, Function<B, R> mapSecond, Function<C, R> mapThird,
            Function<D, R> mapFourth, Function<E, R> mapFifth, Function<F, R> mapSixth) {
        return mapSecond.apply(value);
    }

    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Union6Second))
            return false;
        final Union6Second other = (Union6Second)o;
        final Object this$value = this.value;
        final Object other$value = other.value;
        return this$value == null ? other$value == null : this$value.equals(other$value);
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $value = this.value;
        result = result * PRIME + ($value == null ? 0 : $value.hashCode());
        return result;
    }
}
