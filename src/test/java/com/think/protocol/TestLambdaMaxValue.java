package com.think.protocol;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class TestLambdaMaxValue {

    @Test
    public void testLambdaMaxValue() {
        List<Integer> list = Arrays.asList(13, 25, 6, 534, 234, 76, 3, 53, 26, 84);
        int maxValue = list.stream().reduce(0, Integer::max);
        assertEquals(maxValue, 534);

        List<BattlePlayer> playerList = Lists.newArrayList();
        playerList.add(new BattlePlayer("team1", 20));
        playerList.add(new BattlePlayer("team2", 30));
        playerList.add(new BattlePlayer("team3", 40));
        playerList.add(new BattlePlayer("team4", 50));
        playerList.add(new BattlePlayer("team1", 20));
        playerList.add(new BattlePlayer("team2", 30));
        playerList.add(new BattlePlayer("team3", 40));
        playerList.add(new BattlePlayer("team4", 50));
        playerList.add(new BattlePlayer("team1", 20));
        playerList.add(new BattlePlayer("team2", 30));
        playerList.add(new BattlePlayer("team3", 40));
        playerList.add(new BattlePlayer("team4", 50));

        Map<String, Integer> scoreMap = playerList.stream().collect(Collectors.groupingBy(p -> p.teamId, Collectors.summingInt(p->p.score)));
        scoreMap.forEach((k, v) -> System.out.println("key=" + k + " value = " + v));
    }


    class BattlePlayer {
        public String teamId;
        public int score;

        public BattlePlayer(String teamId, int score) {
            this.teamId = teamId;
            this.score = score;
        }
    }

}