package com.xmyunyou.wcd.model.json;

import com.xmyunyou.wcd.model.Gaizhuang_stop_comment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 95 on 2015/4/9.
 */
public class Gaizhuang_shop_commentlist {

    private List<Gaizhuang_stop_comment> List = new ArrayList<Gaizhuang_stop_comment>();
    private int TotalCount;

    public List<Gaizhuang_stop_comment> getList() {
        return List;
    }

    public void setList(List<Gaizhuang_stop_comment> list) {
        List = list;
    }

    public int getTotalCount() {
        return TotalCount;
    }

    public void setTotalCount(int totalCount) {
        TotalCount = totalCount;
    }
}
