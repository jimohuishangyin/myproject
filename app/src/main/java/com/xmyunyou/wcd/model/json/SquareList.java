package com.xmyunyou.wcd.model.json;

import com.xmyunyou.wcd.model.Topic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sanmee on 2015/4/9.
 */
public class SquareList {

    private List<Topic> CommendTopics = new ArrayList<>();
    private String Stats;

    public List<Topic> getCommendTopics() {
        return CommendTopics;
    }

    public void setCommendTopics(List<Topic> commendTopics) {
        CommendTopics = commendTopics;
    }

    public String getStats() {
        return Stats;
    }

    public void setStats(String stats) {
        Stats = stats;
    }
}
