package com.xmyunyou.wcd.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 95 on 2015/3/18.
 */
public class Gaizhuang_services implements Serializable {

    private List<Article> HuanDengs = new ArrayList<Article>();

    private List<Services> Services = new ArrayList<Services>();

    public List<Article> getHuanDengs() {
        return HuanDengs;
    }

    public void setHuanDengs(List<Article> huanDengs) {
        HuanDengs = huanDengs;
    }

    public List<Services> getServices() {
        return Services;
    }

    public void setServices(List<Services> services) {
        Services = services;
    }
}
