package com.xmyunyou.wcd.model.json;

import com.xmyunyou.wcd.model.BrandCarLineType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 95 on 2015/4/14.
 */
public class BrandCarLineTypeList {
    private List<BrandCarLineType> List = new ArrayList<BrandCarLineType>();
    private int TotalCount;

    public List<BrandCarLineType> getList() {
        return List;
    }

    public void setList(List<BrandCarLineType> list) {
        List = list;
    }

    public int getTotalCount() {
        return TotalCount;
    }

    public void setTotalCount(int totalCount) {
        TotalCount = totalCount;
    }
}
