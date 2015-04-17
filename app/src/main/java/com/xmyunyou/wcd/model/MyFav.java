package com.xmyunyou.wcd.model;

/**
 * Created by sanmee on 2014/12/16.
 */
public class MyFav {

    public int UserID;

    public int NewsID;

    public int ID;

    public String CreateDate;

    //    内容
    public Article News;

    public int ToID ;
    public int Type;

    public int getToID() {
        return ToID;
    }

    public void setToID(int toID) {
        ToID = toID;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public int getNewsID() {
        return NewsID;
    }

    public void setNewsID(int newsID) {
        NewsID = newsID;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String createDate) {
        CreateDate = createDate;
    }

    public Article getNews() {
        return News;
    }

    public void setNews(Article news) {
        News = news;
    }
}
