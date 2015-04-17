package com.xmyunyou.wcd.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 95 on 2015/3/23.
 */
public class Gaizhuang_production implements Serializable {
    private List<ProductArticle> List = new ArrayList<ProductArticle>();
    private int TotalCount;

    public List<ProductArticle> getList() {
        return List;
    }

    public void setList(List<ProductArticle> list) {
        List = list;
    }

    public int getTotalCount() {
        return TotalCount;
    }

    public void setTotalCount(int totalCount) {
        TotalCount = totalCount;
    }
}
