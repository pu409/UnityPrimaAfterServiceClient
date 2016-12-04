package com.slamke.afterservice.domain;

import java.io.Serializable;
import java.util.Date;

public class CommentInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6849464582791220911L;
	private String num;
	private String content;
	private String remark;
	private Date time;

	public CommentInfo(String num, String content, String remark) {
		super();
		this.num = num;
		this.content = content;
		this.remark = remark;
		this.time = new Date();
	}
	
	public CommentInfo() {
		super();
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

}
