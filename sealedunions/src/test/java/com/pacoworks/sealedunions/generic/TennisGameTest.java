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

import org.junit.Assert;
import org.junit.Test;

public class TennisGameTest {
    @Test
    public void testTennis() throws Exception {
        TennisGame.Score advantageP1 = TennisGame.scorePoint(TennisGame.Score.deuce(),
                TennisGame.Player.one());
        Assert.assertEquals(TennisGame.Score.advantage(TennisGame.Advantage.one()).getScore(),
                advantageP1.getScore());
        Assert.assertEquals(TennisGame.Score.game(TennisGame.Game.one()).getScore(),
                TennisGame.scorePoint(advantageP1, TennisGame.Player.one()).getScore());
    }
}
