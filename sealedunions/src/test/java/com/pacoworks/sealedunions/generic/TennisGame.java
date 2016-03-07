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

import javafx.util.Pair;

import com.pacoworks.sealedunions.Union2;
import com.pacoworks.sealedunions.Union4;

public class TennisGame {
    public static Score scorePoint(Score score, Player player) {
        return score.getScore().join(scorePoints(player), scoreAdvantage(player),
                scoreDeuce(player), scoreGame(player));
    }

    private static Function<Points, Score> scorePoints(Player player) {
        return points -> {
            if (isPlayerForty(points.getKey())) {
                return Score.game(Game.one());
            } else if (isPlayerForty(points.getValue())) {
                return Score.game(Game.two());
            } else {
                return player.getPlayer().join(
                        one -> Score.points(new Points(score(points.getKey()), points.getValue())),
                        two -> Score.points(new Points(points.getValue(), score(points.getValue()))));
            }
        };
    }

    private static boolean isPlayerForty(PlayerPoints playerPoints) {
        return playerPoints.getPlayerPoints().join(
                zero -> false,
                fifteen -> false,
                thirty -> false,
                forty -> true);
    }

    private static PlayerPoints score(PlayerPoints playerPoints) {
        return playerPoints.getPlayerPoints().join(
                zero -> PlayerPoints.fifteen(),
                fifteen -> PlayerPoints.thirty(),
                thirty -> PlayerPoints.forty(),
                forty -> {
                    throw new IllegalStateException();
                });
    }

    private static Function<Advantage, Score> scoreAdvantage(Player player) {
        return advantage -> player.getPlayer()
                .join(
                        playerOne -> advantage.getPlayer().join(
                                one -> Score.game(Game.one()),
                                two -> Score.deuce()),
                        playerTwo -> advantage.getPlayer().join(
                                one -> Score.advantage(Advantage.one()),
                                two -> Score.deuce()));
    }

    private static Function<Deuce, Score> scoreDeuce(Player player) {
        return deuce -> player.getPlayer().join(
                first -> Score.advantage(Advantage.one()),
                second -> Score.advantage(Advantage.two()));
    }

    private static Function<Game, Score> scoreGame(Player player) {
        return game -> player.getPlayer().join(
                one -> Score.points(new Points(PlayerPoints.fifteen(), PlayerPoints.zero())),
                second -> Score.points(new Points(PlayerPoints.zero(), PlayerPoints.fifteen())));
    }

    public interface Score {
        static Score points(Points points) {
            return () -> GenericUnions.<Points, Advantage, Deuce, Game>quartetFactory()
                    .first(points);
        }

        static Score advantage(Advantage advantage) {
            return () -> GenericUnions.<Points, Advantage, Deuce, Game>quartetFactory()
                    .second(advantage);
        }

        static Score deuce() {
            return () -> GenericUnions.<Points, Advantage, Deuce, Game>quartetFactory()
                    .third(new Deuce());
        }

        static Score game(Game game) {
            return () -> GenericUnions.<Points, Advantage, Deuce, Game>quartetFactory()
                    .fourth(game);
        }

        Union4<Points, Advantage, Deuce, Game> getScore();
    }

    public interface PlayerPoints {
        static PlayerPoints zero() {
            return () -> GenericUnions.<Zero, Fifteen, Thirty, Forty>quartetFactory()
                    .first(new Zero());
        }

        static PlayerPoints fifteen() {
            return () -> GenericUnions.<Zero, Fifteen, Thirty, Forty>quartetFactory()
                    .second(new Fifteen());
        }

        static PlayerPoints thirty() {
            return () -> GenericUnions.<Zero, Fifteen, Thirty, Forty>quartetFactory()
                    .third(new Thirty());
        }

        static PlayerPoints forty() {
            return () -> GenericUnions.<Zero, Fifteen, Thirty, Forty>quartetFactory()
                    .fourth(new Forty());
        }

        Union4<Zero, Fifteen, Thirty, Forty> getPlayerPoints();
    }

    public interface Advantage extends Player {
        static Advantage one() {
            return () -> GenericUnions.<PlayerOne, PlayerTwo>doubletFactory()
                    .first(new PlayerOne());
        }

        static Advantage two() {
            return () -> GenericUnions.<PlayerOne, PlayerTwo>doubletFactory()
                    .second(new PlayerTwo());
        }
    }

    public interface Game extends Player {
        static Game one() {
            return () -> GenericUnions.<PlayerOne, PlayerTwo>doubletFactory()
                    .first(new PlayerOne());
        }

        static Game two() {
            return () -> GenericUnions.<PlayerOne, PlayerTwo>doubletFactory()
                    .second(new PlayerTwo());
        }
    }

    public interface Player {
        static Player one() {
            return () -> GenericUnions.<PlayerOne, PlayerTwo> doubletFactory()
                    .first(new PlayerOne());
        }

        static Player two() {
            return () -> GenericUnions.<PlayerOne, PlayerTwo> doubletFactory()
                    .second(new PlayerTwo());
        }

        Union2<PlayerOne, PlayerTwo> getPlayer();
    }

    public static class Points extends Pair<PlayerPoints, PlayerPoints> {
        public Points(PlayerPoints key, PlayerPoints value) {
            super(key, value);
        }
    }

    public static class Zero {
    }

    public static class Fifteen {
    }

    public static class Thirty {
    }

    public static class Forty {
    }

    public static class Deuce {
    }

    public static class PlayerOne {
    }

    public static class PlayerTwo {
    }
}
