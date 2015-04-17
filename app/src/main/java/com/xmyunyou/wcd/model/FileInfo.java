package com.xmyunyou.wcd.model;

import java.io.File;

/***
 * @author huangsm
 * @date 2014-1-8
 * @email huangsanm@gmail.com
 * @desc 文件信息
 */
public class FileInfo {

	private String name;
	private File file;
	private String endWith;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public String getEndWith() {
		if(file != null && file.exists()){
			String absolutePath = file.getAbsolutePath();
			endWith = absolutePath.substring(absolutePath.lastIndexOf("."));
		}
		return endWith;
	}
	
	
}
