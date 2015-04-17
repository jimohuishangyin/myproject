package com.xmyunyou.wcd.model;

/**
 * Created by sanmee on 2015/4/10.
 */
public class Integral {

    private int ID;
    private int UserID;
    private int Jifen;
    private String Description;
    private String CreateDate;
    private String UserName;
    private int BlanceJiFen;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public int getJifen() {
        return Jifen;
    }

    public void setJifen(int jifen) {
        Jifen = jifen;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String createDate) {
        CreateDate = createDate;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public int getBlanceJiFen() {
        return BlanceJiFen;
    }

    public void setBlanceJiFen(int blanceJiFen) {
        BlanceJiFen = blanceJiFen;
    }
}
