package com.xmyunyou.wcd.model;

import java.io.Serializable;

/**
 * Created by 95 on 2015/1/12.
 */
public class User implements Serializable {

    public int ID ;
    public String Name ;
    public String Password ;

    public String ClientPassword ;
    /// <summary>
    /// 0为普通用户，2为小编，3为管理员
    /// </summary>
    public int Type ;
    public int LevelID ;
    public int PhoneID ;
    public String Email ;
    public String QQ ;
    public String OtherContact ;
    public String LoginDate ;
    public String LoginIP ;
    public String LastLoginDate ;
    public String LastLoginIP ;
    public String CreateDate ;
    public String RealName ;
    public String UserNO ;

    public int IsCheck ;
    public int Coin ;
    public int JiFen ;
    public int CheckInNums ;
    public int BBSTopicNums;
    public int BBSReplyNums ;
    public String Phone;
    public String UpdateDate;

    public String AvatarUrl ;

    public String CreateIP;
    public String DeviceID ;

    public String PushUserID ;
    public String PushChannelID ;
    public Boolean EnablePush ;
    public String LoginDeviceID ;
    /// <summary>
    /// 推荐的用户名
    /// </summary>
    public String CommendUser ;

    /// <summary>
    /// 勋章id列表，逗号分隔
    /// </summary>
    public String Medals ;

    public String QQOpenID ;

    public int CarID ;

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

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getClientPassword() {
        return ClientPassword;
    }

    public void setClientPassword(String clientPassword) {
        ClientPassword = clientPassword;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public int getLevelID() {
        return LevelID;
    }

    public void setLevelID(int levelID) {
        LevelID = levelID;
    }

    public int getPhoneID() {
        return PhoneID;
    }

    public void setPhoneID(int phoneID) {
        PhoneID = phoneID;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getQQ() {
        return QQ;
    }

    public void setQQ(String QQ) {
        this.QQ = QQ;
    }

    public String getOtherContact() {
        return OtherContact;
    }

    public void setOtherContact(String otherContact) {
        OtherContact = otherContact;
    }

    public String getLoginDate() {
        return LoginDate;
    }

    public void setLoginDate(String loginDate) {
        LoginDate = loginDate;
    }

    public String getLoginIP() {
        return LoginIP;
    }

    public void setLoginIP(String loginIP) {
        LoginIP = loginIP;
    }

    public String getLastLoginDate() {
        return LastLoginDate;
    }

    public void setLastLoginDate(String lastLoginDate) {
        LastLoginDate = lastLoginDate;
    }

    public String getLastLoginIP() {
        return LastLoginIP;
    }

    public void setLastLoginIP(String lastLoginIP) {
        LastLoginIP = lastLoginIP;
    }

    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String createDate) {
        CreateDate = createDate;
    }

    public String getRealName() {
        return RealName;
    }

    public void setRealName(String realName) {
        RealName = realName;
    }

    public String getUserNO() {
        return UserNO;
    }

    public void setUserNO(String userNO) {
        UserNO = userNO;
    }

    public int getIsCheck() {
        return IsCheck;
    }

    public void setIsCheck(int isCheck) {
        IsCheck = isCheck;
    }

    public int getCoin() {
        return Coin;
    }

    public void setCoin(int coin) {
        Coin = coin;
    }

    public int getJiFen() {
        return JiFen;
    }

    public void setJiFen(int jiFen) {
        JiFen = jiFen;
    }

    public int getCheckInNums() {
        return CheckInNums;
    }

    public void setCheckInNums(int checkInNums) {
        CheckInNums = checkInNums;
    }

    public int getBBSTopicNums() {
        return BBSTopicNums;
    }

    public void setBBSTopicNums(int BBSTopicNums) {
        this.BBSTopicNums = BBSTopicNums;
    }

    public int getBBSReplyNums() {
        return BBSReplyNums;
    }

    public void setBBSReplyNums(int BBSReplyNums) {
        this.BBSReplyNums = BBSReplyNums;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getUpdateDate() {
        return UpdateDate;
    }

    public void setUpdateDate(String updateDate) {
        UpdateDate = updateDate;
    }

    public String getAvatarUrl() {
        return AvatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        AvatarUrl = avatarUrl;
    }

    public String getCreateIP() {
        return CreateIP;
    }

    public void setCreateIP(String createIP) {
        CreateIP = createIP;
    }

    public String getDeviceID() {
        return DeviceID;
    }

    public void setDeviceID(String deviceID) {
        DeviceID = deviceID;
    }

    public String getPushUserID() {
        return PushUserID;
    }

    public void setPushUserID(String pushUserID) {
        PushUserID = pushUserID;
    }

    public String getPushChannelID() {
        return PushChannelID;
    }

    public void setPushChannelID(String pushChannelID) {
        PushChannelID = pushChannelID;
    }

    public Boolean getEnablePush() {
        return EnablePush;
    }

    public void setEnablePush(Boolean enablePush) {
        EnablePush = enablePush;
    }

    public String getLoginDeviceID() {
        return LoginDeviceID;
    }

    public void setLoginDeviceID(String loginDeviceID) {
        LoginDeviceID = loginDeviceID;
    }

    public String getCommendUser() {
        return CommendUser;
    }

    public void setCommendUser(String commendUser) {
        CommendUser = commendUser;
    }

    public String getMedals() {
        return Medals;
    }

    public void setMedals(String medals) {
        Medals = medals;
    }

    public String getQQOpenID() {
        return QQOpenID;
    }

    public void setQQOpenID(String QQOpenID) {
        this.QQOpenID = QQOpenID;
    }

    public int getCarID() {
        return CarID;
    }

    public void setCarID(int carID) {
        CarID = carID;
    }
}
