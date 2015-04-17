package com.xmyunyou.wcd.model;

import java.io.Serializable;

/**
 * Created by 95 on 2015/4/17.
 */
public class MyFavTopic implements Serializable {
    public int ID;
    public int UserID;
    public String CreateDate;
    public Topic Topic;
    public int ToID;
    public int Type;

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

    public String getCreatDate() {
        return CreateDate;
    }

    public void setCreatDate(String creatDate) {
        CreateDate = creatDate;
    }

    public Topic getTopic() {
        return Topic;
    }

    public void setTopic(Topic topic) {
        Topic = topic;
    }

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
}
