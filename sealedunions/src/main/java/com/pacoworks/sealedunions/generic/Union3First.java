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

import com.pacoworks.sealedunions.Union3;

import java.util.function.Consumer;
import java.util.function.Function;

final class Union3First<T, U, V> implements Union3<T, U, V> {
    private final T value;

    public Union3First(T value) {
        this.value = value;
    }

    @Override
    public void continued(Consumer<T> continuationFirst, Consumer<U> continuationSecond,
            Consumer<V> continuationThird) {
        continuationFirst.accept(value);
    }

    @Override
    public <R> R join(Function<T, R> mapFirst, Function<U, R> mapSecond, Function<V, R> mapThird) {
        return mapFirst.apply(value);
    }

    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Union3First))
            return false;
        final Union3First other = (Union3First)o;
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

    @Override
    public String toString() {
        return value.toString();
    }
}
