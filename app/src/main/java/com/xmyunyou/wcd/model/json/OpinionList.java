package com.xmyunyou.wcd.model.json;

import com.xmyunyou.wcd.model.Opinion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 95 on 2015/1/15.
 */
public class OpinionList {

    private List<Opinion> List =new ArrayList<Opinion>();
    private int TotalCount;

    public List<Opinion> getList() {
        return List;
    }

    public void setList(List<Opinion> list) {
        this.List = list;
    }

    public int getTotalCount() {
        return TotalCount;
    }

    public void setTotalCount(int totalCount) {
        TotalCount = totalCount;
    }
}
