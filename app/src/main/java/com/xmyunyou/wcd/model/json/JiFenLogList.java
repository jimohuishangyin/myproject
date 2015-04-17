package com.xmyunyou.wcd.model.json;

import com.xmyunyou.wcd.model.JiFenLog;

import java.util.ArrayList;
import java.util.List;

public class JiFenLogList {

	private List<JiFenLog> List = new ArrayList<JiFenLog>();
	
	private Integer TotalCount;

	public List<JiFenLog> getList() {
		return List;
	}

	public void setList(List<JiFenLog> list) {
		List = list;
	}

	public Integer getTotalCount() {
		return TotalCount;
	}

	public void setTotalCount(Integer totalCount) {
		TotalCount = totalCount;
	}
	
	
	
}
