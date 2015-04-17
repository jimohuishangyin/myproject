package com.xmyunyou.wcd.model;

/**
 * Created by 95 on 2015/4/3.
 */
public class City {
    private int CityID;
    private String Name;
    private int ProvinceID;
    private String ZiMu;

    public int getCityID() {
        return CityID;
    }

    public void setCityID(int cityID) {
        CityID = cityID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getProvinceID() {
        return ProvinceID;
    }

    public void setProvinceID(int provinceID) {
        ProvinceID = provinceID;
    }

    public String getZiMu() {
        return ZiMu;
    }

    public void setZiMu(String ziMu) {
        ZiMu = ziMu;
    }


}
