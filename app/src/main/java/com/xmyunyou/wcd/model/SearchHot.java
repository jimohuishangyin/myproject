package com.xmyunyou.wcd.model;

/**
 * Created by 95 on 2015/2/2.
 */
public class SearchHot {
    private int ID;
    private String Name;
    private int CarManufacturerID;
    private String SaleDate;
    private String ImageUrl;
    private String ZiMu;
    private int CarCount ;
    private String Factory;
    private int IsHot;
    private int IsShow;
    private int ViewNums;
    private String ImageUrlHD;

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
