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

import static com.pacoworks.sealedunions.TennisGame.*;

import org.junit.Assert;
import org.junit.Test;

public class TennisGameTest {
    @Test
    public void testTennis() throws Exception {
        Score start = Score.points(PlayerPoints.zero(), PlayerPoints.zero());
        Score fifteenZero = scorePoint(start, Player.one());
        printScore(fifteenZero);
        Score thirtyZero = scorePoint(fifteenZero, Player.one());
        printScore(thirtyZero);
        Score fortyZero = scorePoint(thirtyZero, Player.one());
        printScore(fortyZero);
        Assert.assertEquals(Score.points(PlayerPoints.forty(), PlayerPoints.zero()).getScore(),
                fortyZero.getScore());
        Score fortyThirty = scorePoint(scorePoint(fortyZero, Player.two()), Player.two());
        printScore(fortyThirty);
        Assert.assertEquals(Score.points(PlayerPoints.forty(), PlayerPoints.thirty()).getScore(),
                fortyThirty.getScore());
        Score deuce1 = scorePoint(fortyThirty, Player.two());
        printScore(deuce1);
        Assert.assertEquals(Score.deuce().getScore(), deuce1.getScore());
        Score advantageP1 = scorePoint(deuce1, Player.one());
        printScore(advantageP1);
        Assert.assertEquals(Score.advantage(Advantage.one()).getScore(), advantageP1.getScore());
        Score gameP1 = scorePoint(advantageP1, Player.one());
        printScore(gameP1);
        Assert.assertEquals(Score.game(Game.one()).getScore(), gameP1.getScore());
        Score zeroFifteen = scorePoint(gameP1, Player.two());
        printScore(zeroFifteen);
        Assert.assertEquals(Score.points(PlayerPoints.zero(), PlayerPoints.fifteen()).getScore(),
                zeroFifteen.getScore());
        Score deuce2 = scorePoint(advantageP1, Player.two());
        printScore(deuce2);
        Assert.assertEquals(Score.deuce().getScore(), deuce2.getScore());
    }

    private void printScore(Score fortyZero) {
        System.out.println(Score.getString(fortyZero));
    }
}
