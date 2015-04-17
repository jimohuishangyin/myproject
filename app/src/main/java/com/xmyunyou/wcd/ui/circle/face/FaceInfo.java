package com.xmyunyou.wcd.ui.circle.face;
/***
 * @author huangsm
 * @date 2014-4-11
 * @email huangsanm@gmail.com
 * @desc 表情实体
 */
public class FaceInfo {

	//对应的资源id
	private int id;
	//显示中文
	private String character;
	//文件名称
	private String name;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCharacter() {
		return character;
	}
	public void setCharacter(String character) {
		this.character = character;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}