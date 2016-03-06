
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

public interface Union2<Left, Right> {
    void continued(Consumer<Left> continuationLeft, Consumer<Right> continuationRight);

    <R> R join(Function<Left, R> mapLeft, Function<Right, R> mapRight);

    interface Identifiable<Left, Right> extends Union2<Left, Right> {
        boolean isLeft();

        boolean isRight();
    }

    interface Factory<Left, Right> {
        Union2<Left, Right> left(Left left);

        Union2<Left, Right> right(Right right);
    }
}
