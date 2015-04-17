package com.xmyunyou.wcd.model;
/***
 * @desc 积分记录
 */
public class JiFenLog {
	
    // ID
    public int ID;
    // 用户id
    public int UserID;
    // 积分
    public int Jifen;
    // 描述
    public String Description;
    //任务创建时间
    public String CreateDate;
    public String UserName;
    public int BlanceJiFen;

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

    public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
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

	
}
