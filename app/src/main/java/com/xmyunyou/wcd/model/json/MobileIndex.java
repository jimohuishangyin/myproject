package com.xmyunyou.wcd.model.json;

import com.xmyunyou.wcd.model.Article;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 95 on 2015/3/9.
 */
public class MobileIndex implements Serializable {
    private List<Article> HuanDengs = new ArrayList<Article>();
    private List<Article> Gaizhuangs = new ArrayList<Article>();
    public List<Article> getHuanDengs() {
        return HuanDengs;
    }

    public void setHuanDengs(List<Article> huanDengs) {
        HuanDengs = huanDengs;
    }

    public List<Article> getGaizhuangs() {
        return Gaizhuangs;
    }

    public void setGaizhuangs(List<Article> gaizhuangs) {
        Gaizhuangs = gaizhuangs;
    }

}
