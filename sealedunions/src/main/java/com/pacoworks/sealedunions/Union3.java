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

public interface Union3<Left, Middle, Right> {
    void continued(Consumer<Left> continuationLeft, Consumer<Middle> continuationMiddle,
            Consumer<Right> continuationRight);

    <R> R join(Function<Left, R> mapLeft, Function<Right, R> mapMiddle,
            Function<Right, R> mapRight);

    interface IdentifiableUnion3<Left, Middle, Right> extends Union3<Left, Middle, Right> {
        boolean isLeft();

        boolean isMiddle();

        boolean isRight();
    }

    interface Factory<Left, Middle, Right> {
        Union3<Left, Middle, Right> left(Left left);

        Union3<Left, Middle, Right> middle(Middle middle);

        Union3<Left, Middle, Right> right(Right right);
    }
}
