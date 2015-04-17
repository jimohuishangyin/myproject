package com.xmyunyou.wcd.model.json;

import com.xmyunyou.wcd.model.MyFav;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 95 on 2015/1/19.
 */
public class MyFavList {

    private int TotalCount;
    private List<MyFav> List = new ArrayList<MyFav>();

    public int getTotalCount() {
        return TotalCount;
    }

    public void setTotalCount(int totalCount) {
        TotalCount = totalCount;
    }

    public List<MyFav> getList() {
        return List;
    }

    public void setList(List<MyFav> list) {
        List = list;
    }
}
