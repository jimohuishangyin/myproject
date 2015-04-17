package com.xmyunyou.wcd.model.json;

import com.xmyunyou.wcd.model.StopArticle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 95 on 2015/3/20.
 */
public class Gaizhuang_store implements Serializable {
    private List<StopArticle> TopShops = new ArrayList<StopArticle>();

    private List<StopArticle> Shops = new ArrayList<StopArticle>();

    public List<StopArticle> getTopShops() {
        return TopShops;
    }

    public void setTopShops(List<StopArticle> topShops) {
        TopShops = topShops;
    }

    public List<StopArticle> getShops() {
        return Shops;
    }

    public void setShops(List<StopArticle> shops) {
        Shops = shops;
    }
}
