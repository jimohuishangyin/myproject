package com.xmyunyou.wcd.model;

/**
 * Created by sanmee on 2014/12/17.
 */
public class MyFavValue {

    /*private int GoodID;
    private int favID;
    private int userID;
    private int zID;

    public int getGoodID() {
        return GoodID;
    }

    public void setGoodID(int goodID) {
        GoodID = goodID;
    }

    public int getFavID() {
        return favID;
    }

    public void setFavID(int favID) {
        this.favID = favID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getzID() {
        return zID;
    }

    public void setzID(int zID) {
        this.zID = zID;
    }*/

    public int UserID;
    public int NewsID;
    private int favID;

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

    public int getFavID() {
        return favID;
    }

    public void setFavID(int favID) {
        this.favID = favID;
    }
}
