package com.xmyunyou.wcd.model.json;

import com.xmyunyou.wcd.model.MyFavTopic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 95 on 2015/4/17.
 */
public class MyFavTopicList {
    private int TotalCount;
    private List<MyFavTopic> List = new ArrayList<MyFavTopic>();

    public int getTotalCount() {
        return TotalCount;
    }

    public void setTotalCount(int totalCount) {
        TotalCount = totalCount;
    }

    public List<MyFavTopic> getList() {
        return List;
    }

    public void setList(List<MyFavTopic> list) {
        List = list;
    }
}
