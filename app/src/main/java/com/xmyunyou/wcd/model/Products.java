package com.xmyunyou.wcd.model;

/**
 * Created by 95 on 2015/1/28.
 */
public class Products {
    private int ID;
    private String Name;
    private int Order;
    private String Url;
    private String Keywords;
    private String Description;
    private String Title;
    private String ImageUrl;
    private String ImageUrlHD;
    private int IsShow;

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
}
