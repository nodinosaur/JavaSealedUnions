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
import java.util.function.Supplier;

/**
 * Union1 represents a union containing an element of 1 possible type, or none at all
 * 
 * @param <First> The type represented by this union
 */
public interface Union1<First> {
    /**
     * Executes one of the continuations depending on the element type
     */
    void continued(Consumer<First> continuationFirst, Runnable continuationNone);

    /**
     * Transforms the element in the union to a new type
     * 
     * @param <R> result type
     * @return an object of the result type
     */
    <R> R join(Function<First, R> mapFirst, Supplier<R> mapNone);

    /**
     * Creator class for Union1
     */
    interface Factory<First> {
        /**
         * Creates a Union1 wrapping a value
         * 
         * @param single the value
         * @return a Union1 object wrapping the value
         */
        Union1<First> first(First single);

        /**
         * Creates a Union1 wrapping no value
         *
         * @return a Union1 object wrapping no value
         */
        Union1<First> none();
    }
}
