package com.xmyunyou.wcd.model;

/**
 * Created by sanmee on 2014/12/17.
 */
public class ArticleComment {

    // id
    public int ID;
    // 商品id
    public int GoodsID;
    // 用户名
    public String UserName;
    // 设备ID
    public String Content;
    // 创建日期
    public String CreateDate;
    // 创建IP
    public String CreateIP;
    // 赞
    public int Zan;
    // 设备名称
    public String DeviceName;
    // 排序，越大排在越前面
    public int Order;
    // 来自哪里
    public String From;
    public String DeviceID;

    public int getGoodsID() {
        return GoodsID;
    }

    public void setGoodsID(int goodsID) {
        GoodsID = goodsID;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String createDate) {
        CreateDate = createDate;
    }

    public String getCreateIP() {
        return CreateIP;
    }

    public void setCreateIP(String createIP) {
        CreateIP = createIP;
    }

    public int getZan() {
        return Zan;
    }

    public void setZan(int zan) {
        Zan = zan;
    }

    public String getDeviceName() {
        return DeviceName;
    }

    public void setDeviceName(String deviceName) {
        DeviceName = deviceName;
    }

    public int getOrder() {
        return Order;
    }

    public void setOrder(int order) {
        Order = order;
    }

    public String getFrom() {
        return From;
    }

    public void setFrom(String from) {
        From = from;
    }

    public String getDeviceID() {
        return DeviceID;
    }

    public void setDeviceID(String deviceID) {
        DeviceID = deviceID;
    }
}
