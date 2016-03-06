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

import java.util.function.Function;

import org.junit.Test;

import com.pacoworks.sealedunions.Union2;

public class EitherTest {
    @Test
    public void testContinued() throws Exception {
        String join = GenericUnions.eitherFactory().left("paco").join(value -> "hi",
                value -> "value");
    }

    @Test
    public void testJoin() throws Exception {
    }

    @Test
    public void testIs() throws Exception {
    }

    @Test
    public void testFactory() throws Exception {
    }

    public static class Salute {
        public static final Either.Factory<Dog, Neighbour> FACTORY = GenericUnions.eitherFactory();

        private final Union2<Dog, Neighbour> either;

        Salute(Union2<Dog, Neighbour> either) {
            this.either = either;
        }

        public static Salute dog(String name, int paws) {
            return new Salute(FACTORY.left(new Dog(name, paws)));
        }

        public static Salute neighbour(String name, String favouriteFood, boolean isLiked) {
            return new Salute(FACTORY.right(new Neighbour(name, favouriteFood, isLiked)));
        }

        public String getSalute(Function<Dog, String> mapDog,
                Function<Neighbour, String> mapNeighbour) {
            return either.join(mapDog, mapNeighbour);
        }


        private static class Dog {
            public Dog(String name, int paws) {
            }
        }

        private static class Neighbour {
            public Neighbour(String name, String favouriteFood, boolean isLiked) {
            }
        }
    }
}
