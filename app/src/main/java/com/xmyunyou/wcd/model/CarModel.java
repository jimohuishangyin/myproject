package com.xmyunyou.wcd.model;

import java.io.Serializable;

/**
 * Created by 95 on 2015/4/14.
 */
public class CarModel implements Serializable {
    public int ID;
    public String Name;
    public int CarManufacturerID;
    public String SaleDate;
    public String ImageUrl;
    public String ZiMu;
    public int CarCount;
    public String Factory;
    public int IsHot;
    public int IsShow;
    public int ViewNums;
    public String ImageUrlHD;

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

    public int getCarManufacturerID() {
        return CarManufacturerID;
    }

    public void setCarManufacturerID(int carManufacturerID) {
        CarManufacturerID = carManufacturerID;
    }

    public String getSaleDate() {
        return SaleDate;
    }

    public void setSaleDate(String saleDate) {
        SaleDate = saleDate;
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

    public int getCarCount() {
        return CarCount;
    }

    public void setCarCount(int carCount) {
        CarCount = carCount;
    }

    public String getFactory() {
        return Factory;
    }

    public void setFactory(String factory) {
        Factory = factory;
    }

    public int getIsHot() {
        return IsHot;
    }

    public void setIsHot(int isHot) {
        IsHot = isHot;
    }

    public int getIsShow() {
        return IsShow;
    }

    public void setIsShow(int isShow) {
        IsShow = isShow;
    }

    public int getViewNums() {
        return ViewNums;
    }

    public void setViewNums(int viewNums) {
        ViewNums = viewNums;
    }

    public String getImageUrlHD() {
        return ImageUrlHD;
    }

    public void setImageUrlHD(String imageUrlHD) {
        ImageUrlHD = imageUrlHD;
    }
}
