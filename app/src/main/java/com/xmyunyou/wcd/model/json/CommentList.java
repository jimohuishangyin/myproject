package com.xmyunyou.wcd.model.json;

import com.xmyunyou.wcd.model.ArticleComment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sanmee on 2014/12/17.
 */
public class CommentList {

    private List<ArticleComment> List = new ArrayList<ArticleComment>();
    private int TotalCount;


    public List<ArticleComment> getList() {
        return List;
    }

    public void setList(List<ArticleComment> list) {
        List = list;
    }

    public int getTotalCount() {
        return TotalCount;
    }

    public void setTotalCount(int totalCount) {
        TotalCount = totalCount;
    }
}
