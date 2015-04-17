package com.xmyunyou.wcd.model;

import java.io.Serializable;
import java.util.ArrayList;

/***
 * @author huangsm
 * @date 2014-1-13
 * @email huangsanm@gmail.com
 * @desc 论坛
 */
public class Topic implements Serializable {

	private static final long serialVersionUID = 1L;
	//帖子id
	public int ID;
	//游戏id，所在的游戏版块
	public int GameID;
	//用户id
	public int UserID;
	//标题
	public String Title;
	//内容
	public String Content;
	//回复数
	public int ReplyCount;
	private String ReplyContent;
	//帖子图片地址
	public String ImageUrl;
	//这边帖子有没有插入关联的游戏
	public int ToGameID;
	//是否置顶
	public int IsTop;
	//是否精品
	public int IsBest;
	//是否推荐到游戏圈首页
	public int IsCommend;
	//是否审核过
	public int Ischeck;
	//排序
	public int Order;
	//创建时间
	public String CreateDate;
	//更新时间
	public String UpdateDate;
	//发帖者的用户信息
	public User TopicUser;
    public String Replies;
    //设备信息
    private String DeviceID;
    private String DeviceName;
    private String CreateIP;
    private ArrayList<String> Pictures = new ArrayList<String>();
    public String getReplies() {
        return Replies;
    }
    public void setReplies(String replies) {
        Replies = replies;
    }
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public int getGameID() {
		return GameID;
	}
	public void setGameID(int gameID) {
		GameID = gameID;
	}
	public int getUserID() {
		return UserID;
	}
	public void setUserID(int userID) {
		UserID = userID;
	}
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}
	public String getReplyContent() {
		return ReplyContent;
	}
	public void setReplyContent(String replyContent) {
		ReplyContent = replyContent;
	}
	public int getReplyCount() {
		return ReplyCount;
	}
	public void setReplyCount(int replyCount) {
		ReplyCount = replyCount;
	}
	public String getImageUrl() {
		return ImageUrl;
	}
	public void setImageUrl(String imageUrl) {
		ImageUrl = imageUrl;
	}
	public int getToGameID() {
		return ToGameID;
	}
	public void setToGameID(int toGameID) {
		ToGameID = toGameID;
	}
	public int getIsTop() {
		return IsTop;
	}
	public void setIsTop(int isTop) {
		IsTop = isTop;
	}
	public int getIsBest() {
		return IsBest;
	}
	public void setIsBest(int isBest) {
		IsBest = isBest;
	}
	public int getIsCommend() {
		return IsCommend;
	}
	public void setIsCommend(int isCommend) {
		IsCommend = isCommend;
	}
	public int getIscheck() {
		return Ischeck;
	}
	public void setIscheck(int ischeck) {
		Ischeck = ischeck;
	}
	public int getOrder() {
		return Order;
	}
	public void setOrder(int order) {
		Order = order;
	}
	public String getCreateDate() {
		return CreateDate;
	}
	public void setCreateDate(String createDate) {
		CreateDate = createDate;
	}
	public String getUpdateDate() {
		return UpdateDate;
	}
	public void setUpdateDate(String updateDate) {
		UpdateDate = updateDate;
	}
	public User getTopicUser() {
		return TopicUser;
	}
	public void setTopicUser(User topicUser) {
		TopicUser = topicUser;
	}
	public ArrayList<String> getPictures() {
		return Pictures;
	}
	public void setPictures(ArrayList<String> pictures) {
		Pictures = pictures;
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
}
