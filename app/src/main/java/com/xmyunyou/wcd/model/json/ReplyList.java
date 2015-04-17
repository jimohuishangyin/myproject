package com.xmyunyou.wcd.model.json;

import com.xmyunyou.wcd.model.Reply;
import com.xmyunyou.wcd.model.Topic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sanmee on 2015/3/30.
 */
public class ReplyList {

    private List<Reply> List = new ArrayList<>();

    private int TotalCount;

    public List<Reply> getList() {
        return List;
    }

    public void setList(List<Reply> list) {
        List = list;
    }

    public int getTotalCount() {
        return TotalCount;
    }

    public void setTotalCount(int totalCount) {
        TotalCount = totalCount;
    }
}
