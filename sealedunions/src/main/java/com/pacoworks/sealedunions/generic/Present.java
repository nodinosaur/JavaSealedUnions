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
import java.util.function.Supplier;

final class Present<T> implements Result<T> {
    private final T value;

    public Present(T value) {
        this.value = value;
    }

    @Override
    public void continued(Consumer<T> continuationFirst, Runnable continuationNone) {
        continuationFirst.accept(value);
    }

    @Override
    public <R> R join(Function<T, R> mapFirst, Supplier<R> mapNone) {
        return mapFirst.apply(value);
    }
}
