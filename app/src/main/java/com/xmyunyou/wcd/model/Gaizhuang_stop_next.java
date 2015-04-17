package com.xmyunyou.wcd.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 95 on 2015/3/20.
 */
public class Gaizhuang_stop_next implements Serializable{
    private List<StopArticle> List = new ArrayList<StopArticle>();
    private int TotalCount;

    public List<StopArticle> getList() {
        return List;
    }

    public void setList(List<StopArticle> list) {
        List = list;
    }

    public int getTotalCount() {
        return TotalCount;
    }

    public void setTotalCount(int totalCount) {
        TotalCount = totalCount;
    }
}
