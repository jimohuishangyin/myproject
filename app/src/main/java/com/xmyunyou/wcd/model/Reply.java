package com.xmyunyou.wcd.model;

import java.util.ArrayList;

/***
 * @author huangsm
 * @date 2014-1-14
 * @email huangsanm@gmail.com
 * @desc 回复
 */
public class Reply {

	// 回复ID
	public int ID;
	// 主题ID
	public int TopicID;
	// 用户id
	public int UserID;
	// 对回复的回复ID
	public int ToReplyID;
	// 内容
	public String Content;
	// 这边帖子有没有插入关联的游戏
	public int ToGameID;
	// 是否审核过
	public int Ischeck;
	// 创建时间
	public String CreateDate;
	// 发帖者的用户信息
	public User ReplyUser;
	//小编回复
	private String ReplyContent;
	private ArrayList<String> Pictures = new ArrayList<String>();
	
	//设备信息
	private String DeviceID;
	private String DeviceName;
	private String CreateIP;
	private int ReplyCount;
	
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public int getTopicID() {
		return TopicID;
	}
	public void setTopicID(int topicID) {
		TopicID = topicID;
	}
	public int getUserID() {
		return UserID;
	}
	public void setUserID(int userID) {
		UserID = userID;
	}
	public int getToReplyID() {
		return ToReplyID;
	}
	public void setToReplyID(int toReplyID) {
		ToReplyID = toReplyID;
	}
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}
	public int getToGameID() {
		return ToGameID;
	}
	public void setToGameID(int toGameID) {
		ToGameID = toGameID;
	}
	public int getIscheck() {
		return Ischeck;
	}
	public void setIscheck(int ischeck) {
		Ischeck = ischeck;
	}
	public String getCreateDate() {
		return CreateDate;
	}
	public void setCreateDate(String createDate) {
		CreateDate = createDate;
	}

    public User getReplyUser() {
        return ReplyUser;
    }

    public void setReplyUser(User replyUser) {
        ReplyUser = replyUser;
    }

    public ArrayList<String> getPictures() {
		return Pictures;
	}
	public void setPictures(ArrayList<String> pictures) {
		Pictures = pictures;
	}
	public String getReplyContent() {
		return ReplyContent;
	}
	public void setReplyContent(String ReplyContent) {
		this.ReplyContent = ReplyContent;
	}
	public String getDeviceID() {
		return DeviceID;
	}
	public void setDeviceID(String deviceID) {
		DeviceID = deviceID;
	}
	public String getDeviceName() {
		return DeviceName;
	}
	public void setDeviceName(String deviceName) {
		DeviceName = deviceName;
	}
	public String getCreateIP() {
		return CreateIP;
	}
	public void setCreateIP(String createIP) {
		CreateIP = createIP;
	}
	public int getReplyCount() {
		return ReplyCount;
	}
	public void setReplyCount(int replyCount) {
		ReplyCount = replyCount;
	}
}