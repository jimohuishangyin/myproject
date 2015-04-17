package com.xmyunyou.wcd.model;

import java.io.Serializable;

/**
 * Created by 95 on 2015/4/14.
 */
public class CarManufacturer implements Serializable {

    public int ID;
    public String Name;
    public int CarModelCount;
    public String ImageUrl;
    public String ZiMu;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getCarModelCount() {
        return CarModelCount;
    }

    public void setCarModelCount(int carModelCount) {
        CarModelCount = carModelCount;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getZiMu() {
        return ZiMu;
    }

    public void setZiMu(String ziMu) {
        ZiMu = ziMu;
    }
}
