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

import com.pacoworks.sealedunions.Union3;

final class Union3Third<T, U, V> implements Union3<T, U, V> {
    private final V value;

    public Union3Third(V value) {
        this.value = value;
    }

    @Override
    public void continued(Consumer<T> continuationFirst, Consumer<U> continuationSecond,
            Consumer<V> continuationThird) {
        continuationThird.accept(value);
    }

    @Override
    public <R> R join(Function<T, R> mapFirst, Function<U, R> mapSecond, Function<V, R> mapThird) {
        return mapThird.apply(value);
    }
}
