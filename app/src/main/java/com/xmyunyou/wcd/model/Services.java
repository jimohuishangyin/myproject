package com.xmyunyou.wcd.model;

import java.io.Serializable;

/**
 * Created by 95 on 2015/3/18.
 */
public class Services implements Serializable {
    public int ID;
    public String Name;
    public int Order;
    public String Url;
    public String Keywords;
    public String Description;
    public String Title;
    public String ImageUrl;
    public String ImageUrlHD;
    public int IsShow;
    public int ShopNums;
    public int IsMobile;

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

    public int getOrder() {
        return Order;
    }

    public void setOrder(int order) {
        Order = order;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getKeywords() {
        return Keywords;
    }

    public void setKeywords(String keywords) {
        Keywords = keywords;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getImageUrlHD() {
        return ImageUrlHD;
    }

    public void setImageUrlHD(String imageUrlHD) {
        ImageUrlHD = imageUrlHD;
    }

    public int getIsShow() {
        return IsShow;
    }

    public void setIsShow(int isShow) {
        IsShow = isShow;
    }

    public int getShopNums() {
        return ShopNums;
    }

    public void setShopNums(int shopNums) {
        ShopNums = shopNums;
    }

    public int getIsMobile() {
        return IsMobile;
    }

    public void setIsMobile(int isMobile) {
        IsMobile = isMobile;
    }
}
