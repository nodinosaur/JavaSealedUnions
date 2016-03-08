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

import java.util.function.Function;

import javafx.util.Pair;

import com.pacoworks.sealedunions.generic.GenericUnions;

public class TennisGame {
    public static Score scorePoint(Score score, Player player) {
        return score.getScore().join(scorePoints(player), scoreAdvantage(player),
                scoreDeuce(player), scoreGame(player));
    }

    private static Function<Points, Score> scorePoints(Player player) {
        return points -> {
            if (isPlayerForty(points.getKey())) {
                return player.getPlayer().join(one -> Score.game(Game.one()),
                        two -> isPlayerThirty(points.getValue()) ? Score.deuce()
                                : scorePlayer(points.getKey(), score(points.getValue())));
            } else if (isPlayerForty(points.getValue())) {
                return player.getPlayer().join(
one -> isPlayerThirty(points.getKey()) ? Score.deuce()
                                : scorePlayer(score(points.getKey()), points.getValue()),
                                two -> Score.game(Game.two()));
            } else {
                return player.getPlayer().join(
                        one -> scorePlayer(score(points.getKey()), points.getValue()),
                        two -> scorePlayer(points.getKey(), score(points.getValue())));
            }
        };
    }

    private static Score scorePlayer(PlayerPoints playerOnePoints, PlayerPoints playerTwoPoints) {
        return Score.points(playerOnePoints, playerTwoPoints);
    }

    private static boolean isPlayerThirty(PlayerPoints playerPoints) {
        return playerPoints.getPlayerPoints().join(zero -> false, fifteen -> false, thirty -> true,
                forty -> false);
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
        return advantage -> advantage.getPlayer()
                .join(playerOne -> player.getPlayer().join(one -> Score.game(Game.one()),
                        two -> Score.deuce()),
                playerTwo -> player.getPlayer().join(one -> Score.deuce(),
                        two -> Score.game(Game.two())));
    }

    private static Function<Deuce, Score> scoreDeuce(Player player) {
        return deuce -> player.getPlayer().join(
                first -> Score.advantage(Advantage.one()),
                second -> Score.advantage(Advantage.two()));
    }

    private static Function<Game, Score> scoreGame(Player player) {
        return game -> player.getPlayer().join(
                one -> scorePlayer(PlayerPoints.fifteen(), PlayerPoints.zero()),
                second -> scorePlayer(PlayerPoints.zero(), PlayerPoints.fifteen()));
    }

    public interface Score {
        static Score points(PlayerPoints playerOnePoints, PlayerPoints playerTwoPoints) {
            return () -> GenericUnions.<Points, Advantage, Deuce, Game> quartetFactory()
                    .first(new Points(playerOnePoints, playerTwoPoints));
        }

        static Score advantage(Advantage advantage) {
            return () -> GenericUnions.<Points, Advantage, Deuce, Game> quartetFactory()
                    .second(advantage);
        }

        static Score deuce() {
            return () -> GenericUnions.<Points, Advantage, Deuce, Game> quartetFactory()
                    .third(new Deuce());
        }

        static Score game(Game game) {
            return () -> GenericUnions.<Points, Advantage, Deuce, Game> quartetFactory()
                    .fourth(game);
        }

        static String getString(Score score) {
            return score.getScore()
                    .join(Points::toString,
                            advantage -> advantage.getPlayer().join(one -> "Adv P1",
                                    two -> "Adv P2"),
                            deuce -> "Deuce",
                            game -> game.getPlayer().join(one -> "Win P1", two -> "Win P2"));
        }

        Union4<Points, Advantage, Deuce, Game> getScore();
    }

    public interface PlayerPoints {
        static PlayerPoints zero() {
            return () -> GenericUnions.<Zero, Fifteen, Thirty, Forty> quartetFactory()
                    .first(new Zero());
        }

        static PlayerPoints fifteen() {
            return () -> GenericUnions.<Zero, Fifteen, Thirty, Forty> quartetFactory()
                    .second(new Fifteen());
        }

        static PlayerPoints thirty() {
            return () -> GenericUnions.<Zero, Fifteen, Thirty, Forty> quartetFactory()
                    .third(new Thirty());
        }

        static PlayerPoints forty() {
            return () -> GenericUnions.<Zero, Fifteen, Thirty, Forty> quartetFactory()
                    .fourth(new Forty());
        }

        static String getString(PlayerPoints playerPoints) {
            return playerPoints.getPlayerPoints().join(zero -> "0", fifteen -> "15", thirty -> "30",
                    forty -> "40");
        }

        Union4<Zero, Fifteen, Thirty, Forty> getPlayerPoints();
    }

    public interface Advantage extends Player {
        static Advantage one() {
            return () -> GenericUnions.<PlayerOne, PlayerTwo> doubletFactory()
                    .first(new PlayerOne());
        }

        static Advantage two() {
            return () -> GenericUnions.<PlayerOne, PlayerTwo> doubletFactory()
                    .second(new PlayerTwo());
        }
    }

    public interface Game extends Player {
        static Game one() {
            return () -> GenericUnions.<PlayerOne, PlayerTwo> doubletFactory()
                    .first(new PlayerOne());
        }

        static Game two() {
            return () -> GenericUnions.<PlayerOne, PlayerTwo> doubletFactory()
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

        static String getString(Player player) {
            return player.getPlayer().join(playerOne -> "Player 1", playerTwo -> "Player 2");
        }

        Union2<PlayerOne, PlayerTwo> getPlayer();
    }

    public static class Points extends Pair<PlayerPoints, PlayerPoints> {
        Points(PlayerPoints key, PlayerPoints value) {
            super(key, value);
        }

        @Override
        public String toString() {
            return "Points{" + PlayerPoints.getString(getKey()) + ", "
                    + PlayerPoints.getString(getValue()) + "}";
        }
    }

    public static class Zero {
        public boolean equals(Object o) {
            return o == this || o instanceof Zero;
        }
    }

    public static class Fifteen {
        public boolean equals(Object o) {
            return o == this || o instanceof Fifteen;
        }
    }

    public static class Thirty {
        public boolean equals(Object o) {
            return o == this || o instanceof Thirty;
        }
    }

    public static class Forty {
        public boolean equals(Object o) {
            return o == this || o instanceof Forty;
        }
    }

    public static class Deuce {
        public boolean equals(Object o) {
            return o == this || o instanceof Deuce;
        }
    }

    public static class PlayerOne {
        public boolean equals(Object o) {
            return o == this || o instanceof PlayerOne;
        }
    }

    public static class PlayerTwo {
        public boolean equals(Object o) {
            return o == this || o instanceof PlayerTwo;
        }
    }
}
