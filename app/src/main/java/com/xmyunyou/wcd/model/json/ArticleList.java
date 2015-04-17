package com.xmyunyou.wcd.model.json;

import com.xmyunyou.wcd.model.Article;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sanmee on 2015/1/12.
 */
public class ArticleList implements Serializable{

    public int TotalCount;

    public List<Article> List = new ArrayList<Article>();


    public List<Article> getList() {
        return List;
    }

    public void setList(List<Article> list) {
        List = list;
    }

    public int getTotalCount() {
        return TotalCount;
    }

    public void setTotalCount(int totalCount) {
        TotalCount = totalCount;
    }



}
