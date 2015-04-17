package com.xmyunyou.wcd.model;

/**
 * Created by sanmee on 2015/1/13.
 */
public class Category {

    private int ID;
    private String Name;
    private String Url;
    private int ParentID;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getParentID() {
        return ParentID;
    }

    public void setParentID(int parentID) {
        ParentID = parentID;
    }
}
